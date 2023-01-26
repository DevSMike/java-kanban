import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;
import service.StatusManager;
import service.server.HttpTaskServer;
import service.server.KVServer;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;


import static org.junit.jupiter.api.Assertions.*;


public class HttpTaskServerTest {


    static HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new service.server.LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new service.server.DurationAdapter()).create();

    static HttpTaskServer server;

    @BeforeAll
    public static void before() throws IOException, InterruptedException {
        new KVServer().start();
         server = new HttpTaskServer();

    }
    @Test
    public void handleShouldBe200WhenTryToCreateTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Task newTask = new Task("Прогулка", "Сходить в лес",
                LocalDateTime.of(2023, Month.JANUARY, 16, 21, 22),  Duration.ofMinutes(200));
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void handleShouldBe200WhenTryToCreateEpic() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        Epic newEpic = new Epic("Купить подарки на НГ", "Составить список");
        String json = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void handleShouldBe200WhenTryToCreateSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        Subtask newSubtask = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22),  Duration.ofMinutes(500));
        newSubtask.setStatus(StatusManager.Statuses.NEW);
        String json = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void handleShouldBe400WhenTryToCreateSubtaskAndIncorrectLink() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/TASK/");
        Subtask newSubtask = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22),  Duration.ofMinutes(500));
        newSubtask.setStatus(StatusManager.Statuses.NEW);
        String json = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 400);
    }

    @Test
    public void handleShouldBe200WhenTryToGetTasks() throws IOException, InterruptedException {
        URI urlToPost = URI.create("http://localhost:8080/tasks/task");
        Task task = new Task("Привет", "Я тест");
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlToPost).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response  = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void handleShouldBe200WhenTryToGetEpics() throws IOException, InterruptedException {
        URI urlToPost = URI.create("http://localhost:8080/tasks/epic");
        Epic epic = new Epic("Привет", "Я тест");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlToPost).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response  = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void handleShouldBe200WhenTryToGetSubtasks() throws IOException, InterruptedException {
        URI urlToPost = URI.create("http://localhost:8080/tasks/subtask");
        Subtask newSubtask = new Subtask("Тестовый сабтаск234", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2024, Month.JANUARY, 1, 21, 22),  Duration.ofMinutes(500));
        newSubtask.setStatus(StatusManager.Statuses.NEW);
        String json = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlToPost).POST(body).build();
         client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2  = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response2.statusCode(), 200);
    }

    @Test
    public  void handleShouldBe200WhenGetTaskById() throws IOException, InterruptedException {
        URI urlToPost = URI.create("http://localhost:8080/tasks/task");
        Task newTask = new Task("Тестовая прогулка", "Сходить в лес");
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlToPost).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        int id = 0;
       for (Task task : server.getTasksManager().getTasks()) {
           if (task.getName().equals(newTask.getName()))
               id = task.getId();
       }
        URI url = URI.create("http://localhost:8080/tasks/task/?id="+ id);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2  = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response2.statusCode(), 200);
    }

    @Test
    public  void handleShouldBe200WhenGetEpicById() throws IOException, InterruptedException {
        URI urlToPost = URI.create("http://localhost:8080/tasks/epic");
        Epic epic = new Epic("Привет", "Я тест");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlToPost).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        int id = 0;
        for (Epic epic2 : server.getTasksManager().getEpics()) {
            if (epic2.getName().equals(epic.getName()))
                id = epic2.getId();
        }
        URI url = URI.create("http://localhost:8080/tasks/epic/?id="+ id);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2  = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response2.statusCode(), 200);
    }

    @Test
    public  void handleShouldBe200WhenGetSubtaskById() throws IOException, InterruptedException {
        URI urlToPost = URI.create("http://localhost:8080/tasks/subtask");
        Subtask newSubtask = new Subtask("Тестовый сабтаск", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2024, Month.JANUARY, 15, 21, 22),  Duration.ofMinutes(500));
        newSubtask.setStatus(StatusManager.Statuses.NEW);
        String json = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlToPost).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        request= HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response2  = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response2.statusCode(), 200);
    }

    @Test
    public  void handleShouldBe200WhenGetHistory() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request= HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response  = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public  void handleShouldBe200WhenGetPrioritizedTasks() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request= HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response  = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public  void handleShouldBe200WhenGetEpicsSubtask() throws IOException, InterruptedException {
        URI urlToPostEpic = URI.create("http://localhost:8080/tasks/epic");
        Epic epic = new Epic("Пока", "Я тест");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlToPostEpic).POST(body).build();
         client.send(request, HttpResponse.BodyHandlers.ofString());

        int id = 0;
        for (Epic epic2 : server.getTasksManager().getEpics()) {
            if (epic2.getName().equals(epic.getName()))
                id = epic2.getId();
        }
        URI urlToPost = URI.create("http://localhost:8080/tasks/subtask");
        Subtask newSubtask = new Subtask("Купить TEST", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2021, Month.JANUARY, 12, 21, 32),  Duration.ofMinutes(500));
        newSubtask.setEpicId(id);
        json = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body2 =  HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request2 = HttpRequest.newBuilder().uri(urlToPost).POST(body2).build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + id);
        HttpRequest request3 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response3  = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(response3.statusCode(), 200);
    }

    @Test
    public  void handleShouldBe200WhenDeleteSubtaskById() throws IOException, InterruptedException {
        URI urlToPost = URI.create("http://localhost:8080/tasks/subtask");
        Subtask newSubtask = new Subtask("Купить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2020, Month.JANUARY, 15, 21, 22),  Duration.ofMinutes(500));
        newSubtask.setStatus(StatusManager.Statuses.NEW);

        String json = gson.toJson(newSubtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(urlToPost).POST(body).build();
         client.send(request, HttpResponse.BodyHandlers.ofString());

        int id = 0;
        for (Subtask subtask : server.getTasksManager().getSubtasks()) {
            if (subtask.getName().equals(newSubtask.getName()))
                id = subtask.getId();
        }
       URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + id);
       HttpRequest  request2 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json")
               .DELETE().build();

       HttpResponse<String> response2  = client.send(request2, HttpResponse.BodyHandlers.ofString());
       assertEquals(response2.statusCode(), 200);
    }

    @Test
    public  void handleShouldBe404WhenDeleteSubtaskByIdButIdIsIncorrect() throws IOException, InterruptedException {

        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=11");
        HttpRequest  request2 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json")
                .DELETE().build();

        HttpResponse<String> response2  = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response2.statusCode(), 404);
    }

    @Test
    public void handleShouldBe200WhenDeleteAllEpics() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest  request2 = HttpRequest.newBuilder().uri(url).header("Content-Type", "application/json")
                .DELETE().build();

        HttpResponse<String> response  = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }
}

