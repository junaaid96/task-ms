package dev.cyborg.task_management.service;

import dev.cyborg.task_management.dto.TaskDTO;
import dev.cyborg.task_management.enums.TaskPriority;
import dev.cyborg.task_management.enums.TaskStatus;
import dev.cyborg.task_management.model.Project;
import dev.cyborg.task_management.model.Task;
import dev.cyborg.task_management.repository.ProjectRepository;
import dev.cyborg.task_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<TaskDTO> getTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus().name());
        taskDTO.setPriority(task.getPriority().name());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setProjectId(task.getProject().getId());
        taskDTO.setProjectName(task.getProject().getName());
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setUpdatedAt(task.getUpdatedAt());
        return taskDTO;
    }

    public Task createTaskForExistingProject(Long projectId, String title, String description, TaskStatus status, TaskPriority priority) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        Task task = new Task(title, description, status, priority);
        task.setProject(project);
        return taskRepository.save(task);
    }
}
