package com.example.parse.service;

import com.example.parse.model.CallableNodeTask;
import com.example.parse.model.Node;
import com.example.parse.service.NodeResolveService;
import lombok.AllArgsConstructor;
import org.eclipse.aether.graph.Dependency;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
@Service
@AllArgsConstructor
public class NodeResolveServiceImpl implements NodeResolveService {

    private final NodeService nodeService;

    @Override
    public List<Node> getNodes(List<Dependency> dependencies, boolean resolveDependencies) {
        List<Node> nodes = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<Node>> futures = new ArrayList<>();
        for (Dependency dependency : dependencies) {
            futures.add(executorService.submit(new CallableNodeTask(dependency, resolveDependencies,
                    nodeService)));
        }
        executorService.shutdown();
        for (Future<Node> future : futures) {
            try {
                nodes.add(future.get());
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
        return nodes;
    }
}
