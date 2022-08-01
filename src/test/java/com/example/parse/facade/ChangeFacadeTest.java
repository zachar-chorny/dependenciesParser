package com.example.parse.facade;

import com.example.parse.facade.impl.DefaultChangeFacade;
import com.example.parse.facade.impl.DefaultParseFacade;
import com.example.parse.model.DependencyNode;
import com.example.parse.model.Node;
import com.example.parse.model.ParentNode;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import com.example.parse.service.ProjectResolveService;
import com.example.parse.service.impl.ProjectResolveServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ChangeFacadeTest {

    @Mock
    private ParseFacade parseFacade;
    @Mock
    private ProjectResolveService projectResolveService;
    @InjectMocks
    private DefaultChangeFacade changeFacade;

    @DisplayName("Test case return correct map.")
    @Test
    void shouldReturnCorrectMap() {
        ProjectInstruction projectInstruction = new ProjectInstruction();
        projectInstruction.setName("parent");
        projectInstruction.setArtifactIdsForRemoving(List.of("remove"));
        projectInstruction.setNodesFroReplacing(List.of(
                new DependencyNode("default", "replace", "default", "default", "default")));
        List<ProjectInstruction> projectInstructions = List.of(projectInstruction);
        List<Project> projects = createTestProjects();
        Map<String, List<String>> expectedMap = createTestMap();
        Mockito.when(parseFacade.createProjectsFromFile(any(), any())).thenReturn(projects);
        Mockito.when(projectResolveService.createNewProject(any(), any(), any())).thenReturn(expectedMap);
        Map<String, List<String>> actualMap = changeFacade.getFutureChanges(
                new File("path"), projectInstructions);
        Assertions.assertEquals(expectedMap, actualMap);
    }

    private Map<String, List<String>> createTestMap() {
        Map<String, List<String>> testMap = new HashMap<>();
        testMap.put("remove", List.of("1", "2.1"));
        testMap.put("replace", List.of("1.1", "2"));
        return testMap;
    }

    private List<Project> createTestProjects() {
        Node nodeForRemove = new Node("default", "remove", "default", "default", "default");
        Node nodeForReplace = new Node("default", "replace", "default", "default", "default");
        List<Project> projects = new ArrayList<>();
        ParentNode parentNode = new ParentNode();
        parentNode.setUp(new Node("default", "parent", "default", "default", "default"));
        projects.add(new Project("1", List.of(nodeForRemove), parentNode));
        ParentNode firstParentNode = new ParentNode();
        firstParentNode.setUp(new Node("default", "1", "default",
                "default", "default"));
        projects.add(new Project("1.1", List.of(nodeForReplace), firstParentNode));
        projects.add(new Project("2", List.of(nodeForReplace), parentNode));
        ParentNode secondParentNode = new ParentNode();
        secondParentNode.setUp(new Node("default", "2", "default",
                "default", "default"));
        projects.add(new Project("2.1", List.of(nodeForRemove), secondParentNode));
        projects.add(new Project("3", new ArrayList<>(), parentNode));
        ParentNode thirdParentNode = new ParentNode();
        thirdParentNode.setUp(new Node("default", "3", "default",
                "default", "default"));
        projects.add(new Project("3.1", new ArrayList<>(), thirdParentNode));
        return projects;
    }
}