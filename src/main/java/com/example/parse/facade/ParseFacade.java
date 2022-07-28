package com.example.parse.facade;

import com.example.parse.model.CallableTask;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import com.example.parse.service.ParseService;
import com.example.parse.service.ProjectResolveService;
import com.example.parse.service.ProjectService;
import lombok.AllArgsConstructor;
import org.apache.maven.model.Model;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@AllArgsConstructor
public class ParseFacade {
    private final ParseService parseService;
    private final ProjectService projectService;
    private final ProjectResolveService projectResolveService;

    public List<Project> createProjectsFromFile(File file, boolean resolveDependencies) {
        List<File> files = getFiles(file);
        return getProjectsFromFiles(files, resolveDependencies);
    }

    private List<Project> getProjectsFromFiles(List<File> files, boolean resolveDependencies) {
        List<Project> projects = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(25);
        List<Future<Optional<Project>>> futures = new ArrayList<>();
        for (File iterFile : files) {
            futures.add(executorService.submit(new CallableTask(parseService, projectService, iterFile,
                    resolveDependencies)));
        }
        executorService.shutdown();
        for (Future<Optional<Project>> future : futures) {
            try {
                future.get().ifPresent(projects::add);
            } catch (InterruptedException | ExecutionException ignored) {
            }
        }
        return projects;
    }

    private List<File> getFiles(File file) {
        List<File> fileList = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach(f -> fileList.addAll(getFiles(f)));
            }
        } else {
            if(file.getName().equals("pom.xml")){
                fileList.add(file);
            }
        }
        return fileList;
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
