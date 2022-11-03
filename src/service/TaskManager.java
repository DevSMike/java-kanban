package service;
import model.Epic;
import model.Task;
import model.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    protected HashMap<Integer, Task> taskItems = new HashMap<>();
    protected HashMap<Integer, Epic> epicItems = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskItems = new HashMap<>();
    protected int nextId = 1;

    public void addNewTaskItem(Task task) {
        task.setTaskId(nextId++);
        task.setStatus(0);
        taskItems.put(task.getTaskId(), task);
    }

    public void addNewEpicItem(Epic epic) {
        epic.setTaskId(nextId++);
        epicItems.put(epic.getTaskId(), epic);
        syncEpicCollection(epic);
    }

    public void addNewSubtaskItem(Subtask subtask) {
        subtask.setTaskId(nextId++);
        subtask.setStatus(0);
        subtaskItems.put(subtask.getTaskId(), subtask);
    }

    public HashMap<Integer,Task> printAllTasks() {
       return taskItems;
    }

    public HashMap<Integer,Epic> printAllEpics() {
        return epicItems;
    }

    public HashMap<Integer, Subtask> printAllSubtasks() {
        return subtaskItems;
    }

    public void deleteAllTasks() {
        taskItems.clear();
    }

    public void deleteAllEpics() {
        epicItems.clear();
    }

    public void deleteAllSubtasks() {
        subtaskItems.clear();
    }

    public Task getTaskById(int id) {
        for (int taskId : taskItems.keySet()) {
            if (taskId == id)
                return taskItems.get(taskId);
        }
        return null;
    }

    public Epic getEpicById(int id) {
        for (int epicId : epicItems.keySet()) {
            if (epicId == id)
                return epicItems.get(epicId);
        }
        return null;
    }

    public Subtask getSubtaskById(int id) {
        for (int subtaskId : subtaskItems.keySet()) {
            if (subtaskId == id)
                return subtaskItems.get(subtaskId);
        }
        return null;
    }
    private void syncEpicCollection(Epic epic) {
        boolean isNewStatus = false;
        int newCount = 0;
        boolean isDoneStatus = false;
        int doneCount = 0;
        for (int itemId : epic.getItems()) {
            Subtask subtask = subtaskItems.get(itemId);
            if (isNewStatus = subtask.getCurStatus() == "NEW") { newCount++; }
            if (isDoneStatus = subtask.getCurStatus() == "DONE") { doneCount++; }
            subtask.setEpicId(epic.getTaskId());
        }
        if (newCount == epic.getItems().size()) {
            epic.setStatus(0);
        } else if (doneCount == epic.getItems().size()) {
            epic.setStatus(2);
        } else { epic.setStatus(1); }
    }
    public void deleteTaskById(int id) {
        for (int taskId : taskItems.keySet()) {
            if (taskId == id) {
                taskItems.remove(taskId);
                break;
            }
        }
    }

    public void deleteEpicById(int id) {
        for (int epicId : epicItems.keySet()) {
            if (epicId == id) {
                epicItems.remove(epicId);
                break;
            }
        }
    }

    public void deleteSubtaskById(int id) {
        for (int subtaskId : subtaskItems.keySet()) {
            if (subtaskId == id) {
                subtaskItems.remove(subtaskId);
                break;
            }
        }
    }

    public ArrayList<Subtask> getSubtaskForEpic(Epic epic) {
        ArrayList<Subtask> subtaskForEpic = new ArrayList<>();
        for (Subtask subtask : subtaskItems.values()) {
            if (subtask.getEpicId() == epic.getTaskId()) {
                subtaskForEpic.add(subtask);
            }
        }
        return subtaskForEpic;
    }

    public void updateTask(Task task, int taskId) {
        taskItems.put(taskId, task);
    }

    public void updateEpic(Epic epic, int epicID) {
        epicItems.put(epicID, epic);
        syncEpicCollection(epic);
    }

    public void updateSubtask(Subtask subtask, int subId) {
        subtaskItems.put(subId, subtask);
        Epic epic = epicItems.get(subtask.getEpicId());
        syncEpicCollection(epic);
    }

    public void setTaskStatus(Task.Statuses status, Task task) {
        taskItems.get(task.getTaskId()).setStatus(getStatusId(status));
    }

    public void setSubtaskStatus(Task.Statuses status, Subtask subtask) {
        subtaskItems.get(subtask.getTaskId()).setStatus(getStatusId(status));;
        Epic epic = epicItems.get(subtask.getEpicId());
        syncEpicCollection(epic);
    }


    private int getStatusId(Task.Statuses status) {
        switch(status) {
            case NEW:
                return 0;
            case IN_PROGRESS:
                return 1;
            case DONE:
                return 2;
            default: return -1;
        }
    }

}
