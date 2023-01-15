package service;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager  {

    void add(Task task);

    ArrayList<Task> getHistory();

    void remove(int id);

    boolean isContainsId(int id);

}
