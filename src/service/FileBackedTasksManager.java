package service;

import model.Epic;
import model.Subtask;
import model.Task;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager  extends InMemoryTaskManager implements TaskManager {
    private static final String PATH = "src/resources/tasksData.csv";
    private final List<Integer> allIds = new ArrayList<>();

    enum TaskNames {
        TASK,
        EPIC,
        SUBTASK;
    }

    public FileBackedTasksManager() {
        isCanReadDataFile();
    }

    public void save() {
        try ( FileWriter file = new FileWriter(PATH, false)) {
            file.write(allTasksToString());
            file.write(historyToString());
        } catch (IOException e) {
            System.out.println("Ошибка в методе save()");
        }
    }
    private String allTasksToString () {
        StringBuilder taskString = new StringBuilder("id,type,name,status,description,epic\n");
        for (int id : allIds) {
            if (tasks.containsKey(id)) {
                String[] params = tasks.get(id).toStringForFile().split("\b");
                taskString.append(params[0]).append(",").append(TaskNames.TASK).append(",")
                        .append(params[1]).append(",").append(params[2]).append(",").append(params[3]).append(",")
                        .append("\n");
            } else if (epics.containsKey(id)) {
                String[] params = epics.get(id).toStringForFile().split("\b");
                taskString.append(params[0]).append(",").append(TaskNames.EPIC).append(",").append(params[1]).append(",")
                        .append(params[2]).append(",").append(params[3]).append(",").append("\n");
            } else if (subtasks.containsKey(id)) {
                String[] params = subtasks.get(id).toStringForFile().split("\b");
                taskString.append(params[0]).append(",").append(TaskNames.SUBTASK).append(",").append(params[1])
                        .append(",").append(params[2]).append(",").append(params[3]).append(",").append(params[4]).append(",").append("\n");
            } else {
                System.out.println("Ошибочный id");
            }
        }

        return taskString + "\n";
    }

    private String historyToString() {
        StringBuilder historyString = new StringBuilder("");
        for (Task task : historyManager.getHistory()) {
            historyString.append(task.getId()).append(",");
        }
        return historyString.toString();
    }
    private String readDataFromFile () {
        try {
            return Files.readString(Path.of(PATH));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл");
            return null;
        }
    }
    private void isCanReadDataFile() {
        String data = readDataFromFile();
        if (data == null) { return; }
        String[] lines = data.split("\\n");
        if (lines.length <= 1) { return; }
        for (int i = 1; i < lines.length; i++) {
            String[] items = lines[i].split(",");
            if (lines[i].isBlank())  {
                if (i != lines.length-1) {
                    historyManager.getHistory().addAll(historyFromString(lines[i+1]));
                }
                break;
            }
            if (Objects.equals(items[1], TaskNames.TASK.toString())) {
                fromStringTask(items);
            } else if (Objects.equals(items[1],TaskNames.EPIC.toString())) {
                fromStringEpic(items);
            } else if (Objects.equals(items[1],TaskNames.SUBTASK.toString())) {
                fromStringSubtask(items);
            }
        }
    }
    private List<Task> historyFromString(String value) {
        String[] params = value.split(",");
        for (String param : params) {
            int id = Integer.parseInt(param);
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else if (subtasks.containsKey(id)) {
                historyManager.add(subtasks.get(id));
            }
        }
        return historyManager.getHistory();
    }
    private void fromStringEpic (String[] params) {
        Epic epic = new Epic(Integer.parseInt(params[0]), params[2], params[3], params[4]);
        epics.put(epic.getId(), epic);
    }
    private void fromStringTask (String[] params) {
        Task task = new Task(Integer.parseInt(params[0]), params[2], params[3], params[4]);
        tasks.put(task.getId(), task);
    }
    private void fromStringSubtask (String[] params) {
        Subtask subtask = new Subtask(Integer.parseInt(params[0]), params[2], params[3], params[4]
                                      ,Integer.parseInt(params[5].trim()));
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());
    }
    @Override
    public void addNewTaskItem(Task task) {
        super.addNewTaskItem(task);
        allIds.add(task.getId());
        save();
    }

    @Override
    public void addNewEpicItem(Epic epic) {
        super.addNewEpicItem(epic);
        allIds.add(epic.getId());
        save();
    }

    @Override
    public void addNewSubtaskItem(Subtask subtask) {
        super.addNewSubtaskItem(subtask);
        allIds.add(subtask.getId());
        save();
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return super.getEpics();
    }
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            allIds.remove(task.getId());
        }
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            allIds.remove(epic.getId());
        }
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            allIds.remove(subtask.getId());
        }
        super.deleteSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        save();
        return tasks.get(id);

    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        save();
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        save();
        return subtasks.get(id);
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        allIds.remove(id);
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        allIds.remove(id);
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        allIds.remove(id);
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtaskForEpic(Epic epic) {
        return super.getSubtaskForEpic(epic);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void setTaskStatus(StatusManager.Statuses status, Task task) {
       super.setTaskStatus(status, task);
       save();
    }

    @Override
    public void setSubtaskStatus(StatusManager.Statuses status, Subtask subtask) {
        super.setSubtaskStatus(status, subtask);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
