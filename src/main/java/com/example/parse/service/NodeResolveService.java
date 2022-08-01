package com.example.parse.service;

import com.example.parse.model.Node;
import org.eclipse.aether.graph.Dependency;

import java.util.List;

public interface NodeResolveService {

    List<Node> getNodes(List<Dependency> dependencies, boolean resolveDependencies);
}
