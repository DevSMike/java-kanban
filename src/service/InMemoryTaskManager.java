package service;
import exception.MethodExecutionException;
import exception.TaskCreationException;
import model.Epic;
import model.Task;
import model.Subtask;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected static final DateTimeFormatter DATE_TIME_FORMATTER = Task.DATE_TIME_FORMATTER;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime
            ,Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int nextId = 1;

    protected void validateLocalDataInTasks(Task checkTask) {
        if (prioritizedTasks.isEmpty()) {
            return;
        }
        for (Task task : prioritizedTasks) {
            if (checkTask.getInstantStartTime() >= task.getInstantStartTime()
                    && checkTask.getInstantEndTime() <= task.getInstantEndTime() && checkTask.getStartTime() != null) {
                throw new TaskCreationException("Задача:" + checkTask + " попадает в занятый интервал");
            }
        }
    }

    protected void calculateEpicTime(Epic epic) {
        long epicDuration = 0;
        LocalDateTime minStartTime = LocalDateTime.now();
        LocalDateTime maxEndTime = LocalDateTime.now();
        for (int id: epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(id);
            if (id == epic.getSubtaskIds().get(0)) {
                minStartTime = subtask.getStartTime();
                maxEndTime = subtask.getEndTime();
            }
            epicDuration += Duration.ofMinutes(subtask.getDuration().toMinutes()).toMinutes();
            if (subtask.getStartTime().isBefore(minStartTime))
                minStartTime = subtask.getStartTime();
            if (subtask.getEndTime().isAfter(maxEndTime))
                maxEndTime = subtask.getEndTime();
        }
        epic.setStartTime(minStartTime);
        epic.setEndTime(maxEndTime);
        epic.setDuration(Duration.ofMinutes(epicDuration));
    }

    private boolean tryToCreateTask(Task task) {
        try {
            validateLocalDataInTasks(task);
        } catch (TaskCreationException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isMapsCreated() {
        return tasks.isEmpty() && subtasks.isEmpty() && epics.isEmpty();
    }
    @Override
    public void addNewTaskItem(Task task) {
        if (!tryToCreateTask(task)) {
            return;
        }
        prioritizedTasks.add(task);
        task.setId(nextId++);
        task.setStatus(StatusManager.Statuses.NEW);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addNewEpicItem(Epic epic) {
        if (!tryToCreateTask(epic)) {
            return;
        }
        epic.setId(nextId++);
        epic.setStatus(StatusManager.Statuses.NONE);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addNewSubtaskItem(Subtask subtask) {
        if (!tryToCreateTask(subtask)) {
            return;
        }
        prioritizedTasks.add(subtask);
        subtask.setId(nextId++);
        subtask.setStatus(StatusManager.Statuses.NEW);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.setSubtaskIds(subtask.getId());
        updateEpicInfo(epic);
    }

    @Override
    public ArrayList<Task> getTasks() {
        if (!tasks.isEmpty())
            return new ArrayList<>(tasks.values());
        else throw new MethodExecutionException("Ошибка выполнения метода getTasks");
    }

    @Override
    public ArrayList<Epic> getEpics() {
        if (!epics.isEmpty())
            return new ArrayList<>(epics.values());
        else throw new MethodExecutionException("Ошибка выполнения метода getEpics");
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        if (!subtasks.isEmpty())
            return new ArrayList<>(subtasks.values());
        else throw new MethodExecutionException("Ошибка выполнения метода getSubtasks");
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
        prioritizedTasks.removeAll(tasks.values());
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
        prioritizedTasks.removeAll(epics.values());
        prioritizedTasks.removeAll(subtasks.values());
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        prioritizedTasks.removeAll(subtasks.values());
        for (Epic epic : epics.values()) {
            epic.deleteSubtaskIds();
        }
    }
    @Override
    public Task getTaskById(int id) {
        if (tasks.isEmpty())
            throw new MethodExecutionException("Невозможно получить task by id");
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.isEmpty())
            throw new MethodExecutionException("Невозможно получить epic by id");
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.isEmpty())
            throw new MethodExecutionException("Невозможно получить subtask by id");
        historyManager.add(subtasks.get(id));
        return  subtasks.get(id);
    }
    @Override
    public void updateEpicInfo(Epic epic) {
        if (epic.getSubtaskIds().isEmpty())  {
            epic.setStatus(StatusManager.Statuses.NONE);
            return;
        }
        boolean isNewStatus = false;
        int newCount = 0;
        boolean isDoneStatus = false;
        int doneCount = 0;
        if (epic.getSubtaskIds().isEmpty())
            return;
        for (int itemId : epic.getSubtaskIds()) {

            Subtask subtask = subtasks.get(itemId);
            if (subtask == null) {
                throw new MethodExecutionException("Subtask у Epic равен null");
            }
            if (isNewStatus = subtask.getStatus() == StatusManager.Statuses.NEW) { newCount++; }
            if (isDoneStatus = subtask.getStatus() == StatusManager.Statuses.DONE) { doneCount++; }
        }

        if (newCount == epic.getSubtaskIds().size()) {
            epic.setStatus(StatusManager.Statuses.NEW);
        } else if (doneCount == epic.getSubtaskIds().size()) {
            epic.setStatus(StatusManager.Statuses.DONE);
        } else { epic.setStatus(StatusManager.Statuses.IN_PROGRESS); }

        calculateEpicTime(epic);
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.isEmpty())
            throw new MethodExecutionException("Невозможно удалить task by id");
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.isEmpty())
            throw new MethodExecutionException("Невозможно удалить epic by id");

        for (int ids : epics.get(id).getSubtaskIds()) {
            prioritizedTasks.remove(subtasks.get(ids));
            subtasks.remove(ids);
        }
        epics.get(id).deleteSubtaskIds();
        updateEpicInfo(epics.get(id));
        epics.remove(id);
        if (historyManager.isContainsId(id))
             historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.isEmpty())
            throw new MethodExecutionException("Невозможно удалить subtask by id");
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.deleteSubtaskIdInList(subtasks.get(id).getId());
        prioritizedTasks.remove(subtasks.get(id));
        subtasks.remove(id);
        if (historyManager.isContainsId(id))
            historyManager.remove(id);
        updateEpicInfo(epic);
    }

    @Override
    public ArrayList<Subtask> getSubtaskForEpic(Epic epic) {
        if (epic.getSubtaskIds().isEmpty())
            throw new MethodExecutionException("У данного эпика: " + epic + " нет сабтасков");
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
        if (!tryToCreateTask(task))
            return;
        if (task.getStatus() == null)
            task.setStatus(StatusManager.Statuses.NEW);
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        if (!tryToCreateTask(epic))
            return;
        prioritizedTasks.remove(epics.get(epic.getId()));
        epics.put(epic.getId(), epic);
        prioritizedTasks.add(epic);
       // updateEpicInfo(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId()) && !epics.containsKey(subtask.getEpicId())) {
            return;
        }
        subtask.setEpicId(subtasks.get(subtask.getId()).getEpicId());
        if (!tryToCreateTask(subtask))
            return;
        if ( subtask.getStatus() == null )
            subtask.setStatus(StatusManager.Statuses.NEW);
        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        subtasks.put(subtask.getId(), subtask);
        prioritizedTasks.add(subtask);
        updateEpicInfo(epics.get(subtask.getEpicId()));
    }

    @Override
    public void setTaskStatus(StatusManager.Statuses status, Task task) {
          if (tasks.isEmpty())
            throw new MethodExecutionException("Невозможно задать статус таску");
        tasks.get(task.getId()).setStatus(status);
    }

    @Override
    public void setSubtaskStatus(StatusManager.Statuses status, Subtask subtask) {
        if (subtasks.isEmpty())
            throw new MethodExecutionException("Невозможно задать статус сабтаску");
        subtasks.get(subtask.getId()).setStatus(status);;
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicInfo(epic);
    }

    @Override
    public List<Task> getHistory() {
        if (historyManager.getHistory().isEmpty())
            throw new MethodExecutionException("Ошибка в выводе истории просмотра");
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        if (tasks.isEmpty() && subtasks.isEmpty() && epics.isEmpty())
            throw new MethodExecutionException("Ошибка в выводе приоритета задач");
       return prioritizedTasks;
    }
}

