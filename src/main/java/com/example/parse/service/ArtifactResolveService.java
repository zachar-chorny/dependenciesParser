package com.example.parse.service;

import org.eclipse.aether.artifact.Artifact;

import java.util.List;

public interface ArtifactResolveService {

    List<Artifact> resolve(List<Artifact> artifacts);
}
