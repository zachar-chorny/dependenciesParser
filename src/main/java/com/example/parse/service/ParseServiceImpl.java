package com.example.parse.service;

import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParseServiceImpl implements ParseService {
    private final MavenXpp3Reader reader;
    private final ModelBuildingRequest modelBuildingRequest;
    private final DefaultModelBuilderFactory modelBuilderFactory;

    public Optional<Model> getModelFromFile(File file) {
        if(file != null) {
            ModelBuilder modelBuilder = modelBuilderFactory.newInstance();
            ModelBuildingResult modelBuildingResult;
            Model model;
            try {
                modelBuildingResult = modelBuilder.build(modelBuildingRequest.setPomFile(file));
                model = modelBuildingResult.getEffectiveModel();
                try {
                    model.setParent(reader.read(new FileReader(file)).getParent());
                } catch (XmlPullParserException | IOException ignored) {
                }
                return Optional.of(model);
            } catch (ModelBuildingException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

}
