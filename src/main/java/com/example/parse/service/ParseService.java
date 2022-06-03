package com.example.parse.service;

import com.example.parse.model.dto.ModelDto;

import java.io.File;

public interface ParseService {
    ModelDto getArtifactsFromFile(File file);
}
