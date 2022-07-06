package com.example.parse.service;

import lombok.SneakyThrows;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ParseServiceImplTest {

    @InjectMocks
    private ParseServiceImpl parseService;
    @Mock
    private MavenXpp3Reader reader;
    @Mock
    private ModelBuildingRequest modelBuildingRequest;
    @Mock
    private DefaultModelBuilderFactory modelBuilderFactory;

    @SneakyThrows
    @DisplayName("Test case return correct model with parent.")
    @Test
    void shouldReturnCorrectModelWithParent() {
        Model expectedModel = createTestModel();
        Parent expectedParent = createTestParent();
        Model modelWithParent = new Model();
        modelWithParent.setParent(expectedParent);
        DefaultModelBuilder modelBuilder = Mockito.mock(DefaultModelBuilder.class);
        Mockito.when(modelBuilderFactory.newInstance()).thenReturn(modelBuilder);
        ModelBuildingResult modelBuildingResult = Mockito.mock(ModelBuildingResult.class);
        Mockito.when(modelBuilder.build(any())).thenReturn(modelBuildingResult);
        Mockito.when(modelBuildingResult.getEffectiveModel()).thenReturn(expectedModel);
        Mockito.when(reader.read(any(FileReader.class))).thenReturn(modelWithParent);
        expectedModel.setParent(expectedParent);
        String path = "src/test/resources/files/one.xml";
        File file = Paths.get("", path).toAbsolutePath().toFile();
        Optional<Model> actualModel = parseService.getModelFromFile(file);
        if (actualModel.isPresent()) {
            Assertions.assertEquals(expectedModel, actualModel.get());
        } else {
            Assertions.fail();
        }
    }

    @SneakyThrows
    @DisplayName("Test case return correct model.")
    @Test
    void shouldReturnCorrectModel() {
        Model expectedModel = createTestModel();
        DefaultModelBuilder modelBuilder = Mockito.mock(DefaultModelBuilder.class);
        Mockito.when(modelBuilderFactory.newInstance()).thenReturn(modelBuilder);
        ModelBuildingResult modelBuildingResult = Mockito.mock(ModelBuildingResult.class);
        Mockito.when(modelBuilder.build(any())).thenReturn(modelBuildingResult);
        Mockito.when(modelBuildingResult.getEffectiveModel()).thenReturn(expectedModel);
        Mockito.when(reader.read(any(FileReader.class))).thenThrow(XmlPullParserException.class);
        String path = "src/test/resources/files/one.xml";
        File file = Paths.get("", path).toAbsolutePath().toFile();
        Optional<Model> actualModel = parseService.getModelFromFile(file);
        if (actualModel.isPresent()) {
            Assertions.assertEquals(expectedModel, actualModel.get());
        } else {
            Assertions.fail();
        }
    }

    @SneakyThrows
    @DisplayName("Test case return empty optional.")
    @Test
    void shouldReturnEmptyOptional(){
        File incorrectFile = new File("incorrect");
        DefaultModelBuilder modelBuilder = Mockito.mock(DefaultModelBuilder.class);
        Mockito.when(modelBuilderFactory.newInstance()).thenReturn(modelBuilder);
        Mockito.when(modelBuilder.build(any())).thenThrow(ModelBuildingException.class);
        Optional<Model> modelFromNullFile = parseService.getModelFromFile(null);
        Optional<Model> modelFromIncorrectFile = parseService.getModelFromFile(incorrectFile);
        Assertions.assertTrue(modelFromNullFile.isEmpty());
        Assertions.assertTrue(modelFromIncorrectFile.isEmpty());
    }

    private Model createTestModel() {
        Model model = new Model();
        String path = "src/test/resources/files/one.xml";
        File file = Paths.get("", path).toAbsolutePath().toFile();
        model.setModelVersion("4.0.0");
        model.setGroupId("com.example");
        model.setArtifactId("Parse");
        model.setVersion("0.0.1-SNAPSHOT");
        model.setPackaging("jar");
        model.setName("Parse");
        model.setPomFile(file);
        return model;
    }

    private Parent createTestParent() {
        Parent parent = new Parent();
        parent.setGroupId("org.springframework.boot");
        parent.setArtifactId("spring-boot-starter-parent");
        parent.setVersion("2.7.0");
        return parent;
    }
}