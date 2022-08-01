package com.example.parse.facade;

import com.example.parse.model.ProjectInstruction;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ChangeFacade {

    Map<String, List<String>> getFutureChanges(File file, List<ProjectInstruction> instructions);
}
