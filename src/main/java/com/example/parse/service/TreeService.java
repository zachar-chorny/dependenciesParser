package com.example.parse.service;

import com.example.parse.model.DependencyTree;
import org.eclipse.aether.artifact.Artifact;

import java.util.List;

public interface TreeService {

    DependencyTree createTree(List<Artifact> artifacts, String projectName);
}
