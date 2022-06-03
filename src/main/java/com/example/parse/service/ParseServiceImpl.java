package com.example.parse.service;

import com.example.parse.model.dto.ModelDto;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingResult;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParseServiceImpl implements ParseService{

    public ModelDto getArtifactsFromFile(File file){
        DefaultModelBuildingRequest modelBuildingRequest = new DefaultModelBuildingRequest()
                .setPomFile(file);
        ModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
        ModelBuildingResult modelBuildingResult;
        Model model;
        try {
            modelBuildingResult = modelBuilder.build(modelBuildingRequest);
            model = modelBuildingResult.getEffectiveModel();
            List<Artifact> artifacts = getArtifactsFromModel(model);
            return new ModelDto(model.getBuild().getFinalName(), artifacts);
        } catch (ModelBuildingException e) {
            throw new RuntimeException("File reading failed");
        }
    }



    private List<Artifact> getArtifactsFromModel(Model model){
        List<Artifact> artifacts = new ArrayList<>();
        model.getDependencies().forEach(d -> {
            artifacts.add(new DefaultArtifact(d.getGroupId(), d.getArtifactId(), d.getType(),
                    d.getVersion()));
        });
        return artifacts;
    }

}
