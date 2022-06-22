package com.example.parse.model;

import lombok.Data;
import javax.validation.constraints.NotNull;

import java.util.List;

@Data
public class ProjectInstruction {

    @NotNull(message = "Name can't be null")
    private final String name;
    private final List<DependencyNode> nodesForAdding;
    private final List<String> artifactIdsForRemoving;
    private final List<DependencyNode> nodesFroReplacing;
}
