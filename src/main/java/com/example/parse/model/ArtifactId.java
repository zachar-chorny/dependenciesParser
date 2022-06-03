package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ArtifactId {
    private final String name;
    private Version version;
}
