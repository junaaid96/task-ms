package dev.cyborg.task_management.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateProjectDTO {
    @NotEmpty(message = "Name is required")
    private String name;
    @NotEmpty(message = "Description is required")
    private String description;
}
