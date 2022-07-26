package com.example.parse.service;

import com.example.parse.model.DependencyNode;
import com.example.parse.model.Node;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class InstructionServiceImplTest {
    @InjectMocks
    private InstructionServiceImpl instructionService;
    @Mock
    private NodeService nodeService;

    @DisplayName("Test case return correct list with new nodes.")
    @Test
    void shouldReturnCorrectListWithNewNodes() {
        List<Node> nodes = new ArrayList<>(createTestNodes());
        ProjectInstruction instruction = createTestInstruction();
        Node nodeForAdd = new Node("add", "add", "add",
                "add", "add");
        Mockito.when(nodeService.getNodeFromDependency(any())).thenReturn(nodeForAdd);
        List<Node> result = instructionService.addNodes(nodes, instruction);
        Assertions.assertTrue(result.contains(nodeForAdd));
    }

    @DisplayName("Test case return correct list with replaced nodes.")
    @Test
    void shouldReturnCorrectListWithReplacedNodes() {
        List<Node> nodes = new ArrayList<>(createTestNodes());
        ProjectInstruction instruction = createTestInstruction();
        Node nodeForReplace = new Node("default", "default", "replaced",
                "replaced", "replaced");
        Node oldNode = new Node("default", "default", "default",
                "default", "default");
        Mockito.when(nodeService.getNodeFromDependency(any())).thenReturn(nodeForReplace);
        List<Node> result = instructionService.replaceNodes(nodes, instruction);
        Assertions.assertTrue(result.contains(nodeForReplace));
        Assertions.assertFalse(result.contains(oldNode));
    }

    @DisplayName("Test case return correct list without chosen nodes.")
    @Test
    void shouldReturnCorrectListWithoutChosenNodes() {
        List<Node> nodes = new ArrayList<>(createTestNodes());
        ProjectInstruction instruction = createTestInstruction();
        Node nodeForRemove = nodes.get(1);
        List<Node> result = instructionService.removeNodes(nodes, instruction);
        Assertions.assertFalse(result.contains(nodeForRemove));
    }

    @DisplayName("Test case return correct map with removed nodes.")
    @Test
    void shouldReturnCorrectMapWithRemovedNodes() {
        Project project = createTestProject();
        ProjectInstruction instruction = createTestInstructionForChange();
        Map<String, List<String>> expectedMap = Map.of("1", List.of("Parse"),
                "2", List.of("Parse"), "3", List.of("Parse"));
        Map<String, List<String>> actualMap = instructionService.removeNodes(project, instruction,
                new HashMap<>());
        Assertions.assertEquals(expectedMap, actualMap);
    }

    @DisplayName("Test case return correct map with replaced nodes.")
    @Test
    void shouldReturnCorrectMapWithReplacedNodes() {
        Project project = createTestProject();
        ProjectInstruction instruction = createTestInstructionForChange();
        Map<String, List<String>> expectedMap = Map.of("1", List.of("Parse"),
                "2", List.of("Parse"), "3", List.of("Parse"));
        Map<String, List<String>> actualMap = instructionService.replaceNodes(project, instruction,
                new HashMap<>());
        Assertions.assertEquals(expectedMap, actualMap);
    }

    private ProjectInstruction createTestInstructionForChange() {
        ProjectInstruction projectInstruction = new ProjectInstruction();
        projectInstruction.setNodesFroReplacing(List.of(
                new DependencyNode("default", "1", "default", "default", "default"),
                new DependencyNode("default", "2", "default", "default", "default"),
                new DependencyNode("default", "3", "default", "default", "default")));
        projectInstruction.setArtifactIdsForRemoving(List.of("1", "2", "3"));
        return projectInstruction;
    }

    private Project createTestProject() {
        Project project = new Project();
        project.setName("Parse");
        List<Node> nodes = List.of(
                new Node("default", "1", "default", "default", "default"),
                new Node("default", "2", "default", "default", "default"),
                new Node("default", "3", "default", "default", "default"));
        project.setNodes(nodes);
        return project;
    }


    private ProjectInstruction createTestInstruction(){
        ProjectInstruction projectInstruction = new ProjectInstruction();
        DependencyNode nodeForAdd = new DependencyNode("add", "add", "add",
                "add", "add");
        DependencyNode nodeForReplace = new DependencyNode("default", "default", "replaced",
                "replaced", "replaced");
        String idForRemove = "remove";
        projectInstruction.setNodesFroReplacing(List.of(nodeForReplace));
        projectInstruction.setNodesForAdding(List.of(nodeForAdd));
        projectInstruction.setArtifactIdsForRemoving(List.of(idForRemove));
        return projectInstruction;
    }

    private List<Node> createTestNodes(){
        Node defaultNode = new Node("default", "default", "default",
                "default", "default");
        Node nodeForRemove = new Node("remove", "remove","remove",
                "remove","remove");
        return List.of(defaultNode, nodeForRemove);
    }
}