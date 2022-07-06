package com.example.parse.service;

import com.example.parse.model.RepositoriesDto;
import lombok.SneakyThrows;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ArtifactResolveServiceImplTest {

    @InjectMocks
    private ArtifactResolveServiceImpl artifactResolveService;
    @Mock
    private RepositorySystem repositorySystem;
    @Mock
    private RepositoriesDto repositories;
    private static final String REMOTE_REPOSITORY_URL = "https://repo1.maven.org/maven2/";

    @BeforeEach
    public void setup() {
        RemoteRepository remoteRepository = new RemoteRepository.Builder(
                "central", "default", REMOTE_REPOSITORY_URL).build();
        Mockito.lenient().when(repositories.getRepositories()).thenReturn(List.of(remoteRepository));
    }

    @SneakyThrows
    @DisplayName("Test case return resolved artifact.")
    @Test
    void shouldReturnResolvedArtifact() {
        Artifact artifact = new DefaultArtifact("javax.validation", "validation-api",
                "jar", "2.0.1.Final");
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(repositories.getRepositories());
        Artifact resolvedArtifact = artifact.setFile(new File(""));
        ArtifactResult artifactResult = new ArtifactResult(artifactRequest);
        artifactResult.setArtifact(resolvedArtifact);
        Mockito.when(repositorySystem.resolveArtifact(any(), any()))
                .thenReturn(artifactResult);
        Optional<Artifact> result = artifactResolveService.resolve(artifact);
        if(result.isPresent()){
            Assertions.assertNotNull(result.get().getFile());
        }else {
            Assertions.fail();
        }
    }

    @SneakyThrows
    @DisplayName("Test case return default artifact.")
    @Test
    void shouldReturnDefaultArtifact() {
        Artifact artifact = new DefaultArtifact("incorrect", "incorrect",
                "jar", "2.0.1.Final");
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        Mockito.when(repositorySystem.resolveArtifact(any(), any()))
                .thenThrow(ArtifactResolutionException.class);
        Optional<Artifact> result = artifactResolveService.resolve(artifact);
        if(result.isPresent()){
            Assertions.assertEquals(artifact, result.get());
        }else {
            Assertions.fail();
        }
    }

    @DisplayName("Test case return empty optional.")
    @Test
    void shouldReturnEmptyOptional() {
        Optional<Artifact> artifact = artifactResolveService.resolve(null);
        Assertions.assertTrue(artifact.isEmpty());
    }

}