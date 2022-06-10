package com.example.parse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

@AllArgsConstructor
@Data
public class RepositoriesDto {

    List<RemoteRepository> repositories;
}
