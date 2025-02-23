package dev.cyborg.task_management.service;

import dev.cyborg.task_management.dto.*;
import dev.cyborg.task_management.enums.TaskPriority;
import dev.cyborg.task_management.enums.TaskStatus;
import dev.cyborg.task_management.model.Project;
import dev.cyborg.task_management.model.Task;
import dev.cyborg.task_management.repository.ProjectRepository;
import dev.cyborg.task_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public List<ProjectDTO> getProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProjectDTO convertToDto(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setTasks(project.getTasks().stream()
                .map(this::convertTaskToDto)
                .collect(Collectors.toSet()));
        return projectDTO;
    }

    private TaskDTO convertTaskToDto(Task task) {
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

    public ProjectDTO createProject(CreateProjectDTO createProjectDTO) {
        Project project = new Project();
        project.setName(createProjectDTO.getName());
        project.setDescription(createProjectDTO.getDescription());

        if (createProjectDTO.getTasks() != null) {
            for (CreateTaskDTO createTaskDTO : createProjectDTO.getTasks()) {
                Task task = new Task();
                task.setTitle(createTaskDTO.getTitle());
                task.setDescription(createTaskDTO.getDescription());
                task.setStatus(TaskStatus.TODO); // Default status for new tasks
                task.setPriority(TaskPriority.valueOf(createTaskDTO.getPriority()));
                task.setDueDate(createTaskDTO.getDueDate());
                task.setProject(project);
                project.getTasks().add(task);
            }
        }

        Project savedProject = projectRepository.save(project);
        return convertToDto(savedProject);
    }

    public ProjectDTO updateProject(Long projectId, UpdateProjectDTO updateProjectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (updateProjectDTO.getName() != null) {
            project.setName(updateProjectDTO.getName());
        }
        if (updateProjectDTO.getDescription() != null) {
            project.setDescription(updateProjectDTO.getDescription());
        }

        Project savedProject = projectRepository.save(project);
        return convertToDto(savedProject);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        projectRepository.delete(project);
    }

    // Clear all data from the database
    public void clearAllData() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
    }
}
