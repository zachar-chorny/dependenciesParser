package com.example.parse.service;

import com.example.parse.model.RepositoriesDto;
import lombok.AllArgsConstructor;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ArtifactResolveServiceImpl implements ArtifactResolveService {
    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession session;
    private final RepositoriesDto repositories;

    @Override
    public Optional<Artifact> resolve(Artifact artifact) {
        if (artifact != null) {
            ArtifactRequest artifactRequest = new ArtifactRequest();
            artifactRequest.setArtifact(artifact);
            artifactRequest.setRepositories(repositories.getRepositories());
            try {
                ArtifactResult artifactResult = repositorySystem.resolveArtifact(session, artifactRequest);
                return Optional.of(artifactResult.getArtifact());
            } catch (ArtifactResolutionException e) {
                return Optional.of(artifact);
            }
        }
        return Optional.empty();
    }
}
