package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class GroupId {
    private final String name;
    private List<ArtifactId> artifacts;
}
