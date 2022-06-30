package com.example.parse.service;

import com.example.parse.model.Node;
import org.eclipse.aether.graph.Dependency;

import java.util.Optional;

public interface NodeService {

    Optional<Node> getNodeFromDependency(Dependency dependency);
}
