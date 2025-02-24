package dev.cyborg.task_management.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskDTO {
    @NotEmpty(message = "Title is required")
    private String title;
    @NotEmpty(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    @NotEmpty(message = "Priority is required")
    private String priority;
    private String status;
    private LocalDateTime dueDate;
    @NotNull(message = "Please select project for the task")
    private Long projectId;
}