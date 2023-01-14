package test;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.StatusManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    @Test
    void addNewTaskItem() {
        Task task = new Task("Задача", "Нужно выполнить");
        taskManager.addNewTaskItem(task);
        Task testTask = taskManager.getTaskById(task.getId());
        assertNotNull(testTask);
        assertEquals(task, testTask);
        assertEquals(task.getStatus(), StatusManager.Statuses.NEW);
        assertEquals(task.getId(), 1);
    }

    @Test
    void addNewEpicItem() {
        Epic epic = new Epic("Задача", "Что делаем");
        taskManager.addNewEpicItem(epic);
        Epic testEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(testEpic);
        assertEquals(epic, testEpic);
        Subtask subs = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22), 500);
        subs.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subs);
        assertEquals(epic.getStatus(), StatusManager.Statuses.NEW);
        List<Integer> list = Collections.singletonList(subs.getId());
        assertEquals(epic.getSubtaskIds(), list);
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