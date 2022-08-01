package com.example.parse.model;

import com.example.parse.service.NodeService;
import org.eclipse.aether.graph.Dependency;

import java.util.concurrent.Callable;

public class CallableNodeTask implements Callable<Node> {

    private final Dependency dependency;
    private final boolean resolveDependencies;
    private final NodeService nodeService;

    public CallableNodeTask(Dependency dependency, boolean resolveDependencies, NodeService nodeService) {
        this.dependency = dependency;
        this.resolveDependencies = resolveDependencies;
        this.nodeService = nodeService;
    }


    @Override
    public Node call() throws Exception {
        return nodeService.getNodeFromDependency(dependency, resolveDependencies);
    }
}
