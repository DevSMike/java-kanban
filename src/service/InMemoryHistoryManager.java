package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements  HistoryManager {

    private static final int MAX_TASKS_IN_HISTORY = 10;

    private static final int FIRST_LIST_INDEX = 0;

    private final ArrayList<Task> tasksHistory = new ArrayList<>();

    // данный список я использовал, чтобы избавиться от дубликатов просмотренных задач
    // у ребят уточнил, что пока этого делать ненужно, закомментирую его
    // private  ArrayList<Integer> viewedTasks = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return  tasksHistory;
    }

    @Override
    public void add(Task task) {
        if (tasksHistory.size() >= MAX_TASKS_IN_HISTORY) {
            tasksHistory.remove(FIRST_LIST_INDEX);
        }
        tasksHistory.add(task);
        //viewedTasks.add(task.getId());
    }
}
