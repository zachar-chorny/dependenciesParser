package com.example.parse.service;

import com.example.parse.model.DependencyNode;
import com.example.parse.model.Node;
import com.example.parse.model.ProjectInstruction;
import lombok.AllArgsConstructor;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InstructionServiceImpl implements InstructionService {

    private final NodeService nodeService;

    @Override
    public List<Node> addNodes(List<Node> nodes, ProjectInstruction instruction) {
        if (instruction != null) {
            List<DependencyNode> dependencyNodes = instruction.getNodesForAdding();
            if (dependencyNodes != null) {
                for (DependencyNode dependencyNode : dependencyNodes) {
                    getNode(dependencyNode).ifPresent(nodes::add);
                }
            }
        }
        return nodes;
    }

    @Override
    public List<Node> replaceNodes(List<Node> nodes, ProjectInstruction instruction) {
        if (instruction != null) {
            List<DependencyNode> dependencyNodes = instruction.getNodesFroReplacing();
            if (dependencyNodes != null) {
                for (DependencyNode dependencyNode : dependencyNodes) {
                    for (int i = 0; i < nodes.size(); i++) {
                        Node node = nodes.get(i);
                        if (dependencyNode.getGroupId().equals(node.getGroupId()) &&
                                dependencyNode.getArtifactId().equals(node.getArtifactId())) {
                            Optional<Node> optionalNode = getNode(dependencyNode);
                            if (optionalNode.isPresent()) {
                                nodes.set(i, optionalNode.get());
                            }
                        }
                    }
                }
            }
        }
        return nodes;
    }

    @Override
    public List<Node> removeNodes(List<Node> nodes, ProjectInstruction instruction) {
        if(instruction != null) {
            List<String> artifactIds = instruction.getArtifactIdsForRemoving();
            if (artifactIds != null) {
                for (String artifactId : artifactIds) {
                    nodes.removeIf(node -> artifactId.equals(node.getArtifactId()));
                }
            }
        }
        return nodes;
    }

    private Optional<Node> getNode(DependencyNode dependencyNode) {
        Artifact artifact = new DefaultArtifact(dependencyNode.getGroupId(), dependencyNode.getArtifactId(), dependencyNode.getType(), dependencyNode.getVersion());
        return nodeService.getNodeFromDependency(new Dependency(artifact, dependencyNode.getScope()));
    }
}
