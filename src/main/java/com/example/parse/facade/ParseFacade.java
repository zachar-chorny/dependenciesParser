package com.example.parse.facade;

import com.example.parse.model.Project;
import com.example.parse.service.ParseService;
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

    public List<Project> createProjectsFromFile(File file) {
        List<Project> projects = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(f -> projects.addAll(createProjectsFromFile(f)));
            }
        } else {
            parseService.getModelFromFile(file).ifPresent(
                    p -> projects.add(projectService.createProjectFromModel(p)));

        }
        return projects;
    }

}
