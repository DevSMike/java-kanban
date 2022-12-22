package service;
import model.Epic;
import model.Task;
import model.Subtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    protected  HistoryManager historyManager = Managers.getDefaultHistory();
    protected int nextId = 1;

    @Override
    public void addNewTaskItem(Task task) {
        task.setId(nextId++);
        task.setStatus(StatusManager.Statuses.NEW);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addNewEpicItem(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void addNewSubtaskItem(Subtask subtask) {
        subtask.setId(nextId++);
        subtask.setStatus(StatusManager.Statuses.NEW);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.setSubtaskIds(subtask.getId());
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteSubtaskIds();
        }
    }
    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return  subtasks.get(id);
    }
    @Override
    public void updateEpicStatus(Epic epic) {
        boolean isNewStatus = false;
        int newCount = 0;
        boolean isDoneStatus = false;
        int doneCount = 0;
        for (int itemId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(itemId);
            if (isNewStatus = subtask.getStatus() == StatusManager.Statuses.NEW) { newCount++; }
            if (isDoneStatus = subtask.getStatus() == StatusManager.Statuses.DONE) { doneCount++; }
        }
        if (newCount == epic.getSubtaskIds().size()) {
            epic.setStatus(StatusManager.Statuses.NEW);
        } else if (doneCount == epic.getSubtaskIds().size()) {
            epic.setStatus(StatusManager.Statuses.DONE);
        } else { epic.setStatus(StatusManager.Statuses.IN_PROGRESS); }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        for (int ids : epics.get(id).getSubtaskIds()) {
            subtasks.remove(ids);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        subtasks.remove(id);
        historyManager.remove(id);
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<Subtask> getSubtaskForEpic(Epic epic) {
        ArrayList<Subtask> subtaskForEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                subtaskForEpic.add(subtask);
            }
        }
        return subtaskForEpic;
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
           return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId()) && !epics.containsKey(subtask.getEpicId())) {
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    @Override
    public void setTaskStatus(StatusManager.Statuses status, Task task) {
        tasks.get(task.getId()).setStatus(status);
    }

    @Override
    public void setSubtaskStatus(StatusManager.Statuses status, Subtask subtask) {
        subtasks.get(subtask.getId()).setStatus(status);;
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
