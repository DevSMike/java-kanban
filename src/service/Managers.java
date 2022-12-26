package service;

import java.io.File;

public class Managers {

    private static final String PATH = "src/resources/tasksData.csv";

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File(PATH));
    }

    public static HistoryManager getDefaultHistory() {
       return new InMemoryHistoryManager();
    }
}

