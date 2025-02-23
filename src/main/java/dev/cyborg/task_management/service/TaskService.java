package dev.cyborg.task_management.service;

import dev.cyborg.task_management.dto.CreateTaskDTO;
import dev.cyborg.task_management.dto.TaskDTO;
import dev.cyborg.task_management.dto.UpdateTaskDTO;
import dev.cyborg.task_management.enums.TaskPriority;
import dev.cyborg.task_management.enums.TaskStatus;
import dev.cyborg.task_management.model.Project;
import dev.cyborg.task_management.model.Task;
import dev.cyborg.task_management.repository.ProjectRepository;
import dev.cyborg.task_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public TaskDTO convertToDTO(Task task) {
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

    public Task createTask(CreateTaskDTO createTaskDTO) {
        Project project = projectRepository.findById(createTaskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDescription(createTaskDTO.getDescription());
        task.setStatus(TaskStatus.TODO); // Default status for new tasks
        task.setPriority(TaskPriority.valueOf(createTaskDTO.getPriority()));
        task.setDueDate(createTaskDTO.getDueDate());
        task.setProject(project);

        return taskRepository.save(task);
    }

    public TaskDTO updateTask(Long taskId, UpdateTaskDTO updateTaskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (updateTaskDTO.getTitle() != null) {
            task.setTitle(updateTaskDTO.getTitle());
        }
        if (updateTaskDTO.getDescription() != null) {
            task.setDescription(updateTaskDTO.getDescription());
        }
        if (updateTaskDTO.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(updateTaskDTO.getStatus()));
        }
        if (updateTaskDTO.getPriority() != null) {
            task.setPriority(TaskPriority.valueOf(updateTaskDTO.getPriority()));
        }
        if (updateTaskDTO.getDueDate() != null) {
            task.setDueDate(updateTaskDTO.getDueDate());
        }

        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }
}
