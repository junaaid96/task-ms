The error you're encountering is a classic case of infinite recursion caused by Lombok's `@Data` annotation in a bidirectional relationship between `Project` and `Task` entities. The problem occurs because both entities try to call each other's `hashCode()` and `toString()` methods recursively.

Let's fix this by modifying both entity classes:

1. First, let's fix the `Task` class:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Task.java
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
    // ... other fields remain the same ...

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Project project;

    // ... other methods remain the same ...

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
```

2. Then, let's fix the `Project` class:

```java:d:\TEST\task-ms\src\main\java\dev\cyborg\task_management\model\Project.java
package dev.cyborg.task_management.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = "tasks")
@Table(name = "Project")
public class Project {
    // ... other fields remain the same ...

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Task> tasks = new HashSet<>();

    // ... constructors remain the same ...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
```

The key changes made:

1. Replaced `@Data` with specific Lombok annotations (`@Getter`, `@Setter`, `@ToString`)
2. Added `@ToString(exclude = ...)` to break the circular reference
3. Added proper `@JsonManagedReference` and `@JsonBackReference` annotations to handle JSON serialization
4. Implemented custom `equals()` and `hashCode()` methods that only use the ID field
5. Used proper JPA annotations for bidirectional relationship

These changes will resolve the `StackOverflowError` by:

-   Breaking the circular reference in toString() methods
-   Implementing proper equals() and hashCode() methods that don't cause infinite recursion
-   Properly handling JSON serialization with Jackson annotations

After making these changes, the `/load-data` endpoint should work correctly without any stack overflow errors.
