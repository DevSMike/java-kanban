package service.server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import exception.MethodExecutionException;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class HttpTaskServer  {

    final static private int PORT = 8080;
    private static final String URL = "http://localhost:8078/";
    TaskManager tasksManager = Managers.getDefault();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    HttpServer httpServer;
    public HttpTaskServer() throws IOException, InterruptedException {
        try {
            tryLoadManager();
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", this::handle);
            httpServer.start();
            System.out.println("Сервер успешно запущен на порту: " + PORT);
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер");
        }
    }

    //Для тестов
    public TaskManager getTasksManager() {
        return  tasksManager;
    }

    private void tryLoadManager() {
        try {
            tasksManager = HttpTaskManager.loadFromServer(URL);
        } catch (Exception e) {
            System.out.println("Это первый вызов manager");
        }
    }
    
    public void stop() {
        httpServer.stop(0);
    }


    public void handle(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();
        String type = getPathType(path);
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "POST": {
                if (isUriHasQuery(httpExchange)) {
                    writeResponse(httpExchange, 400, "Данный метод не поддерживается!");
                    return;
                }
                postTask(httpExchange, type);
                break;
            }
            case "GET": {
                if (type.isEmpty()) {
                    getPrioritizedTasks(httpExchange);
                    return;
                }
                if (isUriHasQuery(httpExchange)) {
                    if (isUriHasEpicId(path)) {
                        getEpicSubtaskId(httpExchange);
                        return;
                    }
                    getTaskById(httpExchange, type);
                    return;
                }
                if (type.equals("history")){
                    getHistory(httpExchange);
                    return;
                }
                getTasks(httpExchange, type);
                break;
            }
            case "DELETE": {
                if (isUriHasQuery(httpExchange)) {
                    deleteTaskById(httpExchange, type);
                }
                deleteTasks(httpExchange, type);
                break;
            }
        }
    }

    private String getPathType(String path) {
        try {
            return  path.split("/")[2];
        } catch (Exception e) {
            return "";
        }
    }

    private void getHistory(HttpExchange httpExchange) {
        int rCode =200;
        String response;
        try {
            response = gson.toJson(tasksManager.getHistory());
        } catch (MethodExecutionException e) {
            response = "Список истории пуст";
            rCode = 400;
        }
        writeResponse(httpExchange, rCode, response);
    }
    private void getPrioritizedTasks(HttpExchange httpExchange) {
        int rCode = 200;
        String response;
        try {
            response = gson.toJson(tasksManager.getPrioritizedTasks());
        } catch (MethodExecutionException e) {
            response = "Нет списка приоритетных задач!";
            rCode = 400;
        }
        writeResponse(httpExchange, rCode, response);
    }

    private void writeResponse(HttpExchange httpExchange, int rCode, String response) {
        try {
            httpExchange.sendResponseHeaders(rCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            System.out.println("Не получилось открыть поток");
        }
    }

    private void getEpicSubtaskId(HttpExchange httpExchange) {
        URI uri = httpExchange.getRequestURI();
        String path = uri.toString();
        int id = parsedId(path.substring(path.lastIndexOf("=")+1));
        int rCode = 200;
        String response = "";
        try {
            if (tasksManager.getEpics().contains(tasksManager.getEpicById(id))) {
                List<Subtask> subs = tasksManager.getSubtasks().stream()
                        .filter(subtask -> subtask.getEpicId().equals(tasksManager.getEpicById(id).getId()))
                        .collect(Collectors.toList());
                response = gson.toJson(subs);
            }
        } catch ( MethodExecutionException e) {
            rCode = 400;
            response = "Данного эпика не существует";
        }
       writeResponse(httpExchange, rCode, response);
    }

    private boolean isUriHasEpicId(String path) {
        try {
            return path.split("/")[3].equals("epic");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isUriHasQuery(HttpExchange httpExchange) {
        try {
            return httpExchange.getRequestURI().getQuery().contains("id");
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void getTaskById(HttpExchange httpExchange, String type)  {
        URI uri = httpExchange.getRequestURI();
        String path = uri.toString();
        int id = parsedId(path.substring(path.lastIndexOf("=")+1));
        int rCode = 200;
        String response = "";
        try {
            switch(type) {
                case "task": {
                    response = gson.toJson(tasksManager.getTaskById(id));
                    break;
                }
                case "epic" : {
                    response = gson.toJson(tasksManager.getEpicById(id));
                    break;
                }
                case "subtask" : {
                    response = gson.toJson(tasksManager.getSubtaskById(id));
                    break;
                }
                default: {
                    System.out.println("Тип не поддерживается!");
                }
            }
        } catch (MethodExecutionException e) {
            response = "Такого таска не существует";
            rCode = 404;
        }
        writeResponse(httpExchange, rCode, response);
    }

    private void deleteTaskById(HttpExchange httpExchange, String type) {
        URI uri = httpExchange.getRequestURI();
        String path = uri.toString();
        int id = parsedId(path.substring(path.lastIndexOf("=")+1));
        int rCode = 200;
        String response = "";
        try {
            switch(type) {
                case "task": {
                    tasksManager.deleteTaskById(id);
                    break;
                }
                case "epic" : {
                    tasksManager.deleteEpicById(id);
                    break;
                }
                case "subtask" : {
                    tasksManager.deleteSubtaskById(id);
                    break;
                }
                default: {
                    System.out.println("Тип не поддерживается!");
                    rCode = 400;
                }
                response = type + " с id:" + id + " успешно удалён!";
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении таска");
            rCode = 404;
        }

        writeResponse(httpExchange, rCode, response);
    }

    private int parsedId (String id) {
        int parsedId;
        try {
            parsedId = Integer.parseInt(id);
        } catch (Exception e) {
            return -1;
        }
        return parsedId;
    }

    private void deleteTasks(HttpExchange httpExchange, String type) {
        int rCode = 200;
        switch (type) {
            case "task": {
                tasksManager.deleteTasks();
                break;
            }
            case "epic": {
                tasksManager.deleteEpics();
                break;
            }
            case "subtask": {
                tasksManager.deleteSubtasks();
                break;
            }
            default: {
                System.out.println("Тип не поддерживается!");
                rCode = 400;
            }
        }
        writeResponse(httpExchange, rCode,(type + "s успешно удалены!" ));
    }

    private void getTasks(HttpExchange httpExchange, String type) {
        int rCode;
        String response = getJsonTasks(type);
        if (!response.isEmpty()) {
            rCode = 200;
        } else {
            rCode = 400;
            response = "Вызываемый список - пустой";
        }
        writeResponse(httpExchange, rCode, response);
    }

    private String getJsonTasks(String type) {
        String json = "";
        switch (type) {
            case "task" : {
                try {
                    json = gson.toJson(tasksManager.getTasks());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "epic" : {
                try {
                    json = gson.toJson(tasksManager.getEpics());
                } catch (MethodExecutionException e) {
                    System.out.println("Список эпиков пустой");
                }
                break;
            }
            case "subtask" : {
                try {
                    json = gson.toJson(tasksManager.getSubtasks());
                } catch (MethodExecutionException e) {
                    System.out.println("Список subtasks пустой");
                }
                break;
            }
        }
        return json;
    }

    private void postTask(HttpExchange httpExchange, String type)  {
        InputStream inputStream = httpExchange.getRequestBody();
        String requestBody = "";
        try {
            requestBody = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            System.out.println("Не удалось прочитать поток байтов");
        }
        int rCode;
        String response;
        if (tryToCreateTasks(tasksManager, requestBody, type)) {
            rCode = 200;
            response = "Объект типа " + type + " успешно создан!";
        } else {
            rCode = 400;
            response = "Не удалось создать объект";
        }
        writeResponse(httpExchange, rCode, response);
    }

    private boolean tryToCreateTasks(TaskManager taskManager, String request, String type)
            throws MethodExecutionException {
        boolean isCouldCreate = true;
        try {
            switch (type) {
                case "task" : {
                    Task task = gson.fromJson(request, Task.class);
                    try {
                        if (taskManager.getTasks().contains(task)) {
                            taskManager.updateTask(task);
                        } else {
                            taskManager.addNewTaskItem(task);
                        }
                    } catch (MethodExecutionException e) {
                        taskManager.addNewTaskItem(task);
                    }
                    break;
                }
                case "epic" : {
                    Epic jsonEpic = gson.fromJson(request, Epic.class);
                    Epic epic = new Epic(jsonEpic.getName(), jsonEpic.getDescription());
                    try {
                        if (taskManager.getEpics().contains(epic)) {
                            taskManager.updateEpic(epic);
                        } else {
                            taskManager.addNewEpicItem(epic);
                        }
                    } catch (MethodExecutionException e ) {
                        taskManager.addNewEpicItem(epic);
                    }
                    break;
                }
                case "subtask" : {
                    Subtask sub = gson.fromJson(request, Subtask.class);
                    try {
                        if (taskManager.getSubtasks().contains(sub)) {
                            taskManager.updateSubtask(sub);
                        } else {
                            taskManager.addNewSubtaskItem(sub);
                        }
                    } catch (MethodExecutionException e) {
                        taskManager.addNewSubtaskItem(sub);
                    }
                    break;
                } default: {
                    System.out.println("Неверный тип передан!");

                    isCouldCreate = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isCouldCreate = false;
        }
        return isCouldCreate;
    }
}

