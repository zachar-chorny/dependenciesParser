package com.example.parse.facade;

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
public class ParseFacade {
    private final ParseService parseService;
    private final ProjectService projectService;
    private final ProjectResolveService projectResolveService;

    public List<Project> createProjectsFromFile(File file) {
        List<Project> projects = new ArrayList<>();
        if (file != null) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    Arrays.stream(files).forEach(f -> projects.addAll(createProjectsFromFile(f)));
                }
            } else {
                parseService.getModelFromFile(file).flatMap(projectService::createProjectFromModel)
                        .ifPresent(projects::add);
            }
        }
        return projects;
    }

    public List<Project> createProjectsFromFile(File file, List<ProjectInstruction> instructions) {
        List<Project> projects = createProjectsFromFile(file);
        for (ProjectInstruction instruction : instructions) {
            for (Project project : projects) {
                if (instruction.getName().equals(project.getName())) {
                    projectResolveService.createNewProject(project, instruction)
                            .ifPresent(project::setNewProject);
                }
            }
        }
        return projects;
    }

}
