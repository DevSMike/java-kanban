

import exception.MethodExecutionException;
import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import service.StatusManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    static final int SYMBOLS_IN_BEGIN_OF_THE_FILE = 65;
    File file = new File("src/resources/test.csv");
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

    @BeforeEach
    void createNewInMemoryTaskManager() {
        super.setTaskManager(fileBackedTasksManager);

    }

    @Test
    void saveShouldBeEmptyFileWhileEmptyListOfTasks() {
        taskManager.save();
        try {
            String file = Files.readString(Path.of("src/resources/test.csv"));
            assertEquals(file.length(), SYMBOLS_IN_BEGIN_OF_THE_FILE, "Файл непустой");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста save # 1");
        }
    }

    @Test
     void saveShouldBeEpicWithoutSubtasks() {
        Epic epic = new Epic("1", "f");
        taskManager.addNewEpicItem(epic);
        taskManager.save();
        try {
            String file = Files.readString(Path.of("src/resources/test.csv"));
            assertTrue(file.contains("1"), "Эпик не с копировался");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста save # 1");
        }
    }
    @Test
    void saveShouldBeEmptyHistoryWhileEmptyGetHistory() {
        Epic epic = new Epic("1", "f");
        taskManager.addNewEpicItem(epic);
        Epic epic2 = new Epic("2", "f");
        taskManager.addNewEpicItem(epic2);
        Epic epic3 = new Epic("3", "f");
        taskManager.addNewEpicItem(epic3);
        taskManager.save();
        try {
            String file = Files.readString(Path.of("src/resources/test.csv"));
            String[] fileLines = file.split("\n");
            assertEquals(fileLines.length, 4, "Лишние строки в файле");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка в методе save #3");
        }
    }

    @Test
    void saveShouldPrintHistoryInFileIfItExist() {
        Epic epic = new Epic("1", "f");
        taskManager.addNewEpicItem(epic);
        Epic epic2 = new Epic("2", "f");
        taskManager.addNewEpicItem(epic2);
        Epic epic3 = new Epic("3", "f");
        taskManager.addNewEpicItem(epic3);
        taskManager.getEpicById(epic.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getEpicById(epic3.getId());
        taskManager.save();
        try {
            String file = Files.readString(Path.of("src/resources/test.csv"));
            String[] fileLines = file.split("\n");
            assertEquals(fileLines.length, 6, "Лишние строки в файле");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка в методе save #4");
        }
    }

    @Test
    void loadFromFileShouldBeEmptyTaskManagerIfEmptyFile() {
        taskManager.save();
        try {
            FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(file);
        } catch (IOException e) {
            assertEquals(e.getMessage(), "Данных в файле нет", "Ошибки не совпадают");
        }
    }

    @Test
    void loadFromFileShouldBeEpicWithoutSubtasks() {
        Epic epic = new Epic("1", "f");
        taskManager.addNewEpicItem(epic);
        taskManager.save();
        try {
            FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(file);
            assertEquals(testManager.getEpics().get(0).getName(), epic.getName(), "Эпики не равны");
            assertEquals(testManager.getEpics().get(0).getStatus(), StatusManager.Statuses.NONE
                    , "Статусы не равны");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста load #2");
        }
    }

    @Test
    void loadFromFileShouldBeEmptyHistoryWhileEmptyGetHistory() {
        Epic epic = new Epic("2", "f");
        taskManager.addNewEpicItem(epic);
        taskManager.save();
        try {
            FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(file);
            MethodExecutionException e = assertThrows(MethodExecutionException.class, testManager::getHistory
                    , "Исключения не выпадают");
            assertEquals(e.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не совпадают");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста load #3");
        }
    }

    @Test
    void loadFromFileShouldPrintHistoryInFileIfItExist() {
        try {
            FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(file);
            Epic epic = new Epic("1", "d");
            Epic epic2 = new Epic("2", "d");
            testManager.addNewEpicItem(epic);
            testManager.addNewEpicItem(epic2);
            testManager.getEpicById(epic.getId());
            testManager.getEpicById(epic2.getId());
            testManager.save();
            assertEquals(testManager.getHistory().size(), 2, "Списки не совпадают");
            assertEquals(testManager.getHistory().get(0).getName(), epic.getName(), "Эпики не совпадают");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста load #4");
        }
    }
}