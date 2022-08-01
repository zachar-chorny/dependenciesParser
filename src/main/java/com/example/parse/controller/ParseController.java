package com.example.parse.controller;

import com.example.parse.facade.ParseFacade;
import com.example.parse.facade.impl.DefaultParseFacade;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;
import com.example.parse.model.Setting;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ParseController {
    private ParseFacade parseFacade;
    private static final String path = "src/main/resources/files/";

    @PostMapping(value = "/create")
    public List<Project> createProjects(@RequestBody @Valid Setting setting) {
//        if (multipartFile != null && setting == null) {
//            File file = Paths.get("", path + multipartFile
//                    .getOriginalFilename()).toAbsolutePath().toFile();
//            try {
//                multipartFile.transferTo(file);
//                return parseFacade.createProjectsFromFile(file);
//            } catch (IOException e) {
//                throw new WrongParamsException("Incorrect file");
//            }
//        } else if (setting != null && multipartFile == null) {
//            File file = new File(setting.getPath());
//            List<ProjectInstruction> instructions = setting.getInstructions();
//            if (instructions != null) {
//                return parseFacade.createProjectsFromFile(file, instructions);
//            }
//            return parseFacade.createProjectsFromFile(file);
//        }
//        throw new WrongParamsException("Incorrect params, pass only one parameter");
        File file = new File(setting.getPath());
        List<ProjectInstruction> instructions = setting.getInstructions();
        if (instructions != null) {
            return parseFacade.createProjectsFromFile(file, instructions);
        }
        return parseFacade.createProjectsFromFile(file, true);
    }

}
