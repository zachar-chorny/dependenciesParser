package com.example.parse.service;

import com.example.parse.model.Node;
import com.example.parse.model.ParentNode;
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

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProjectResolveServiceImplTest {

    @InjectMocks
    private ProjectResolveServiceImpl projectResolveService;
    @Mock
    private InstructionServiceImpl instructionService;

    @DisplayName("Test case return correct project.")
    @Test
    void shouldReturnCorrectProject() {
        Project expectedProject = createTestProject();
        List<Node> nodes = expectedProject.getNodes();
        Mockito.when(instructionService.addNodes(any(), any()))
                .thenReturn(nodes);
        Mockito.when(instructionService.removeNodes(any(), any()))
                .thenReturn(nodes);
        Mockito.when(instructionService.replaceNodes(any(), any()))
                .thenReturn(nodes);
        Project actualProject = projectResolveService.createNewProject(
                expectedProject, new ProjectInstruction());
        Assertions.assertEquals(expectedProject, actualProject);
    }

    @DisplayName("Test case return correct project.")
    @Test
    void shouldReturnCorrectMap() {
        Map<String, List<String>> expectedMap = Map.of("1", List.of("Parse"),
                "2", List.of("Parse"), "3", List.of("Parse"));
        Mockito.when(instructionService.removeNodes(any(), any(), any()))
                .thenReturn(expectedMap);
        Mockito.when(instructionService.replaceNodes(any(), any(), any()))
                .thenReturn(expectedMap);
        Map<String, List<String>> actualMap = projectResolveService.createNewProject(new Project(),
                new ProjectInstruction(), new HashMap<>());
        Assertions.assertEquals(expectedMap, actualMap);
    }

    private Project createTestProject() {
        Project project = new Project();
        Node defaultNode = new Node("default", "default", "default",
                "default", "default");
        project.setName("Parse");
        project.setNodes(new ArrayList<>(List.of(defaultNode)));
        project.setParentNode(new ParentNode());
        return project;
    }
}