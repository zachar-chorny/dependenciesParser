package com.example.parse.facade;

import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;

import java.io.File;
import java.util.List;

public interface ParseFacade {

    List<Project> createProjectsFromFile(File file, boolean resolveDependencies);

    List<Project> createProjectsFromFile(File file, List<ProjectInstruction> instructions);
}
