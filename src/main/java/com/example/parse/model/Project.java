package com.example.parse.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode
public class Project {

    private String name;
    private List<Node> nodes;
    private ParentNode parentNode;
    private Project newProject;
}
