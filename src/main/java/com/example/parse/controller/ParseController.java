package com.example.parse.controller;

import com.example.parse.facade.ParseFacade;
import com.example.parse.model.Project;
import com.example.parse.model.Setting;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ParseController {
    private ParseFacade parseFacade;
    private static final String path = "src/main/resources/files/";

    @PostMapping(value = "/create")
    public List<Project> getModelFromFile(@RequestParam(required = false) Setting setting,
                                          @RequestParam(value = "file", required = false)
                                          MultipartFile multipartFile) {
        List<Project> projects = new ArrayList<>();
        if(multipartFile != null){
            File file = Paths.get("", path + multipartFile
                    .getOriginalFilename()).toAbsolutePath().toFile();
            try {
                multipartFile.transferTo(file);
                projects.addAll(parseFacade.createProjectsFromFile(file));
            } catch (IOException ignored) {}
        }
        if(setting != null){
            File file = new File(path);
            projects.addAll(parseFacade.createProjectsFromFile(file));
        }
        return projects;
    }

}
