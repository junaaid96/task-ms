package dev.cyborg.task_management.service;

import dev.cyborg.task_management.dto.ProjectDTO;
import dev.cyborg.task_management.dto.TaskDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

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

    public Project createProjectWithTasks(String projectName, String projectDescription, Set<Task> tasks) {
        Project project = new Project(projectName, projectDescription);
        for (Task task : tasks) {
            task.setProject(project);
        }
        project.setTasks(tasks);
        return projectRepository.save(project);
    }

    // load sample project data
    public void loadProjectData() {
        Project project1 = new Project("Project 1", "Project description 1");
        Project project2 = new Project("Project 2", "Project description 2");
    
        Task task1 = new Task("Task 1", "Task description 1", TaskStatus.TODO, TaskPriority.LOW);
        Task task2 = new Task("Task 2", "Task description 2", TaskStatus.DONE, TaskPriority.URGENT);
        Task task3 = new Task("Task 3", "Task description 3", TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM);
        Task task4 = new Task("Task 4", "Task description 4", TaskStatus.REVIEW, TaskPriority.HIGH);
    
        // Set due dates
        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
        task1.setDueDate(dueDate);
        task2.setDueDate(dueDate);
        task3.setDueDate(dueDate);
        task4.setDueDate(dueDate);
    
        // Add tasks to projects
        project1.addTask(task1);
        project1.addTask(task2);
        project2.addTask(task3);
        project2.addTask(task4);
    
        // Save projects (will cascade save tasks)
        projectRepository.saveAll(Arrays.asList(project1, project2));
    }
}
