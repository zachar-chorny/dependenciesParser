package com.example.parse.service;

import lombok.AllArgsConstructor;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ArtifactResolveServiceImpl implements ArtifactResolveService {
    private RepositorySystem repositorySystem;
    private RepositorySystemSession session;


    @Override
    public List<Artifact> resolve(List<Artifact> artifacts) {
        List<Artifact> resolvedArtifacts = new ArrayList<>();
        artifacts.stream().forEach(a -> {
                ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(a);
        artifactRequest.setRepositories(getRepositories());
        try {
            ArtifactResult artifactResult = repositorySystem
                    .resolveArtifact(session, artifactRequest);
            resolvedArtifacts.add(artifactResult.getArtifact());
        } catch (ArtifactResolutionException e) {
            resolvedArtifacts.add(a);
            throw new RuntimeException("can't resolve artifact with name: " + a.getArtifactId());
        }
    });
        return resolvedArtifacts;
}

    private List<RemoteRepository> getRepositories() {
        return Arrays.asList(getCentralMavenRepository());
    }

    private RemoteRepository getCentralMavenRepository() {
        return new RemoteRepository.Builder("content", "default", "https://repo1.maven.org/maven2/")
                .build();
    }
}
