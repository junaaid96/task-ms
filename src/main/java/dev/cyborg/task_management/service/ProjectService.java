package dev.cyborg.task_management.service;


import dev.cyborg.task_management.dto.ProjectDTO;
import dev.cyborg.task_management.enums.TaskPriority;
import dev.cyborg.task_management.enums.TaskStatus;
import dev.cyborg.task_management.model.Project;
import dev.cyborg.task_management.model.Task;
import dev.cyborg.task_management.repository.ProjectRepository;
import dev.cyborg.task_management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        return projectDTO;
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

        task1.setProject(project1);
        task2.setProject(project1);
        task3.setProject(project2);
        task4.setProject(project2);

        Set<Task> tasks1 = new HashSet<>(Arrays.asList(task1, task2));
        Set<Task> tasks2 = new HashSet<>(Arrays.asList(task3, task4));

        project1.setTasks(tasks1);
        project2.setTasks(tasks2);

        projectRepository.saveAll(Arrays.asList(project1, project2));
    }
}
