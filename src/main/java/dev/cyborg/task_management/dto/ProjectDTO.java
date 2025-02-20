package dev.cyborg.task_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
}