package model;

public class Subtask extends Task {

    protected int epicId;

    public Subtask(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
