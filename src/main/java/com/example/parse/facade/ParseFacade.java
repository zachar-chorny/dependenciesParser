package com.example.parse.facade;

import com.example.parse.model.Project;
import com.example.parse.service.ParseService;
import com.example.parse.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ParseFacade {
    private final ParseService parseService;
    private final ProjectService treeService;

    public List<Project> createProjectsFromFile(File file) {
        List<Project> projects = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    createProjectsFromFile(file1);
                }
            }
        } else {
            parseService.getModelFromFile(file).ifPresent(p -> projects.add(treeService.createProjectFromModel(p)));

        }
        return projects;
    }

}
