package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DependencyNode {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String type;
    private final String scope;
}
