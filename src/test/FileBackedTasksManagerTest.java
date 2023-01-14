package test;

import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    File file = new File ("src/resources/test.csv");
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

    @Test
    void save() {
    }

    @Test
    void loadFromFile() {
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
    }

    @Test
    void getTasks() {
    }

    @Test
    void getEpics() {
    }

    @Test
    void getSubtasks() {
    }

    @Test
    void deleteTasks() {
    }

    @Test
    void deleteEpics() {
    }

    @Test
    void deleteSubtasks() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getEpicById() {
    }

    @Test
    void getSubtaskById() {
    }

    @Test
    void updateEpicInfo() {
    }

    @Test
    void deleteTaskById() {
    }

    @Test
    void deleteEpicById() {
    }

    @Test
    void deleteSubtaskById() {
    }

    @Test
    void getSubtaskForEpic() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void setTaskStatus() {
    }

    @Test
    void setSubtaskStatus() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void getPrioritizedTasks() {
    }
}