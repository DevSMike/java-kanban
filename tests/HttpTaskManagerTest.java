import exception.MethodExecutionException;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.server.HttpTaskManager;
import service.server.KVServer;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private static final String URL = "http://localhost:8078/";
    HttpTaskManager taskManager = new HttpTaskManager(URL);
    static KVServer server;

    static {
        try {
            server = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    HttpTaskManagerTest() throws IOException, InterruptedException {
    }

    @BeforeEach
    void createNewInMemoryTaskManager() {
        super.setTaskManager(taskManager);

    }
    @BeforeAll
    static void before()  {
        server.start();
    }

    @Test
    void saveMustBeEmptyWhileEmptyTasks() {
        taskManager.save();
        MethodExecutionException  e = assertThrows(MethodExecutionException.class, () -> taskManager.getHistory());
        assertEquals(e.getMessage(), "Ошибка в выводе истории просмотра","Ошибки не совпадают");

    }

    @Test
    void saveShouldSaveEpicWithoutSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "epic");
        taskManager.addNewEpicItem(epic);
        HttpTaskManager temp = HttpTaskManager.loadFromServer(URL);
        assertEquals(taskManager.getEpicById(epic.getId()).getName(), temp.getEpicById(epic.getId()).getName());
    }

    @Test
    void saveShouldBeEmptyHistoryIsItIsEmpty() throws IOException, InterruptedException {
        server.deleteAllData();
        HttpTaskManager temp = HttpTaskManager.loadFromServer(URL);
        MethodExecutionException e = assertThrows(MethodExecutionException.class, temp::getHistory);
        assertEquals(e.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не равны");
    }

    @Test
    void saveShouldBeHistoryIsNotItIsEmpty() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "epic");
        taskManager.addNewEpicItem(epic);
        taskManager.getEpicById(epic.getId());

        HttpTaskManager temp = HttpTaskManager.loadFromServer(URL);
        assertEquals(temp.getHistory().get(0).getName(), epic.getName());
    }

    @Test
    void loadShouldBeEmptyIfServerIsEmpty() throws IOException, InterruptedException {
        HttpTaskManager temp = HttpTaskManager.loadFromServer(URL);
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        MethodExecutionException e2  = assertThrows(MethodExecutionException.class, temp::getTasks);
        assertEquals(e.getMessage(), e2.getMessage());

    }

    @Test
    void loadShouldBeEpicWithoutSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "epic");
        taskManager.addNewEpicItem(epic);
        HttpTaskManager temp = HttpTaskManager.loadFromServer(URL);
        assertEquals(epic, temp.getEpicById(epic.getId()));
    }


}