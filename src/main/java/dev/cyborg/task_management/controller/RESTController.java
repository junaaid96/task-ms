package dev.cyborg.task_management.controller;

import dev.cyborg.task_management.dto.*;
import dev.cyborg.task_management.model.Task;
import dev.cyborg.task_management.service.ProjectService;
import dev.cyborg.task_management.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RESTController {
    @Autowired
    TaskService taskService;
    @Autowired
    private ProjectService projectService;

    @GetMapping("/load-data")
    public ResponseEntity<String> loadSampleProjects() {
        projectService.loadSampleData();
        return ResponseEntity.ok("Data Loaded!");
    }

    @GetMapping("/clear-all")
    public ResponseEntity<String> clearAllData() {
        projectService.clearAllData();
        return ResponseEntity.ok("All cleared!");
    }

    @PostMapping("/create-task")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        Task task = taskService.createTask(createTaskDTO);
        return ResponseEntity.ok(taskService.convertToDTO(task));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @Valid @RequestBody UpdateTaskDTO updateTaskDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskId, updateTaskDTO));
    }

    @PostMapping("/create-project")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody CreateProjectDTO createProjectDTO) {
        return ResponseEntity.ok(projectService.createProject(createProjectDTO));
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long projectId, @Valid @RequestBody UpdateProjectDTO updateProjectDTO) {
        return ResponseEntity.ok(projectService.updateProject(projectId, updateProjectDTO));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return ResponseEntity.ok(projectService.getProjects());
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasks() {
        return ResponseEntity.ok(taskService.getTasks());
    }
}