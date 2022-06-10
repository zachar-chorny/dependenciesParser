package com.example.parse.service;

import org.apache.maven.model.Model;

import java.io.File;
import java.util.Optional;

public interface ParseService {

    Optional<Model> getModelFromFile(File file);
}
