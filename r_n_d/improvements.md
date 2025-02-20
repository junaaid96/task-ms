Looking at your Project model, there are a couple of things that need to be added:

1. You need a no-args constructor (required by JPA)
2. You might want to add helper methods for managing the bidirectional relationship with Task

Here's how to update the Project class:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Project.java
// ... existing imports and annotations remain the same ...

public class Project {
    // ... existing fields remain the same ...

    public Project() {
        // Required by JPA
    }

    // ... existing constructor remains the same ...

    public void addTask(Task task) {
        tasks.add(task);
        task.setProject(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.setProject(null);
    }

    // ... existing equals and hashCode methods remain the same ...
}
```

And for the Task class, you should have:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Task.java
@Entity
@Getter
@Setter
@ToString(exclude = "project")
@Table(name = "Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ... other fields ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Project project;

    public Task() {
        // Required by JPA
    }

    // ... existing constructor, equals, hashCode methods ...
}
```

These changes ensure:

1. JPA can properly instantiate the entities
2. The bidirectional relationship is properly managed
3. Infinite recursion is prevented in toString(), equals(), and hashCode()
4. Proper JSON serialization with @JsonBackReference and @JsonManagedReference
5. Lazy loading for better performance with @ManyToOne(fetch = FetchType.LAZY)

## The models should now work correctly without any stack overflow or recursion issues.

Looking at both models, they are now well-structured, but there are a few improvements we can make:

1. In the <mcfile name="Project.java" path="d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Project.java"></mcfile>, we should add orphanRemoval to the tasks collection:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Project.java
    // ... existing code ...

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Task> tasks = new HashSet<>();

    // ... rest of the code ...
```

2. In the <mcfile name="Task.java" path="d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Task.java"></mcfile>, we should add some validation annotations:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Task.java
    // ... existing code ...

    @Column(name = "Title", nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(name = "Description", length = 1000)
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(name = "Status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private TaskStatus status;

    @Column(name = "Priority", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    // ... rest of the code ...
```

These improvements will:

1. Enable automatic removal of orphaned tasks when they're removed from a project
2. Add proper validation constraints to ensure data integrity
3. Make the API more robust by enforcing required fields
4. Provide meaningful validation messages

Don't forget to add the following import to Task.java:

```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
```

The rest of your model structure looks good with:

-   Proper bidirectional relationship management
-   Correct lazy loading configuration
-   Proper JSON serialization handling
-   Good timestamp management with @PrePersist and @PreUpdate
-   Correct equals and hashCode implementations

---

====================================================================================
Let's fix these issues:

1. First, let's update the ProjectDTO to include tasks:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\dto\ProjectDTO.java
@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Set<TaskDTO> tasks = new HashSet<>();
}
```

2. Update TaskDTO to include timestamps:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\dto\TaskDTO.java
@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private Long projectId;
    private String projectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

3. Update the ProjectService's convertToDto method:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\service\ProjectService.java
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
```

4. Update the TaskService's convertToDTO method:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\service\TaskService.java
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
```

5. For the missing tasks issue, make sure the fetch type for tasks in Project entity is EAGER:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Project.java
@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
@JsonManagedReference
private Set<Task> tasks = new HashSet<>();
```

These changes will:

1. Include tasks in project listings
2. Show all timestamps (createdAt and updatedAt)
3. Include project name in task DTOs
4. Fix the task fetching issue

The dueDate is null because it's not being set in your sample data loading. If you want to include due dates, update the loadProjectData method in ProjectService to set due dates:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\service\ProjectService.java
Task task1 = new Task("Task 1", "Task description 1", TaskStatus.TODO, TaskPriority.LOW);
task1.setDueDate(LocalDateTime.now().plusDays(7));
```

# After these changes, restart your application and hit the endpoints again. You should see complete data including tasks in projects, all timestamps, and project names in tasks.

The issue is in your ProjectService's loadProjectData method. It's creating 4 tasks but only 2 are showing up because we need to modify the TaskRepository to fetch all tasks. Let's fix this:

1. First, update the TaskRepository to include a method to find tasks by project:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\repository\TaskRepository.java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByProjectIdOrderByCreatedAtDesc(Long projectId);
}
```

2. Then, update the TaskService's getTasks method to ensure we're getting all tasks:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\service\TaskService.java
public List<TaskDTO> getTasks() {
    List<Task> tasks = taskRepository.findAll();
    return tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
}
```

3. Double-check the ProjectService's loadProjectData method to ensure all tasks are being saved:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\service\ProjectService.java
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
```

After making these changes:

1. Clear your database (if possible)
2. Restart your application
3. Hit the `/load-data` endpoint again
4. Check `/all-tasks` endpoint

You should now see all 4 tasks in the response. If you're still only seeing 2 tasks, you might want to check:

1. Your database to ensure all tasks are being saved
2. The SQL queries being generated (enable SQL logging in application.properties)
3. Any potential transaction issues

To enable SQL logging, add this to your application.properties:

```properties:d:\TEST\task-ms\src\main\resources\application.properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```
