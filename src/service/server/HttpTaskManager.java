package service.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exception.MethodExecutionException;
import model.Epic;
import model.Subtask;
import model.Task;
import service.InMemoryTaskManager;
import service.Managers;
import service.StatusManager;
import service.TaskManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class HttpTaskManager extends InMemoryTaskManager implements TaskManager  {

    private static final String KEY_TASKS = "tasks";
    private static final String KEY_EPICS = "epics";
    private static final String KEY_SUBTASKS = "subtasks";
    private static final String KEY_HISTORY = "history";
    private static final String KEY_PRIORITIZED_TASKS = "allTasks";

    private final KVTaskClient client;
    private static final Gson gson = Managers.getDefaultGson();


    public HttpTaskManager(String url) {
        client = new KVTaskClient(url);
    }

    public KVTaskClient getClient() {
        return client;
    }

    public void save() {
        tryToPutOnServer(client, gson.toJson(tasks.values()), KEY_TASKS);
        tryToPutOnServer(client, gson.toJson(epics.values()), KEY_EPICS);
        tryToPutOnServer(client, gson.toJson(subtasks.values()), KEY_SUBTASKS);
        try {
            tryToPutOnServer(client, gson.toJson(getHistory()), KEY_HISTORY );
            tryToPutOnServer(client, gson.toJson(getPrioritizedTasks()), KEY_PRIORITIZED_TASKS);
        } catch (MethodExecutionException e) {
            System.out.println("Список истории пока / приоритетных задач - пока пустые");
        }
    }

    public static HttpTaskManager loadFromServer(String url) {
        HttpTaskManager newTaskManager = new HttpTaskManager(url);
        ArrayList<Task> historyList = new ArrayList<>();
        try {
            historyList = gson.fromJson(newTaskManager.getInfoFromServer(newTaskManager, KEY_HISTORY)
                    ,new TypeToken<ArrayList<Task>>() {}.getType());
        } catch (Exception e) {
            System.out.println("Список истории - пуст");
        }
        ArrayList<Task> allTasks = newTaskManager.getAllTasks(newTaskManager);
        if (historyList.isEmpty() && allTasks.isEmpty()) {
            return newTaskManager;
        }
        List<Task> sortedList = allTasks.stream().sorted(Comparator.comparingInt(Task::getId))
                .collect(Collectors.toList());
        for (Task task : sortedList) {
            switch (task.getType()) {
                case "TASK" : {
                    newTaskManager.addNewTaskItem(task);
                    break;
                }
                case "EPIC" : {
                    Epic epic = (Epic)task;
                    newTaskManager.addNewEpicItem(epic);
                    break;
                }
                case "SUBTASK" : {
                    Subtask subtask = (Subtask) task;
                    newTaskManager.addNewSubtaskItem(subtask);
                    break;
                }
                default: {
                    System.out.println("Неизвестный тип");
                }
            }
        }
        for (Task task : historyList) {
            newTaskManager.historyManager.add(task);
        }

        return newTaskManager;
    }

    private ArrayList<Task> getAllTasks (HttpTaskManager newTaskManager) {
        ArrayList<Task> tasks = tryToGetTasks(newTaskManager);
        ArrayList<Epic> epics = tryToGetEpics(newTaskManager);
        ArrayList<Subtask> subtasks = tryToGetSubtasks(newTaskManager);

        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks);
        allTasks.addAll(epics);
        allTasks.addAll(subtasks);
        return allTasks;
    }

    private ArrayList<Subtask> tryToGetSubtasks (HttpTaskManager newTaskManager) {
        try {
            return gson.fromJson(newTaskManager.getInfoFromServer(newTaskManager, KEY_SUBTASKS)
                    ,new TypeToken<ArrayList<Subtask>>() {}.getType());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ArrayList<Epic> tryToGetEpics(HttpTaskManager newTaskManager) {
        try {
            return gson.fromJson(newTaskManager.getInfoFromServer(newTaskManager, KEY_EPICS)
                    ,new TypeToken<ArrayList<Epic>>() {}.getType());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private ArrayList<Task> tryToGetTasks(HttpTaskManager newTaskManager) {
        try {
            return gson.fromJson(newTaskManager.getInfoFromServer(newTaskManager, KEY_TASKS)
                    ,new TypeToken<ArrayList<Task>>() {}.getType());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String getInfoFromServer(HttpTaskManager taskManager, String key) {
        try {
            return taskManager.getClient().load(key);
        } catch (Exception e) {
            System.out.println("Поле - пустое!");
        }
        return "";
    }

    private void tryToPutOnServer(KVTaskClient client, String value,String key ) {
        try {
            client.put(key, value);
        } catch (Exception e) {
            System.out.println("Не получилось положить значение на сервер");
        }
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
