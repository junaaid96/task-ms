package dev.cyborg.task_management.controller;

import dev.cyborg.task_management.dto.ProjectDTO;
import dev.cyborg.task_management.dto.TaskDTO;
import dev.cyborg.task_management.model.Project;
import dev.cyborg.task_management.model.Task;
import dev.cyborg.task_management.service.ProjectService;
import dev.cyborg.task_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RESTController {
    @Autowired
    TaskService taskService;
    @Autowired
    private ProjectService projectService;

    @GetMapping("/load-data")
    public String loadSampleProjects() {
        projectService.loadProjectData();
        return "Data Loaded!";
    }

    @GetMapping("/all-projects")
    public List<ProjectDTO> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/all-tasks")
    public List<TaskDTO> getTasks() {
        return taskService.getTasks();
    }
}
