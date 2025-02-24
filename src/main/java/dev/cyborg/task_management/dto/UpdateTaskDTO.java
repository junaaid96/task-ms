package dev.cyborg.task_management.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTaskDTO {
    @NotEmpty(message = "Title is required")
    private String title;
    @NotEmpty(message = "Description is required")
    private String description;
    @NotEmpty(message = "Priority is required")
    private String priority;
    @NotEmpty(message = "Status is required")
    private String status;
    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;
}