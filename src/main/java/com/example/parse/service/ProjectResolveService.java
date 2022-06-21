package com.example.parse.service;

import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;

public interface ProjectResolveService {

     void resolveProject(Project project, ProjectInstruction instruction);
}
