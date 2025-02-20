package dev.cyborg.task_management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateTaskDTO {
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
}