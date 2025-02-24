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
@RequestMapping("${API_BASE_URL:/api}")
public class RESTController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

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

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
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