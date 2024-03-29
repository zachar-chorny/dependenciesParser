package com.example.parse.facade.impl;

import com.example.parse.facade.ParseFacade;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import com.example.parse.service.ParseService;
import com.example.parse.service.ProjectResolveService;
import com.example.parse.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class DefaultParseFacade implements ParseFacade {
    private final ParseService parseService;
    private final ProjectService projectService;
    private final ProjectResolveService projectResolveService;

    public List<Project> createProjectsFromFile(File file, boolean resolveDependencies) {
        List<Project> projects = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(f -> projects.addAll(createProjectsFromFile(f, resolveDependencies)));
            }
        } else {
            if(file.getName().equals("pom.xml")) {
                parseService.getModelFromFile(file).ifPresent(model -> projects.add
                        (projectService.createProjectFromModel(model, resolveDependencies)));
            }
        }
        return projects;
    }

    public List<Project> createProjectsFromFile(File file, List<ProjectInstruction> instructions) {
        List<Project> projects = createProjectsFromFile(file, true);
        for (ProjectInstruction instruction : instructions) {
            for (Project project : projects) {
                if (instruction.getName().equals(project.getName())) {
                    project.setNewProject(projectResolveService.createNewProject(project, instruction));
                }
            }
        }
        return projects;
    }

}
