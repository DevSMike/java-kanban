package test;

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
    File file = new File ("src/resources/test.csv");
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

    @BeforeEach
    void createNewInMemoryTaskManager() {
        super.setTaskManager(fileBackedTasksManager);

    }

    private void shouldBeEmptyFileWhileEmptyListOfTasks()  {
        taskManager.save();
        try {
          String file = Files.readString(Path.of("src/resources/test.csv"));
          assertEquals(file.length(), SYMBOLS_IN_BEGIN_OF_THE_FILE, "Файл непустой");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста save # 1");
        }
    }

    private void shouldBeEpicWithoutSubtasksInSave() {
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

    private void shouldBeEmptyHistoryWhileEmptyGetHistoryInSave() {
        Epic epic = new Epic("1", "f");
        taskManager.addNewEpicItem(epic);
        Epic epic2 = new Epic("2", "f");
        taskManager.addNewEpicItem(epic2);
        taskManager.save();
        try {
            String file = Files.readString(Path.of("src/resources/test.csv"));
            String[] fileLines = file.split("\n");
            assertEquals(fileLines.length, 4, "Лишние строки в файле");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка в методе save #3");
        }
    }

    private void shouldPrintHistoryInFileIfItExistInSave() {
        List<Epic> epics = taskManager.getEpics();
        taskManager.getEpicById(epics.get(0).getId());
        taskManager.getEpicById(epics.get(1).getId());
        taskManager.getEpicById(epics.get(2).getId());
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
    void save() {
        shouldBeEmptyFileWhileEmptyListOfTasks();
        shouldBeEpicWithoutSubtasksInSave();
        shouldBeEmptyHistoryWhileEmptyGetHistoryInSave();
        shouldPrintHistoryInFileIfItExistInSave();
    }

    private void shouldBeEmptyTaskManagerIfEmptyFile()  {
        taskManager.save();
        try {
            FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(file);
        } catch (IOException e) {
            assertEquals(e.getMessage(), "Данных в файле нет", "Ошибки не совпадают");
        }
    }

    private void shouldBeEpicWithoutSubtasksInLoad() {
        Epic epic = new Epic("1", "f");
        taskManager.addNewEpicItem(epic);
        taskManager.save();
        try {
            FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(file);
            assertEquals(testManager.getEpics().get(0).getName(), epic.getName(), "Эпики не равны" );
            assertEquals(testManager.getEpics().get(0).getStatus(), StatusManager.Statuses.NONE
                    , "Статусы не равны");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста load #2");
        }
    }

    private void shouldBeEmptyHistoryWhileEmptyGetHistoryInLoad() {
        Epic epic = new Epic("2", "f");
        taskManager.addNewEpicItem(epic);
        taskManager.save();
        try {
            FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(file);
            MethodExecutionException e = assertThrows(MethodExecutionException.class, testManager::getHistory
            ,"Исключения не выпадают");
            assertEquals(e.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не совпадают");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста load #3");
        }
    }

    private void shouldPrintHistoryInFileIfItExistInLoad() {

        try {
            FileBackedTasksManager testManager = FileBackedTasksManager.loadFromFile(file);
            List<Epic> epics = testManager.getEpics();
            testManager.getEpicById(epics.get(0).getId());
            testManager.getEpicById(epics.get(1).getId());
            testManager.save();
            assertEquals(testManager.getHistory().size(), 2, "Списки не совпадают");
            assertEquals(testManager.getHistory().get(0), epics.get(0), "Эпики не совпадают");
        } catch (IOException e) {
            throw new MethodExecutionException("Ошибка при выполнении теста load #4");
        }
    }

    @Test
    void loadFromFile() {
        shouldBeEmptyTaskManagerIfEmptyFile();
        shouldBeEpicWithoutSubtasksInLoad();
        shouldBeEmptyHistoryWhileEmptyGetHistoryInLoad();
        shouldPrintHistoryInFileIfItExistInLoad();
    }

    @Test
    void addNewTaskItem() {
        super.addNewTaskItem();
    }

    @Test
    void addNewEpicItem() {
        super.addNewEpicItem();
    }

    @Test
    void addNewSubtaskItem() {
        super.addNewSubtaskItem();
    }

    @Test
    void getTasks() {
        super.getTasks();
    }

    @Test
    void getEpics() {
        super.getEpics();
    }

    @Test
    void getSubtasks() {
        super.getSubtasks();
    }

    @Test
    void deleteTasks() {
        super.deleteTasks();
    }

    @Test
    void deleteEpics() {
        super.deleteEpics();
    }

    @Test
    void deleteSubtasks() {
        super.deleteSubtasks();
    }

    @Test
    void getTaskById() {
        super.getTaskById();
    }

    @Test
    void getEpicById() {
        super.getEpicById();
    }

    @Test
    void getSubtaskById() {
        super.getSubtaskById();
    }

    @Test
    void updateEpicInfo() {
        super.updateEpicInfo();
    }

    @Test
    void deleteTaskById() {
        super.deleteTaskById();
    }

    @Test
    void deleteEpicById() {
        super.deleteEpicById();
    }

    @Test
    void deleteSubtaskById() {
        super.deleteSubtaskById();
    }

    @Test
    void getSubtaskForEpic() {
        super.getSubtaskForEpic();
    }

    @Test
    void updateTask() {
        super.updateTask();
    }

    @Test
    void updateEpic() {
        super.updateEpic();
    }

    @Test
    void updateSubtask() {
        super.updateSubtask();
    }

    @Test
    void setTaskStatus() {
        super.setTaskStatus();
    }

    @Test
    void setSubtaskStatus() {
        super.setSubtaskStatus();
    }

    @Test
    void getHistory() {
        super.getHistory();
    }

    @Test
    void getPrioritizedTasks() {
        super.getPrioritizedTasks();
    }
}