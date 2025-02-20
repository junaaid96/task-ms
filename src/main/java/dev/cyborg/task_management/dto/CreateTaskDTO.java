package dev.cyborg.task_management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskDTO {
    private String title;
    private String description;
    private String priority;
    private LocalDateTime dueDate;
    private Long projectId;
}