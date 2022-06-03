package com.example.parse.service;

import com.example.parse.model.ArtifactId;
import com.example.parse.model.DependencyTree;
import com.example.parse.model.GroupId;
import com.example.parse.model.Version;
import org.eclipse.aether.artifact.Artifact;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TreeServiceImpl implements TreeService{

    public DependencyTree createTree(List<Artifact> artifacts, String projectName) {
        List<GroupId> groups = new ArrayList<>();
        saveAvailableGroups(artifacts, groups);
        return new DependencyTree(projectName, groups);
    }

    private void saveAvailableGroups(List<Artifact> artifacts, List<GroupId> groups) {
        for (Artifact artifact : artifacts) {
            String group = artifact.getGroupId();
            List<Artifact> artifactsWithSameGroup = new ArrayList<>();
            for (Artifact artifact1 : artifacts) {
                if (artifact1.getGroupId().equals(group)) {
                    artifactsWithSameGroup.add(artifact1);
                }
            }
            artifacts.removeAll(artifactsWithSameGroup);
            groups.add(createGroup(artifactsWithSameGroup, group));
            break;
        }
        if(artifacts.size() > 0){
            saveAvailableGroups(artifacts, groups);
        }
    }

    private GroupId createGroup(List<Artifact> artifacts, String group) {
        List<ArtifactId> artifactIds = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            artifactIds.add(createArtifact(artifact));
        }
        return new GroupId(group, artifactIds);
    }

    private ArtifactId createArtifact(Artifact artifact) {
        Version version = new Version(artifact.getVersion());
        return new ArtifactId(artifact.getArtifactId(), version);
    }
}
