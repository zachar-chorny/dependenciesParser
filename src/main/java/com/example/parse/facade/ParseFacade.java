package com.example.parse.facade;

import com.example.parse.model.DependencyTree;
import com.example.parse.model.dto.ModelDto;
import com.example.parse.service.ParseService;
import com.example.parse.service.TreeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ParseFacade {
    private final ParseService parseService;
    private final TreeService treeService;
    private static final String pom = "pom.xml";
    private static final String bom = "bom.xml";

    public List<DependencyTree> createTreeFromFile(File file) {
        List<ModelDto> artifacts = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null){
                for(File file1 : files){
                    createTreeFromFile(file1);
                }
            }
        }else if (file.getName().equals(pom) || file.getName().equals(bom)){
            artifacts.add(parseService.getArtifactsFromFile(file));
        }
        return createTriesFromModels(artifacts);
    }

    private List<DependencyTree> createTriesFromModels(List<ModelDto> models) {
        return models.stream()
                .map(a -> treeService.createTree(a.getArtifacts(), a.getProjectName()))
                .collect(Collectors.toList());
    }

}
