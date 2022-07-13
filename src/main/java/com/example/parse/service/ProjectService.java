package com.example.parse.service;

import com.example.parse.model.Project;
import org.apache.maven.model.Model;

import java.util.Optional;

public interface ProjectService {

    Project createProjectFromModel(Model model);
}
