import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Task;
import service.server.*;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class MainForFiles {
    private static final String URL = "http://localhost:8078/";
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        HttpTaskServer server = new HttpTaskServer();

        Task taskN1 = new Task("Прогулка", "Сходить в лес",
                LocalDateTime.of(2023, Month.JANUARY, 16, 21, 22),  Duration.ofMinutes(200));

        Task taskN2 = new Task("Почитать", "Чтение перед сном"
                ,LocalDateTime.of(2023, Month.JANUARY, 17, 21, 22),  Duration.ofMinutes(100));

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        String jSonTask = gson.toJson(taskN1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jSonTask);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);

        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());


        server.stop();
        HttpTaskServer server1 = new HttpTaskServer();
        jSonTask = gson.toJson(taskN2);
        body = HttpRequest.BodyPublishers.ofString(jSonTask);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
         response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);

        request = HttpRequest.newBuilder().uri(uri).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

    }
}
class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm:ss");
    private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yy|HH:mm:ss");

    @Override
    public void write(final JsonWriter jsonWriter, LocalDateTime localDate) throws IOException {
        try {
            jsonWriter.value(localDate.format(formatterWriter));
        } catch (NullPointerException e) {
            System.out.println("На вход Time не передавалось!");
            jsonWriter.value("NONE");
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}

class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        Optional<Duration> optDuration = Optional.ofNullable(duration);
        if (optDuration.isPresent()) {
            jsonWriter.value(optDuration.get().toMinutes());
        } else {
            jsonWriter.value(0L);
        }
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
    }
}