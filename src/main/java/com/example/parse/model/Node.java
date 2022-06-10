package com.example.parse.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Node {
    private String groupId;
    private String artifactId;
    private String version;
    private String type;
    private String scope;
    private List<Node> children;

    public Node() {
    }

    public Node(String groupId, String artifactId, String version, String type, String scope,
                List<Node> children) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.type = type;
        this.scope = scope;
        this.children = children;
    }
}
