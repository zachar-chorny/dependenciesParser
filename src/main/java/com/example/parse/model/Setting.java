package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Setting {

    private String path;
    private List<ProjectInstruction> instructions;
}
