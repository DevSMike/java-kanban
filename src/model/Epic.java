package model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    // Для хранения индексов сабтасков
    protected  ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public Epic (int id, String name, String status, String description) {
        super(id, name,status, description);
    }

    public void setSubtaskIds(int itemId) {
        subtaskIds.add(itemId);
    }

    public void updateSubtaskIds(Epic o) {
        this.subtaskIds = o.subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public int getIemByIndex(int i) {
        return subtaskIds.get(i);
    }

    public void deleteSubtaskIds() {
        subtaskIds.clear();
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
}
