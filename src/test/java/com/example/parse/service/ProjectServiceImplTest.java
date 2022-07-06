package com.example.parse.service;

import com.example.parse.model.Node;
import com.example.parse.model.Project;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
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
class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;
    @Mock
    private ParseService parseService;
    @Mock
    private ArtifactResolveService artifactResolver;
    @Mock
    private NodeService nodeService;

    @DisplayName("Test case return correct project.")
    @Test
    void shouldReturnCorrectProject() {
        setup();
        Model expectedModel = createTestModel();
        Optional<Project> project = projectService.createProjectFromModel(expectedModel);
        if(project.isPresent()){
            Assertions.assertTrue(isCorrect(expectedModel, project.get()));
        }else {
            Assertions.fail();
        }
    }

    void setup(){
        Model model = new Model();
        model.setModelVersion("4.0.0");
        model.setGroupId("org.springframework.boot");
        model.setArtifactId("spring-boot-starter-parent");
        model.setVersion("2.7.0");
        model.setPackaging("pom");
        model.setName("spring-boot-starter-parent");
        Artifact artifact = new DefaultArtifact(null, null, null, null);
        Mockito.lenient().when(parseService.getModelFromFile(any())).thenReturn(Optional.of(model));
        Mockito.lenient().when(artifactResolver.resolve(any())).thenReturn(Optional.of(artifact));
        Mockito.lenient().when(nodeService.getNodeFromDependency(any())).thenReturn(Optional.of(new Node()));
    }

    private Model createTestModel() {
        Model model = new Model();
        model.setModelVersion("4.0.0");
        model.setGroupId("com.example");
        model.setArtifactId("Parse");
        model.setVersion("0.0.1-SNAPSHOT");
        model.setPackaging("jar");
        model.setName("Parse");
        model.setParent(new Parent());
        model.setDependencies(new ArrayList<>(List.of(new Dependency(), new Dependency(), new Dependency())));
        return model;
    }

    @DisplayName("Test case return empty optional.")
    @Test
    void shouldReturnEmptyOptional(){
        Optional<Project> project = projectService.createProjectFromModel(null);
        Assertions.assertTrue(project.isEmpty());
    }

    private boolean isCorrect(Model model, Project project) {
        boolean isCorrect = true;
        String modelName = model.getName();
        String projectModel = project.getName();
        int modelDependencyListSize = model.getDependencies().size();
        int projectNodeListSize = project.getNodes().size();
        if (!modelName.equals(projectModel) || modelDependencyListSize != projectNodeListSize
                || !(model.getParent() != null && project.getParentNode() != null)) {
            isCorrect = false;
        }
        return isCorrect;
    }

}