package com.example.parse.service;

import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectResolveService {

     Project createNewProject(Project project, ProjectInstruction instruction);

     Map<String, List<String>> createNewProject(Project project, ProjectInstruction instruction,
                                                Map<String, List<String>> changes);
}
