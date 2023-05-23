# Backend Project: Kanban
This project serves as the backend for a note-taking application that allows you to manage team notes.

## Features

The note management app includes the following features:

### Note Types

- Task: Create a task, get all tasks, get a task by ID, update a task.
- Epic: Create an epic, update an epic, delete an epic and all its subtasks, get all subtasks for an epic. The epic status is determined by the status of its subtasks.
- Subtask: Add a subtask to an epic, delete a subtask from an epic, change the status of a subtask, get the epic associated with a subtask.

### History

- View the history of task views.

### Priority Tasks

- View tasks by priority.

## Testing

The functionality has been tested using JUnit 5 in the `tests` package.

## Installation

To use this project, follow these steps:

1. Clone the repository: `git clone https://github.com/DevSMike/java-kanban.git`
2. Navigate to the project directory: `cd note-management-app`
3. Compile the Java source files: `javac *.java`

## Development Stack

The project is developed using the following technologies:

- Java 11
- JUnit 

## Contributing

Contributions to this project are welcome. To contribute, follow these steps:

1. Fork the repository.
2. Create a new branch: `git checkout -b my-branch`
3. Make your changes and commit them: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin my-branch`
5. Submit a pull request.
