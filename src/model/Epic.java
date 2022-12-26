package model;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    // Для хранения индексов сабтасков
    protected  ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public Epic (int id, String name, String status, String description) {
        super(id, name, status, description);
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
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}\n";
    }

    @Override
    public String getType () {
        return "EPIC";
    }
}
