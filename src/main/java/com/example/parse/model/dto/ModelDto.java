package com.example.parse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.aether.artifact.Artifact;

import java.util.List;

@AllArgsConstructor
@Data
public class ModelDto {
    private final String projectName;
    private List<Artifact> artifacts;
}
