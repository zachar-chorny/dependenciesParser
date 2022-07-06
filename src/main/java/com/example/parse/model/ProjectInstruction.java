package com.example.parse.model;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProjectInstruction {

    @NotNull(message = "Name can't be null")
    private String name;
    private List<DependencyNode> nodesForAdding;
    private List<String> artifactIdsForRemoving;
    private List<DependencyNode> nodesFroReplacing;
}
