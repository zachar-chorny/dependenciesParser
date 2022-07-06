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
import java.util.List;
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
        Optional<Project> actualProject = projectResolveService.createNewProject(
                expectedProject, new ProjectInstruction());
        if (actualProject.isPresent()) {
            Assertions.assertEquals(expectedProject, actualProject.get());
        } else {
            Assertions.fail();
        }
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

    @DisplayName("Test case return empty optional.")
    @Test
    void shouldReturnEmptyOptional() {
        Optional<Project> project = projectResolveService.createNewProject(null, new ProjectInstruction());
        Assertions.assertTrue(project.isEmpty());
    }
}