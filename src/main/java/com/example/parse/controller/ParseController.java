package com.example.parse.controller;

import com.example.parse.facade.ParseFacade;
import com.example.parse.model.Project;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ParseController {
    private ParseFacade parseFacade;
    private static final String path = "src/main/resources/files/";

    @PostMapping("/create")
    public List<Project> getModelFromFile(@RequestParam(value = "file", required = true)
                                          MultipartFile multipartFile) {
        File file = Paths.get("", path + multipartFile
                .getOriginalFilename()).toAbsolutePath().toFile();
        try {
            multipartFile.transferTo(file);
            return parseFacade.createProjectsFromFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Bad request");
        }
    }

}
