package service;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import exception.MethodExecutionException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;


public class HttpTaskServer  {

    final static private int PORT = 8080;
    final TaskManager tasksManager = Managers.getDefault();
    private final Gson gson = new Gson();

    public HttpTaskServer() {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", this::handle);
            httpServer.start();
            System.out.println("Сервер успешно запущен на порту: " + PORT);
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер");
        }
    }


    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer();
    }


    public void handle(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();
        String type = path.split("/")[2];
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "POST": {
                if (isUriHasQuery(httpExchange)) {
                    httpExchange.sendResponseHeaders(400, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write("Данный метод не поддерживается!".getBytes());
                    }
                    return;
                }
                try {
                    postTask(httpExchange, type);
                } catch (IOException e ) {
                    System.out.println("Не получилось создать таску");
                } finally {
                    httpExchange.close();
                }
                break;
            }
            case "GET": {
                if (isUriHasQuery(httpExchange)) {
                    getTaskById(httpExchange, type);
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

    private boolean isUriHasQuery(HttpExchange httpExchange) {
        try {
            return httpExchange.getRequestURI().getQuery().contains("id");
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void getTaskById(HttpExchange httpExchange, String type)  {
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
            rCode = 400;
        }
        try {
            httpExchange.sendResponseHeaders(rCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            System.out.println("Невозможно отравить ответ!");
        }
    }

    public  void deleteTaskById(HttpExchange httpExchange, String type) {
        URI uri = httpExchange.getRequestURI();
        String path = uri.toString();
        int id = parsedId(path.substring(path.lastIndexOf("=")+1));
        int rCode = 200;
        String response;
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
            }
        } catch (MethodExecutionException e) {
            System.out.println("Такого таска не существует");
        }
        try {
            response = type + " с id:" + id + " успешно удалён!";
            httpExchange.sendResponseHeaders(rCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            System.out.println("Невозможно отравить ответ!");
        }
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
        try {
            httpExchange.sendResponseHeaders(rCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write((type + "s успешно удалены!").getBytes());
            }
        } catch (IOException e) {
            System.out.println("Невозможно отравить ответ!");
        }
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
        try {
            httpExchange.sendResponseHeaders(rCode, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } catch (IOException e) {
            System.out.println("Не получилось получить таски");
        }
    }

    private String getJsonTasks(String type) {
        String json = "";
        switch (type) {
            case "task" : {
                try {
                    json = gson.toJson(tasksManager.getTasks());
                } catch (MethodExecutionException e) {
                    System.out.println("Список тасков пустой");
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
                    System.out.println("Список сабтасков пустой");
                }
                break;
            }
        }
        return json;
    }

    private void postTask(HttpExchange httpExchange, String type) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String requestBody = new String(inputStream.readAllBytes());
        int rCode;
        String response;
        if (tryToCreateTasks(tasksManager, requestBody, type)) {
            rCode = 200;
            response = "Объект типа " + type + " успешно создан!";
        } else {
            rCode = 400;
            response = "Не удалось создать объект";
        }
        httpExchange.sendResponseHeaders(rCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
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
                    Epic epic = gson.fromJson(request, Epic.class);
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
            System.out.println("Не получилось создать TASK из Json");
            isCouldCreate = false;
        }
        return isCouldCreate;
    }
}
