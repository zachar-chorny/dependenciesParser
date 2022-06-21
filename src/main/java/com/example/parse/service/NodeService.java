package com.example.parse.service;

import com.example.parse.model.Node;
import org.eclipse.aether.graph.Dependency;

public interface NodeService {

    Node getNodeFromDependency(Dependency dependency);
}
