package com.example.parse.service;

import org.eclipse.aether.artifact.Artifact;

import java.util.Optional;

public interface ArtifactResolveService {

    Artifact resolve(Artifact artifact);
}
