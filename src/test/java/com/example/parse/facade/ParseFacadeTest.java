package com.example.parse.facade;

import com.example.parse.facade.impl.DefaultParseFacade;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import com.example.parse.service.ParseService;
import com.example.parse.service.ProjectResolveService;
import com.example.parse.service.ProjectService;
import com.example.parse.service.impl.ProjectServiceImpl;
import org.apache.maven.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ParseFacadeTest {

    @InjectMocks
    private ParseFacade parseFacade;
    @Mock
    private ParseService parseService;
    @Mock
    private ProjectService projectService;
    @Mock
    private ProjectResolveService projectResolveService;

    @BeforeEach
    public void setup() {
        Project project = new Project();
        project.setName("Parse");
        Mockito.lenient().when(parseService.getModelFromFile(any())).thenReturn(Optional.of(new Model()));
        Mockito.lenient().when(projectService.createProjectFromModel(any())).thenReturn(project);
        Mockito.lenient().when(projectResolveService.createNewProject(any(), any()))
                .thenReturn(new Project());
    }

    @DisplayName("Test case return list with few projects.")
    @Test
    void shouldReturnListWithFewProjects() {
        String path = "src/test/resources/files";
        File file = Paths.get("", path).toAbsolutePath().toFile();
        List<Project> projects = parseFacade.createProjectsFromFile(file);
        Assertions.assertEquals(3, projects.size());
    }

    @DisplayName("Test case return project with new project.")
    @Test
    void shouldReturnProjectWithNewProject() {
        String path = "src/test/resources/files/one/pom.xml";
        File file = Paths.get("", path).toAbsolutePath().toFile();
        ProjectInstruction instruction = new ProjectInstruction();
        instruction.setName("Parse");
        List<Project> projects = parseFacade.createProjectsFromFile(file, List.of(instruction));
        Assertions.assertNotNull(projects.get(0).getNewProject());
    }
}