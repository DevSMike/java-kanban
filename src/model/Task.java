package model;

import service.StatusManager;

import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected  String status;

    public Task(String name, String description) {
       this.name = name;
       this.description = description;
    }

    public void setStatus(StatusManager.Statuses id) {
        status = StatusManager.getStatusId(id);
    }
    public StatusManager.Statuses getStatus() {
        return StatusManager.getStatusIdByString(status);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + name + '\'' +
                ", taskDescription='" + description + '\'' +
                ", status='" + status + '\'' +
               "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
