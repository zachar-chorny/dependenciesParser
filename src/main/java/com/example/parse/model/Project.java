package com.example.parse.model;

import lombok.Data;

import java.util.List;

@Data
public class Project {

    private String name;
    private List<Node> nodes;
    private ParentNode parentNode;
}
