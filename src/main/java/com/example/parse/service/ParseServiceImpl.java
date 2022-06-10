package com.example.parse.service;

import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.resolution.ModelResolver;
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
    private final ModelResolver modelResolver;

    public Optional<Model> getModelFromFile(File file) {
        ModelBuildingRequest modelBuildingRequest = new DefaultModelBuildingRequest()
                .setPomFile(file).setProcessPlugins(false).setSystemProperties(System.getProperties())
                .setModelResolver(modelResolver);
        ModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
        ModelBuildingResult modelBuildingResult;
        Model model;
        try {
            modelBuildingResult = modelBuilder.build(modelBuildingRequest);
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

}
