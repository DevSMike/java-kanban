package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements  HistoryManager {

    private static final int MAX_TASKS_IN_HISTORY = 10;

    protected ArrayList<Task> tasksHistory = new ArrayList<>();
    private  ArrayList<Integer> viewedTasks = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return  tasksHistory;
    }

    @Override
    public void add(Task task) {
        if (!viewedTasks.contains(task.getId())) {
            checkTasksHistory(tasksHistory);
            tasksHistory.add(task);
            viewedTasks.add(task.getId());
        }
    }
    private void checkTasksHistory(ArrayList<Task> tasksHistory) {
        int firstId = 0;
        if (tasksHistory.size() >= MAX_TASKS_IN_HISTORY) {
            while (tasksHistory.size() != MAX_TASKS_IN_HISTORY - 1) {
                tasksHistory.remove(firstId);
            }
        }
    }



}
