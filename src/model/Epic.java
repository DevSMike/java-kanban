package model;
import java.util.ArrayList;

public class Epic extends Task {
    // Для хранения индексов сабтасков
    protected   ArrayList<Integer> items = new ArrayList<>();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public void setItems(int itemId) {
        items.add(itemId);
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public int getIemByIndex(int i) {
        return items.get(i);
    }
}
