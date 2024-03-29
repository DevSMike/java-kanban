package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FileBackedTasksManager  extends InMemoryTaskManager implements TaskManager {

    private static final int ELEMENTS_IN_TASK_WITH_TIME = 5;
    private  static final int ELEMENTS_IN_SUBTASK_WITH_TIME = 6;
    private final File taskData;


    public FileBackedTasksManager(File file)  {
        if (!file.exists()) {
            try {
                 file.createNewFile();
           } catch (IOException e) {
                throw new FileCreationException("Ошибка создания нового файла при создании FileBackedManager");
            }
        }
        taskData = file;
    }



    public void save()  {
        try (FileWriter file = new FileWriter(taskData, false)) {
            file.write(allTasksToString());
            file.write(historyToString());
        } catch (IOException e) {
            throw new ManagerSaveException("Произошло исключение в методе save()");
        }
    }

    private String allTasksToString () {
        StringBuilder taskString = new StringBuilder("id,type,name,status,description,duration,startTime,endTime,epic" +
                "\n");
        int collectionsLength = tasks.size() + epics.size() + subtasks.size();
        for (int i = 1; i <= collectionsLength; i++) {
            if (tasks.containsKey(i)) {
                taskString.append(taskToString(tasks.get(i)));
            } else if (epics.containsKey(i)) {
                taskString.append(taskToString(epics.get(i)));
            } else if (subtasks.containsKey(i)) {
                taskString.append(taskToString(subtasks.get(i)));
            } else {
                System.out.println("Ошибочный id");
            }
        }
        return taskString + "\n";
    }

    private String taskToString (Task task) {
        if (task.getStartTime() == null) {
            return task.getInfoWithoutLocalData();
        }
        return task.getInfoWithLocalData();

    }


    private String historyToString() {
        StringBuilder historyString = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            historyString.append(task.getId()).append(",");
        }
        return historyString.toString();
    }

    private static String readDataFromFile(File file) {
        try {
            return Files.readString(Path.of(file.toURI()));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл");
            return null;
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        String data = readDataFromFile(file);
        if (data == null) {
            throw new IOException("Данных в файле нет");
        }
        String[] lines = data.split("\\n");
        if (lines.length <= 1) {
            throw new IOException("Данных в файле нет");
        }
        for (int i = 1; i < lines.length; i++) {
            String[] items = lines[i].split(",");
            if (lines[i].isBlank() && i!=lines.length -1)  {
                manager.historyManager.getHistory().addAll(historyFromString(lines[i+1], manager));
                break;
            }
            fromString(items, manager);
        }

        return manager;
    }

    private  static List<Task> historyFromString(String value, FileBackedTasksManager manager) {
        String[] params = value.split(",");
        for (String param : params) {
            int id = Integer.parseInt(param);
            if (manager.tasks.containsKey(id)) {
                manager.historyManager.add(manager.tasks.get(id));
            } else if (manager.epics.containsKey(id)) {
                manager.historyManager.add(manager.epics.get(id));
            } else if (manager.subtasks.containsKey(id)) {
                manager.historyManager.add(manager.subtasks.get(id));
            }
        }
        return manager.historyManager.getHistory();
    }

    private static void fromString(String[] params, FileBackedTasksManager manager) {
        switch (params[1]) {
            case "TASK":
                Task task = createTaskFromString(params);
                manager.addNewTaskItem(task);
                break;
            case "SUBTASK":
                Subtask subtask = createSubtaskFromString(params);
                manager.addNewSubtaskItem(subtask);
                break;
            case "EPIC":
                Epic epic = new Epic(Integer.parseInt(params[0]), params[2], params[3], params[4]);
                manager.addNewEpicItem(epic);
                break;
            default:
                System.out.println("Такого таска не существует!");
        }
    }

    private static Task createTaskFromString(String[] params) {
        Task task;
        if (params.length > ELEMENTS_IN_TASK_WITH_TIME) {
            task = new Task(Integer.parseInt(params[0]), params[2], params[3], params[4]
                    , LocalDateTime.parse(params[6], DATE_TIME_FORMATTER)
                    , Duration.parse(params[5]));

        } else {
            task = new Task(Integer.parseInt(params[0]), params[2], params[3], params[4]);
        }
        return task;
    }

    private static Subtask createSubtaskFromString(String[] params) {
          Subtask subtask;
          if (params.length > ELEMENTS_IN_SUBTASK_WITH_TIME) {
              subtask = new Subtask(Integer.parseInt(params[0]), params[2], params[3], params[4]
                      ,LocalDateTime.parse(params[6], DATE_TIME_FORMATTER)
                      ,Duration.parse(params[5]),Integer.parseInt(params[8].trim()));
          } else {
              subtask = new Subtask(Integer.parseInt(params[0]), params[2], params[3], params[4]
                      ,Integer.parseInt(params[8].trim()));
          }
          return subtask;
    }
    @Override
    public void addNewTaskItem(Task task) {
        super.addNewTaskItem(task);
        save();
    }

    @Override
    public void addNewEpicItem(Epic epic) {
        super.addNewEpicItem(epic);
        save();
    }

    @Override
    public void addNewSubtaskItem(Subtask subtask) {
        super.addNewSubtaskItem(subtask);
        save();
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> list = super.getTasks();
        save();
        return list;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> list = super.getEpics();
        save();
        return list;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> list = super.getSubtasks();
        save();
        return list;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateEpicInfo(Epic epic) {
        super.updateEpicInfo(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtaskForEpic(Epic epic) {
        ArrayList<Subtask> list = super.getSubtaskForEpic(epic);
        save();
        return list;
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
       return super.getPrioritizedTasks();
    }
}

class ManagerSaveException extends RuntimeException {
    public ManagerSaveException (String message) {
        super(message);
    }
}

class FileCreationException extends RuntimeException {
    public FileCreationException (String message) {
        super(message);
    }
}