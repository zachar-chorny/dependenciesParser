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
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectResolveServiceImpl implements ProjectResolveService {
    private final InstructionService instructionService;

    @Override
    public Project createNewProject(Project project, ProjectInstruction instruction) {
        List<Node> nodes = new ArrayList<>(project.getNodes());
        nodes = instructionService.removeNodes(nodes, instruction);
        nodes = instructionService.replaceNodes(nodes, instruction);
        nodes = instructionService.addNodes(nodes, instruction);
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setNodes(nodes);
        newProject.setParentNode(project.getParentNode());
        return newProject;
    }

    @Override
    public Map<String, List<String>> createNewProject(Project project, ProjectInstruction instruction,
                                                      Map<String, List<String>> changes) {
        changes = instructionService.removeNodes(project, instruction, changes);
        changes = instructionService.replaceNodes(project, instruction, changes);
        return changes;
    }
}
