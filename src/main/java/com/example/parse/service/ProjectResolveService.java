package com.example.parse.service;

import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;

import java.util.Optional;

public interface ProjectResolveService {

     Optional<Project> createNewProject(Project project, ProjectInstruction instruction);
}
