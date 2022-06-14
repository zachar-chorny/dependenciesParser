package com.example.parse.service;

import com.example.parse.model.Node;
import com.example.parse.model.ParentNode;
import com.example.parse.model.Project;
import com.example.parse.model.RepositoriesDto;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ParseService parseService;
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession repositorySystemSession;
    private final RepositoriesDto repositoriesDto;
    private final ArtifactResolveService artifactResolver;

    @Override
    public Project createProjectFromModel(Model model) {
        Project project = new Project();
        project.setName(model.getName());
        project.setParentNode(createParent(model));
        project.setNodes(createNodes(model));
        return project;
    }

    private List<Node> createNodes(Model model) {
        List<Node> nodes = new ArrayList<>();
        List<Artifact> artifacts = getArtifactsFromModel(model);
        for (Artifact artifact : artifacts) {
            nodes.add(getNodeFromDependency(artifact));
        }
        return nodes;
    }

    private ParentNode createParent(Model model) {
        Parent parent = model.getParent();
        if (parent != null) {
            ParentNode parentNode = new ParentNode();
            Artifact artifact = artifactResolver.resolve(getArtifactFromParent(parent));
            parentNode.setUp(getNodeFromDependency(artifact));
            File file = artifact.getFile();
            if (file != null) {
                parseService.getModelFromFile(file).ifPresent(m -> parentNode.setParentNode(createParent(m)));
            }
            return parentNode;
        }
        return null;
    }

    private Node getNodeFromDependency(Artifact artifact) {
        String scope = artifact.getProperties().get("scope");
        Node node = Node.builder().groupId(artifact.getGroupId())
                .artifactId(artifact.getArtifactId()).version(artifact.getVersion())
                .type(artifact.getExtension())
                .scope(scope).children(new ArrayList<>()).build();
        Optional<DependencyNode> dependencyNode = getDependencyNodeFromArtifact(artifact);
        if (dependencyNode.isPresent()) {
            List<DependencyNode> dependencyNodes = dependencyNode.get().getChildren();
            dependencyNodes.forEach(d -> node.getChildren().add(getNodeFromDependency(d.getArtifact())));
        }
        return node;
    }

    private Optional<DependencyNode> getDependencyNodeFromArtifact(Artifact artifact) {
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.setRepositories(repositoriesDto.getRepositories());
        try {
            CollectResult collectResult = repositorySystem.collectDependencies(repositorySystemSession, collectRequest);
            return Optional.of(collectResult.getRoot());
        } catch (DependencyCollectionException e) {
            return Optional.empty();
        }
    }

    private List<Artifact> getArtifactsFromModel(Model model) {
        List<Artifact> artifacts = new ArrayList<>();
        model.getDependencies().forEach(d -> {
            Map<String, String> properties = new HashMap<>();
            properties.put("scope", d.getScope());
            artifacts.add(new DefaultArtifact(d.getGroupId(),
                    d.getArtifactId(), d.getType(), d.getVersion()).setProperties(properties));
        });
        return artifacts;
    }

    private Artifact getArtifactFromParent(Parent parent) {
        return new DefaultArtifact(parent.getGroupId(),
                parent.getArtifactId(), "pom", parent.getVersion());
    }

}
