package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    protected int epicId;

    public Subtask (int id, String name, String status, String description, LocalDateTime startTime
            , Duration duration, int epicId) {
        super(id, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, startTime, duration);
    }

    public Subtask (int id, String name, String description,String status, int epicId) {
        super(id, name, status, description);
        this.epicId = epicId;
    }


    @Override
    public Integer getEpicId() {
        return epicId;
    }
    @Override
    public String getStringEpicId() {
        return Integer.valueOf(epicId).toString();
    }
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String startTimeString = "";
        if (startTime == null) startTimeString = "none";
        else startTimeString = startTime.toString();

        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' + ", startTime='" + startTimeString  +'\'' +
                "}\n";
    }

    @Override
    public String getType() {
        return "SUBTASK";
    }
 }
