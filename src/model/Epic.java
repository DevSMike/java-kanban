package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private LocalDateTime endTime;

    protected  ArrayList<Integer> subtaskIds;


    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
        subtaskIds = new ArrayList<>();
    }

    public Epic (int id, String name, String status, String description) {
        super(id, name,status, description);
        subtaskIds = new ArrayList<>();
    }

    public void setSubtaskIds(int itemId) {
        subtaskIds.add(itemId);
    }

    public void updateSubtaskIds(Epic o) {
        this.subtaskIds = o.subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {
        if (subtaskIds != null)
             return subtaskIds;
        else return new ArrayList<>();
    }

    public int getIemByIndex(int i) {
        return subtaskIds.get(i);
    }

    public void deleteSubtaskIds() {
        try {
            subtaskIds.clear();
        } catch (NullPointerException e) {
            System.out.println("Невозможно очистить сабтаски");
        }
    }

    public void deleteSubtaskIdInList(int id) {
        for (int i = 0; i < subtaskIds.size(); i++) {
            if (subtaskIds.get(i) == id) {
                subtaskIds.remove(i);
                return;
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        String startTimeString = "";
        if (startTime == null) startTimeString = "none";
        else startTimeString = startTime.toString();
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\''  +  ", startTime='" + startTimeString  +'\'' +
                "}\n";
    }


    @Override
    public String getType () {
        return "EPIC";
    }

    public void setEndTime(LocalDateTime endTime) {
       this.endTime = endTime;
    }
    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
