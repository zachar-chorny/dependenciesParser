package com.example.parse.service;

import com.example.parse.model.CallableNodeTask;
import com.example.parse.model.DependencyNode;
import com.example.parse.model.Node;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import lombok.AllArgsConstructor;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@AllArgsConstructor
public class InstructionServiceImpl implements InstructionService {

    private final NodeResolveService nodeResolveService;

    @Override
    public List<Node> addNodes(List<Node> nodes, ProjectInstruction instruction) {
        List<Node> changedNodes = new ArrayList<>(nodes);
        List<DependencyNode> dependencyNodes = instruction.getNodesForAdding();
        if (dependencyNodes != null) {
            changedNodes.addAll(getNodes(dependencyNodes));
        }
        return changedNodes;
    }

    @Override
    public List<Node> replaceNodes(List<Node> nodes, ProjectInstruction instruction) {
        List<Node> changedNodes = new ArrayList<>(nodes);
        List<DependencyNode> dependencyNodes = instruction.getNodesFroReplacing();
        if (dependencyNodes != null) {
            List<Node> nodesForReplace = getNodes(dependencyNodes);
            for (Node nodeForReplace : nodesForReplace) {
                for (int i = 0; i < changedNodes.size(); i++) {
                    Node node = changedNodes.get(i);
                    if (nodeForReplace.getGroupId().equals(node.getGroupId())
                            && nodeForReplace.getArtifactId().equals(node.getArtifactId())) {
                        changedNodes.set(i, nodeForReplace);
                    }
                }
            }
        }
        return changedNodes;
    }

    @Override
    public Map<String, List<String>> replaceNodes(Project project, ProjectInstruction instruction,
                                                  Map<String, List<String>> changes) {
        List<Node> changedNodes = new ArrayList<>(project.getNodes());
        List<DependencyNode> dependencyNodes = instruction.getNodesFroReplacing();
        if (dependencyNodes != null) {
            for (DependencyNode dependencyNode : dependencyNodes) {
                for (Node node : changedNodes) {
                    if (dependencyNode.getGroupId().equals(node.getGroupId())
                            && dependencyNode.getArtifactId().equals(node.getArtifactId())) {
                        String artifactId = dependencyNode.getArtifactId();
                        if (changes.containsKey(artifactId)) {
                            changes.get(artifactId).add(project.getName());
                        } else {
                            changes.put(artifactId, new ArrayList<>(List.of(project.getName())));
                        }
                    }
                }
            }
        }
        return changes;
    }

    @Override
    public List<Node> removeNodes(List<Node> nodes, ProjectInstruction instruction) {
        List<Node> changedNodes = new ArrayList<>(nodes);
        List<String> artifactIds = instruction.getArtifactIdsForRemoving();
        if (artifactIds != null) {
            for (String artifactId : artifactIds) {
                changedNodes.removeIf(node -> artifactId.equals(node.getArtifactId()));
            }
        }
        return changedNodes;
    }

    @Override
    public Map<String, List<String>> removeNodes(Project project, ProjectInstruction instruction,
                                                 Map<String, List<String>> changes) {
        List<Node> changedNodes = new ArrayList<>(project.getNodes());
        List<String> artifactIds = instruction.getArtifactIdsForRemoving();
        if (artifactIds != null) {
            for (String artifactId : artifactIds) {
                for (Node node : changedNodes) {
                    if (node.getArtifactId().equals(artifactId)) {
                        if (changes.containsKey(artifactId)) {
                            changes.get(artifactId).add(project.getName());
                        } else {
                            changes.put(artifactId, new ArrayList<>(List.of(project.getName())));
                        }
                    }
                }
            }
        }
        return changes;
    }

    private List<Node> getNodes(List<DependencyNode> dependencyNodes) {
        List<Dependency> dependencies = new ArrayList<>();
        for (DependencyNode dependencyNode : dependencyNodes){
            Artifact artifact = new DefaultArtifact(dependencyNode.getGroupId(), dependencyNode.getArtifactId(),
                    dependencyNode.getType(), dependencyNode.getVersion());
            dependencies.add(new Dependency(artifact, dependencyNode.getScope()));
        }
        return nodeResolveService.getNodes(dependencies, true);
    }
}
