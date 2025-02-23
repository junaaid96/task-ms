package dev.cyborg.task_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.cyborg.task_management.enums.TaskPriority;
import dev.cyborg.task_management.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString(exclude = "project")
@Table(name = "Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Title", nullable = false)
    // @NotBlank(message = "Title is required")
    private String title;

    @Column(name = "Description", length = 1000)
    // @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Column(name = "Status", nullable = false)
    @Enumerated(EnumType.STRING)
    // @NotNull(message = "Status is required")
    private TaskStatus status;

    @Column(name = "Priority", nullable = false)
    @Enumerated(EnumType.STRING)
    // @NotNull(message = "Priority is required")
    private TaskPriority priority;

    @Column(name = "Due Date")
    private LocalDateTime dueDate;

    @Column(name = "Created At")
    private LocalDateTime createdAt;

    @Column(name = "Updated At")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Project project;

    public Task() {
        // Required by JPA
    }

    public Task(String title, String description, TaskStatus status, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (dueDate == null) {
            dueDate = createdAt.plusDays(7);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
