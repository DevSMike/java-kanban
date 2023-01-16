package model;

import service.StatusManager;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm:ss");

    protected int id;

    protected String name;

    protected String description;

    protected  String status = "";

    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    protected Duration duration;


    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
       this.name = name;
       this.description = description;
       this.startTime = startTime;
       this.duration = duration;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(int id, String name, String status, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String name, String status, String description) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setStatus(StatusManager.Statuses id) {
        status = StatusManager.getStatusStringById(id);
    }
    public StatusManager.Statuses getStatus() {
            if (!status.equals(""))
                 return StatusManager.getStatusIdByString(status);
            else return null;
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
        String startTimeString = "";
        if (startTime == null) startTimeString = "none";
        else startTimeString = startTime.toString();
        return "Task{" +
                "taskName='" + name + '\'' +
                ", taskDescription='" + description + '\'' +
                ", status='" + status + '\'' + ", startTime='" + startTimeString  +'\'' +
               "}\n";
    }

    public String getInfoWithLocalData() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + ","
                + getDescription() + "," + getDuration()+ ","
                + getStartTime().format(DATE_TIME_FORMATTER) + "," + getEndTime().format(DATE_TIME_FORMATTER) + ","
                + getStringEpicId() +"\n";
    }

    public String getInfoWithoutLocalData() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + ","
                + getDescription() + "," + getStringEpicId() + "\n";
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

    public long getInstantStartTime() {
        if (startTime != null)
            return ZonedDateTime.of(startTime, ZoneId.of("Europe/Moscow")).toEpochSecond();
        else
            return 0L;
    }

    public long getInstantEndTime() {
        if (startTime != null)
            return ZonedDateTime.of(endTime, ZoneId.of("Europe/Moscow")).toEpochSecond();
        else
            return 0L;
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    public Integer getEpicId () {
        return -1;
    }

    public String getStringEpicId() {
        return "";
    }
    public String getType () {
        return "TASK";
    }


}
