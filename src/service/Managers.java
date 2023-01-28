package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.server.DurationAdapter;
import service.server.HttpTaskManager;
import service.server.LocalDateAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    private static final String URL = "http://localhost:8078/";

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(URL);
    }

    public static HistoryManager getDefaultHistory() {
       return new InMemoryHistoryManager();
    }

    public static Gson getDefaultGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter()).create();
    }
}

