package model;

import java.util.Objects;

public class Task {
   protected String taskName;
   protected String taskDescription;
   protected int taskId;
   protected  String curStatus;
   protected  String[] taskStatuses = { "NEW", "IN_PROGRESS", "DONE" };

   public enum Statuses {
       NEW,
       IN_PROGRESS,
       DONE
   }
    public Task(String taskName, String taskDescription) {
       this.taskName = taskName;
       this.taskDescription = taskDescription;
    }

    public void setStatus(int index) {
        curStatus = taskStatuses[index];
    }

    public String getCurStatus() {
        return curStatus;
    }
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", curStatus='" + curStatus + '\'' +
               "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId
                && Objects.equals(taskName, task.taskName)
                && Objects.equals(taskDescription, task.taskDescription)
                && Objects.equals(curStatus, task.curStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskId, curStatus);
    }
}
