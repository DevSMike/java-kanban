package service;

import service.server.HttpTaskManager;

import java.io.IOException;

public class Managers {

    private static final String URL = "http://localhost:8078/";

    public static TaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager(URL);
    }

    public static HistoryManager getDefaultHistory() {
       return new InMemoryHistoryManager();
    }
}

