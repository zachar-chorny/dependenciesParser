package com.example.parse.service;

import com.example.parse.model.Node;
import com.example.parse.model.RepositoriesDto;
import lombok.AllArgsConstructor;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NodeServiceImpl implements NodeService {
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession repositorySystemSession;
    private final RepositoriesDto repositoriesDto;

    @Override
    public Node getNodeFromDependency(Dependency dependency) {
        Artifact artifact = dependency.getArtifact();
        Node node = buildNode(dependency);
        Optional<DependencyNode> dependencyNode = getDependencyNodeFromArtifact(artifact);
        if (dependencyNode.isPresent()) {
            List<DependencyNode> dependencyNodes = dependencyNode.get().getChildren();
            node.setChildren(getChildren(dependencyNodes));
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

    private List<Node> getChildren(List<DependencyNode> dependencyNodes) {
        List<Node> nodes = new ArrayList<>();
        dependencyNodes.forEach(d -> {
            Node node = buildNode(d.getDependency());
            nodes.add(node);
            List<DependencyNode> children = d.getChildren();
            if (!children.isEmpty()) {
                node.setChildren(getChildren(children));
            } else {
                node.setChildren(new ArrayList<>());
            }
        });
        return nodes;
    }

    private Node buildNode(Dependency dependency) {
        Artifact artifact = dependency.getArtifact();
        return new Node(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                artifact.getExtension(), dependency.getScope());
    }
}
