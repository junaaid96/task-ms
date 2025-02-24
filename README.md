# Task Management System API

A RESTful API for managing projects and tasks.

## Base URL
```
/api
```

## Endpoints

### Projects

#### Get All Projects
- **GET** `/api/projects`
- Returns a list of all projects with their associated tasks
- Response: `List<ProjectDTO>`

#### Create Project
- **POST** `/api/create-project`
- Creates a new project
- Body: 
```json
{
    "name": "string",
    "description": "string",
    "tasks": [
        {
            "title": "string",
            "description": "string",
            "priority": "LOW|MEDIUM|HIGH|URGENT",
            "status": "string",
            "dueDate": "yyyy-MM-dd'T'HH:mm:ss",
            "projectId": "number"
        }
    ]
}
```

#### Update Project
- **PUT** `/api/projects/{projectId}`
- Updates an existing project
- Body:
```json
{
    "name": "string",
    "description": "string"
}
```

#### Delete Project
- **DELETE** `/api/projects/{projectId}`
- Deletes a project and its associated tasks

### Tasks

#### Get All Tasks
- **GET** `/api/tasks`
- Returns a list of all tasks
- Response: `List<TaskDTO>`

#### Create Task
- **POST** `/api/create-task`
- Creates a new task
- Body:
```json
{
    "title": "string",
    "description": "string",
    "priority": "LOW|MEDIUM|HIGH|URGENT",
    "status": "TODO|IN_PROGRESS|REVIEW|DONE",
    "dueDate": "yyyy-MM-dd'T'HH:mm:ss",
    "projectId": "number"
}
```

#### Update Task
- **PUT** `/api/tasks/{taskId}`
- Updates an existing task
- Body:
```json
{
    "title": "string",
    "description": "string",
    "priority": "LOW|MEDIUM|HIGH|URGENT",
    "status": "TODO|IN_PROGRESS|REVIEW|DONE",
    "dueDate": "yyyy-MM-dd'T'HH:mm:ss"
}
```

#### Delete Task
- **DELETE** `/api/tasks/{taskId}`
- Deletes a specific task

### Utility

#### Clear All Data
- **GET** `/api/clear-all`
- Removes all projects and tasks from the database
- Response: "All cleared!"

## Validation

- All required fields are validated
- Description fields have a maximum length of 1000 characters
- Task priority must be one of: LOW, MEDIUM, HIGH, URGENT
- Task status must be one of: TODO, IN_PROGRESS, REVIEW, DONE
- Project ID is required when creating a task
