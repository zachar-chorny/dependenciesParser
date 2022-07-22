package com.example.parse.controller;

import com.example.parse.exception.WrongParamsException;
import com.example.parse.facade.ChangeFacade;
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
import java.util.Map;

@RestController
@RequestMapping("/changes")
@AllArgsConstructor
public class ChangeController {

    private final ChangeFacade changeFacade;

    @PostMapping(value = "/get")
    public Map<String, List<String>> getChanges(@RequestBody @Valid Setting setting) {
        File file = new File(setting.getPath());
        List<ProjectInstruction> instructions = setting.getInstructions();
        if (instructions != null) {
            return changeFacade.getFutureChanges(file, instructions);
        }
        throw new WrongParamsException("Instructions can't be null");
    }

}
