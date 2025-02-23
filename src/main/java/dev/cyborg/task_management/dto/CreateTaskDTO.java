package dev.cyborg.task_management.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskDTO {
    @NotEmpty(message = "Title is required")
    private String title;
    @NotEmpty(message = "Description is required")
    private String description;
    @NotEmpty(message = "Priority status is required")
    private String priority;
    private LocalDateTime dueDate;
    @NotNull(message = "Please select project for the task")
    private Long projectId;
}