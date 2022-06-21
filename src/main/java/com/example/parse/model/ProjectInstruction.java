package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ProjectInstruction {

    private final String name;
    private final List<DependencyNode> nodesForAdding;
    private final List<String> artifactIdsForRemoving;
    private final List<DependencyNode> nodesFroReplacing;
}
