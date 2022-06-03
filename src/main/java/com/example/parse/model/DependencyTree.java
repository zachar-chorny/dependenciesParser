package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DependencyTree {
    private final String projectName;
    private List<GroupId> groups;
}
