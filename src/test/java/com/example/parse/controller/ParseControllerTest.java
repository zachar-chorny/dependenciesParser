package com.example.parse.controller;

import com.example.parse.facade.ParseFacade;
import com.example.parse.facade.impl.DefaultParseFacade;
import com.example.parse.model.Node;
import com.example.parse.model.ParentNode;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import com.example.parse.model.Setting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ParseControllerTest {

    @Mock
    private ParseFacade parseFacade;
    @InjectMocks
    private ParseController parseController;

    @DisplayName("Test case show created projects.")
    @Test
    void shouldShowCreatedProjects() {
        List<Project> expected = createTestProjects();
        Setting setting = new Setting();
        setting.setPath("src/test/resources/files/one.xml");
        File file = new File(setting.getPath());
        Mockito.when(parseFacade.createProjectsFromFile(eq(file))).thenReturn(expected);
        List<Project> actual = parseController.createProjects(setting);
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Test case show created projects with new project.")
    @Test
    void shouldShowCreatedProjectsWithNewProject() {
        List<Project> expected = createTestProjectsWithNewProjects();
        Setting setting = new Setting();
        setting.setPath("src/test/resources/files/two.xml");
        setting.setInstructions(List.of(new ProjectInstruction()));
        File file = new File(setting.getPath());
        Mockito.when(parseFacade.createProjectsFromFile(eq(file),eq(setting.getInstructions())))
                .thenReturn(expected);
        List<Project> actual = parseController.createProjects(setting);
        Assertions.assertEquals(expected, actual);
    }

    private List<Project> createTestProjects() {
        Project firstProject = new Project();
        firstProject.setName("First");
        firstProject.setParentNode(new ParentNode());
        firstProject.setNodes(List.of(new Node(), new Node()));
        Project secondProject = new Project();
        secondProject.setName("Second");
        secondProject.setParentNode(new ParentNode());
        secondProject.setNodes(List.of(new Node(), new Node()));
        return List.of(firstProject, secondProject);
    }

    private List<Project> createTestProjectsWithNewProjects() {
        Project firstProject = new Project();
        firstProject.setName("First");
        firstProject.setParentNode(new ParentNode());
        firstProject.setNodes(List.of(new Node(), new Node()));
        firstProject.setNewProject(firstProject);
        Project secondProject = new Project();
        secondProject.setName("Second");
        secondProject.setParentNode(new ParentNode());
        secondProject.setNodes(List.of(new Node(), new Node()));
        firstProject.setNewProject(secondProject);
        return List.of(firstProject, secondProject);
    }

}