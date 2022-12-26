package model;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public Subtask (int id, String name, String status, String description, int epicId) {
        super(id, name, status, description);
        this.epicId = epicId;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}\n";
    }

    @Override
    public String getType() {
        return "SUBTASK";
    }
 }
