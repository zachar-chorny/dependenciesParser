package com.example.parse.model;

import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Setting {

    @NotNull(message = "Path can't be null")
    private String path;
    @Valid
    private List<ProjectInstruction> instructions;
}
