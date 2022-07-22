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
import java.util.List;
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

    @DisplayName("Test case return list without changes.")
    @Test
    void shouldReturnListWithoutChanges(){
        List<Node> nodes = new ArrayList<>(createTestNodes());
        List<Node> resultAfterAdding = instructionService.addNodes(nodes, null);
        List<Node> resultAfterReplacing = instructionService.replaceNodes(nodes, null);
        List<Node> resultAfterRemoving = instructionService.removeNodes(nodes, null);
        Assertions.assertEquals(nodes, resultAfterRemoving);
        Assertions.assertEquals(nodes, resultAfterAdding);
        Assertions.assertEquals(nodes, resultAfterReplacing);

        ProjectInstruction emptyInstruction = new ProjectInstruction();
        emptyInstruction.setArtifactIdsForRemoving(null);
        emptyInstruction.setNodesFroReplacing(null);
        emptyInstruction.setNodesForAdding(null);
        resultAfterAdding = instructionService.addNodes(nodes, emptyInstruction);
        resultAfterReplacing = instructionService.replaceNodes(nodes, emptyInstruction);
        resultAfterRemoving = instructionService.removeNodes(nodes, emptyInstruction);
        Assertions.assertEquals(nodes, resultAfterRemoving);
        Assertions.assertEquals(nodes, resultAfterAdding);
        Assertions.assertEquals(nodes, resultAfterReplacing);
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