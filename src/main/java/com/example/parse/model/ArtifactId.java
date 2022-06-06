package com.example.parse.model;

import lombok.Data;

@Data
public class ArtifactId {
    private final String name;
    private DependencyTree tree;
    private final Version version;

    public ArtifactId(String name, Version version){
        this.name = name;
        this.version = version;
    }
}
