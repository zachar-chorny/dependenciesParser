package com.example.parse.service;

import com.example.parse.model.DependencyNode;
import com.example.parse.model.Node;
import com.example.parse.model.Project;
import com.example.parse.model.ProjectInstruction;

import java.util.List;
import java.util.Map;

public interface InstructionService {

    List<Node> addNodes(List<Node> nodes, ProjectInstruction instruction);

    List<Node> replaceNodes(List<Node> nodes, ProjectInstruction instruction);

    Map<String, List<String>> replaceNodes(Project project, ProjectInstruction instruction,
                                           Map<String, List<String>> changes);

    List<Node> removeNodes(List<Node> nodes, ProjectInstruction instruction);

    Map<String, List<String>> removeNodes(Project project, ProjectInstruction instruction,
                                          Map<String, List<String>> changes);
}
