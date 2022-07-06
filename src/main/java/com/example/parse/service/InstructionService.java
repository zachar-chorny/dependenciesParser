package com.example.parse.service;

import com.example.parse.model.DependencyNode;
import com.example.parse.model.Node;
import com.example.parse.model.ProjectInstruction;

import java.util.List;

public interface InstructionService {

    List<Node> addNodes(List<Node> nodes, ProjectInstruction instruction);

    List<Node> replaceNodes(List<Node> nodes, ProjectInstruction instruction);

    List<Node> removeNodes(List<Node> nodes, ProjectInstruction instruction);
}
