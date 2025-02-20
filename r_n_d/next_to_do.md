Looking at your codebase, you have some DTOs that aren't being utilized. Let's implement them:

1. First, let's use `CreateTaskDTO` in the TaskService:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\service\TaskService.java
// ... existing code ...

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

// ... existing code ...
```

2. Add an endpoint in RESTController to use CreateTaskDTO:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\controller\RESTController.java
// ... existing code ...

@PostMapping("/tasks")
public TaskDTO createTask(@RequestBody CreateTaskDTO createTaskDTO) {
    Task task = taskService.createTask(createTaskDTO);
    return taskService.convertToDTO(task);
}

// ... existing code ...
```

3. Implement UpdateTaskDTO functionality in TaskService:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\service\TaskService.java
// ... existing code ...

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

// Make convertToDTO public so it can be used by controller
public TaskDTO convertToDTO(Task task) {
    // ... existing convertToDTO code ...
}
```

4. Add an endpoint in RESTController to use UpdateTaskDTO:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\controller\RESTController.java
// ... existing code ...

@PutMapping("/tasks/{taskId}")
public TaskDTO updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskDTO updateTaskDTO) {
    return taskService.updateTask(taskId, updateTaskDTO);
}

// ... existing code ...
```

Now you can:
1. Create tasks using POST `/tasks` with CreateTaskDTO
2. Update tasks using PUT `/tasks/{taskId}` with UpdateTaskDTO
3. Get all tasks using GET `/all-tasks`
4. Get all projects with their tasks using GET `/all-projects`

The DTOs provide a clean way to:
- Create new tasks with specific fields (CreateTaskDTO)
- Update existing tasks partially (UpdateTaskDTO)
- Return task information with project details (TaskDTO)
- Return project information with task details (ProjectDTO)