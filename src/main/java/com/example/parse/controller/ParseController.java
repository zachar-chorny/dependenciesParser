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
import java.util.List;

@RestController
@RequestMapping("/parse")
@AllArgsConstructor
public class ParseController {
    private ParseFacade parseFacade;
    private static final String path = "/Users/zchornyi/IdeaProjects/dependenciesParser/src/main/resources/files/";

    @PostMapping("/getTree")
    public List<Project> getModelFromFile(@RequestParam(value = "file", required = true)
                                          MultipartFile multipartFile) {
        File file = new File(path + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
            return parseFacade.createProjectsFromFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Bad request");
        }
    }

}
