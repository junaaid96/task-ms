package dev.cyborg.task_management.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Set<TaskDTO> tasks = new HashSet<>();
}