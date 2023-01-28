package service.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private String apiToken;
    private final String hostUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String url)  {
        hostUrl = url;
        URI uri = URI.create(url+"register");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Не получилось отправить запрос!");
        }
    }

    public void put(String key, String json)  {
        URI url = URI.create(hostUrl + "save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Не получилось отправить запрос!");
        }
    }

    public String load(String key)  {
        URI uri = URI.create(hostUrl + "load/" + key + "?API_TOKEN=" + apiToken);
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Не получилось отправить запрос!");
        }
        return "error";
    }
}
