package com.example.parse.facade;

import com.example.parse.model.ParentNode;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import com.example.parse.service.ProjectResolveService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ChangeFacade {
    private final ParseFacade parseFacade;
    private final ProjectResolveService projectResolveService;

    public Map<String, List<String>> getFutureChanges(File file, List<ProjectInstruction> instructions) {
        List<Project> projects = parseFacade.createProjectsFromFile(file);
        Map<String, List<String>> changes = new HashMap<>();
        for (ProjectInstruction instruction : instructions) {
            List<Project> childProjects = getChildProjects(instruction.getName(), projects);
            for (Project project : childProjects) {
                changes = projectResolveService.createNewProject(project, instruction, changes);
            }
        }
        return changes;
    }


    private List<Project> getChildProjects(String name, List<Project> projects) {
        List<Project> correctProjects = new ArrayList<>();
        for (Project project : projects) {
            ParentNode parentNode = project.getParentNode();
            if (parentNode != null) {
                if (parentNode.getArtifactId().equals(name)) {
                    correctProjects.add(project);
                    correctProjects.addAll(getChildProjects(project.getName(), projects));
                }
            }
        }
        return correctProjects;
    }
}
