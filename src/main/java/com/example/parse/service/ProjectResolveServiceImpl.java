package com.example.parse.service;

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

@Service
@AllArgsConstructor
public class ProjectResolveServiceImpl implements ProjectResolveService {
    private final NodeService nodeService;

    @Override
    public void resolveProject(Project project, ProjectInstruction instruction) {
        List<Node> nodes = new ArrayList<>(project.getNodes());
        removeNodes(nodes, instruction.getArtifactIdsForRemoving());
        replaceNodes(nodes, instruction.getNodesFroReplacing());
        addNodes(nodes, instruction.getNodesForAdding());
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setNodes(nodes);
        newProject.setParentNode(project.getParentNode());
        project.setNewProject(newProject);
    }

    private void addNodes(List<Node> nodes, List<DependencyNode> dependencyNodes) {
        for(DependencyNode dependencyNode : dependencyNodes){
            nodes.add(getNode(dependencyNode));
        }
    }

    private void replaceNodes(List<Node> nodes, List<DependencyNode> dependencyNodes) {
        for(DependencyNode dependencyNode : dependencyNodes){
            for(int i = 0; i < nodes.size(); i++){
                Node node = nodes.get(i);
                if(dependencyNode.getArtifactId().equals(node.getArtifactId())){
                    nodes.set(i, getNode(dependencyNode));
                }
            }
        }
    }

    private void removeNodes(List<Node> nodes, List<String> artifactIds) {
        for(String artifactId : artifactIds){
            nodes.removeIf(node -> artifactId.equals(node.getArtifactId()));
        }
    }

    private Node getNode(DependencyNode dependencyNode){
        Artifact artifact = new DefaultArtifact(dependencyNode.getGroupId(), dependencyNode.getArtifactId(),
                dependencyNode.getType(), dependencyNode.getVersion());
        return nodeService.getNodeFromDependency(new Dependency(artifact, dependencyNode.getScope()));
    }
}
