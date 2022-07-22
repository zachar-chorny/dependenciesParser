package com.example.parse.service;

import com.example.parse.model.Node;
import com.example.parse.model.RepositoriesDto;
import lombok.SneakyThrows;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.graph.DefaultDependencyNode;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class NodeServiceImplTest {

    @InjectMocks
    private NodeServiceImpl nodeService;
    @Mock
    private RepositorySystem repositorySystem;
    @Mock
    private RepositoriesDto repositories;
    private static final String REMOTE_REPOSITORY_URL = "https://repo1.maven.org/maven2/";

    @BeforeEach
    public void setup() {
        RemoteRepository remoteRepository = new RemoteRepository.Builder(
                "central", "default", REMOTE_REPOSITORY_URL).build();
        Mockito.lenient().when(repositories.getRepositories()).thenReturn(List.of(remoteRepository));
    }

    @SneakyThrows
    @DisplayName("Test case return correct node.")
    @Test
    void shouldReturnCorrectNode() {
        CollectResult collectResult = createTestCollectResult();
        Artifact finalArtifact = new DefaultArtifact("final", "final", "final", "final");
        Dependency finalDependency = new Dependency(finalArtifact, "final");
        CollectRequest collectRequest = new CollectRequest().setRoot(finalDependency);
        Mockito.when(repositorySystem.collectDependencies(any(), any())).thenReturn(collectResult);
        Mockito.lenient().when(repositorySystem.collectDependencies(any(), eq(collectRequest)))
                .thenReturn(null);
        Node node = nodeService.getNodeFromDependency(collectResult.getRoot().getDependency());
        Assertions.assertTrue(isCorrect(collectResult.getRoot(), node));
    }

    private boolean isCorrect(DependencyNode root, Node node) {
        if (isSame(root, node)) {
            List<DependencyNode> dependencyNodes = root.getChildren();
            List<Node> nodes = node.getChildren();
            if (dependencyNodes != null && nodes != null) {
                if (dependencyNodes.size() == nodes.size()) {
                    for (int i = 0; i < nodes.size(); i++) {
                        isCorrect(dependencyNodes.get(i), nodes.get(i));
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean isSame(DependencyNode root, Node node) {
        String scope = root.getDependency().getScope();
        Artifact artifact = root.getArtifact();
        return artifact.getArtifactId().equals(node.getArtifactId()) &&
                artifact.getGroupId().equals(node.getGroupId()) &&
                artifact.getVersion().equals(node.getVersion()) &&
                artifact.getExtension().equals(node.getType()) &&
                scope.equals(node.getScope());
    }

    private CollectResult createTestCollectResult() {
        CollectResult collectResult = new CollectResult(new CollectRequest());
        Artifact artifact = new DefaultArtifact("test", "test", "test", "test");
        Dependency dependency = new Dependency(artifact, "test");
        DependencyNode dependencyNode = new DefaultDependencyNode(dependency);
        Artifact finalArtifact = new DefaultArtifact("final", "final", "final", "final");
        Dependency finalDependency = new Dependency(finalArtifact, "final");
        DependencyNode finalDependencyNode = new DefaultDependencyNode(finalDependency);
        dependencyNode.setChildren(List.of(finalDependencyNode, finalDependencyNode, finalDependencyNode));
        return collectResult.setRoot(dependencyNode);
    }
}