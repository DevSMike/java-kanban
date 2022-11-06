package service;
import model.Epic;
import model.Task;
import model.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected int nextId = 1;

    public void addNewTaskItem(Task task) {
        task.setId(nextId++);
        task.setStatus(StatusManager.Statuses.NEW);
        tasks.put(task.getId(), task);
    }

    public void addNewEpicItem(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        syncEpicCollection(epic);
    }

    public void addNewSubtaskItem(Subtask subtask) {
        subtask.setId(nextId++);
        subtask.setStatus(StatusManager.Statuses.NEW);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.setSubtaskIds(subtask.getId());
        syncEpicCollection(epic);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtasks.values());
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteSubtaskIds();
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return  subtasks.get(id);
    }
    private void syncEpicCollection(Epic epic) {
        boolean isNewStatus = false;
        int newCount = 0;
        boolean isDoneStatus = false;
        int doneCount = 0;
        for (int itemId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(itemId);
            if (isNewStatus = subtask.getStatus() == "NEW") { newCount++; }
            if (isDoneStatus = subtask.getStatus() == "DONE") { doneCount++; }
        }
        if (newCount == epic.getSubtaskIds().size()) {
            epic.setStatus(StatusManager.Statuses.NEW);
        } else if (doneCount == epic.getSubtaskIds().size()) {
            epic.setStatus(StatusManager.Statuses.DONE);
        } else { epic.setStatus(StatusManager.Statuses.IN_PROGRESS); }
    }
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        for (int ids : epics.get(id).getSubtaskIds()) {
            subtasks.remove(ids);
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        subtasks.remove(id);
        syncEpicCollection(epic);
    }

    public ArrayList<Subtask> getSubtaskForEpic(Epic epic) {
        ArrayList<Subtask> subtaskForEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                subtaskForEpic.add(subtask);
            }
        }
        return subtaskForEpic;
    }

    public void updateTask(Task oldTask, Task newTask) {
        if (tasks.containsKey(oldTask.getId())) {
            newTask.setId(oldTask.getId());
            tasks.put(newTask.getId(), newTask);
        } else System.out.println("Задачи с введенным ID Нет!");

    }

    public void updateEpic(Epic oldEpic, Epic newEpic) {
        if (epics.containsKey(oldEpic.getId())) {
            newEpic.setId(oldEpic.getId());
            epics.put(newEpic.getId(), newEpic);
            if (oldEpic.getSubtaskIds() != null) {
                for (int id : oldEpic.getSubtaskIds()) {
                    newEpic.setSubtaskIds(id);
                }
            }
            syncEpicCollection(newEpic);
        } else System.out.println("Эпика с введенным ID Нет!");
    }

    public void updateSubtask(Subtask oldSubtask, Subtask newSubtask) {
        if (subtasks.containsKey(oldSubtask.getId())) {
            newSubtask.setId(oldSubtask.getId());
            newSubtask.setEpicId(oldSubtask.getEpicId());
            subtasks.put(newSubtask.getId(), newSubtask);
            Epic epic = epics.get(oldSubtask.getEpicId());
            syncEpicCollection(epic);
        } else System.out.println("Сабтаска с введенным ID нет!");
    }

    public void setTaskStatus(StatusManager.Statuses status, Task task) {
        tasks.get(task.getId()).setStatus(status);
    }

    public void setSubtaskStatus(StatusManager.Statuses status, Subtask subtask) {
        subtasks.get(subtask.getId()).setStatus(status);;
        Epic epic = epics.get(subtask.getEpicId());
        syncEpicCollection(epic);
    }
}
