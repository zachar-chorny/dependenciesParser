package com.example.parse.model;

import com.example.parse.service.ParseService;
import com.example.parse.service.ProjectService;
import org.apache.maven.model.Model;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.Callable;

public class CallableProjectTask implements Callable<Optional<Project>> {
    private final ParseService parseService;
    private final ProjectService projectService;
    private final File file;
    private final boolean resolveDependencies;

    public CallableProjectTask(ParseService parseService, ProjectService projectService, File file,
                        boolean resolveDependencies){
        this.parseService = parseService;
        this.projectService = projectService;
        this.file = file;
        this.resolveDependencies = resolveDependencies;
    }

    @Override
    public Optional<Project> call() {
        Optional<Model> optionalModel = parseService.getModelFromFile(file);
        return optionalModel.map(m -> projectService.createProjectFromModel(m, resolveDependencies));
    }
}
