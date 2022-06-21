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
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ParseService parseService;
    private final ArtifactResolveService artifactResolver;
    private final NodeService nodeService;

    @Override
    public Project createProjectFromModel(Model model) {
        Project project = new Project();
        project.setName(model.getArtifactId());
        project.setParentNode(createParent(model));
        project.setNodes(createNodes(model));
        return project;
    }

    private List<Node> createNodes(Model model) {
        List<Node> nodes = new ArrayList<>();
        List<Dependency> dependencies = getDependenciesFromModel(model);
        for (Dependency dependency : dependencies) {
            nodes.add(nodeService.getNodeFromDependency(dependency));
        }
        return nodes;
    }

    private ParentNode createParent(Model model) {
        Parent parent = model.getParent();
        if (parent != null) {
            ParentNode parentNode = new ParentNode();
            Artifact artifact = artifactResolver.resolve(getArtifactFromParent(parent));
            parentNode.setUp(nodeService.getNodeFromDependency(new Dependency(artifact, null)));
            File file = artifact.getFile();
            if (file != null) {
                parseService.getModelFromFile(file).ifPresent(m -> parentNode.setParentNode(createParent(m)));
            }
            return parentNode;
        }
        return null;
    }

    private List<Dependency> getDependenciesFromModel(Model model) {
        List<Dependency> dependencies = new ArrayList<>();
        model.getDependencies().forEach(d -> {
            Artifact artifact = new DefaultArtifact(d.getGroupId(),
                    d.getArtifactId(), d.getType(), d.getVersion());
            dependencies.add(new Dependency(artifact, d.getScope()));
        });
        return dependencies;
    }

    private Artifact getArtifactFromParent(Parent parent) {
        return new DefaultArtifact(parent.getGroupId(),
                parent.getArtifactId(), "pom", parent.getVersion());
    }

}
