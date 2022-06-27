package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DependencyNode {

    private String groupId;
    private String artifactId;
    private String version;
    private String type;
    private String scope;
}
