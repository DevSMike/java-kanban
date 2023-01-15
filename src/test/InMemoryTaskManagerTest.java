package test;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import exception.MethodExecutionException;
import service.StatusManager;

import java.time.LocalDateTime;
import java.time.Month;

import java.util.ArrayList;
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
        Epic testEpic = new Epic("Задача", "Купить подарок на НГ");
        taskManager.addNewEpicItem(testEpic);
        Subtask testSubtask = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22), 500);
        testSubtask.setEpicId(testEpic.getId());
        assertEquals(testSubtask.getEpicId(), testEpic.getId());
        taskManager.addNewSubtaskItem(testSubtask);
        Subtask newSubtask = taskManager.getSubtaskById(testSubtask.getId());
        assertNotNull(newSubtask);
        assertEquals(newSubtask.getEpicId(), testEpic.getId());
    }

    @Test
    void getTasks() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getTasks");
        Task task = new Task("Задача", "Выполнить");
        Task task2 = new Task("Задача", "Выполнить");
        taskManager.addNewTaskItem(task);
        taskManager.addNewTaskItem(task2);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task2);
        assertEquals(taskManager.getTasks(), tasks);
    }

    @Test
    void getEpics() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getEpics());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getEpics");
        Epic epic1 = new Epic("Задача", "Выполнить");
        Epic epic2 = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic1);
        taskManager.addNewEpicItem(epic2);
        ArrayList<Epic> epics = new ArrayList<>();
        epics.add(epic1);
        epics.add(epic2);
        assertEquals(taskManager.getEpics(), epics);
    }


    @Test
    void getSubtasks() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getSubtasks");
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22), 500);
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22), 500);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        assertEquals(taskManager.getSubtasks(), subtasks);
    }

    @Test
    void deleteTasks() {
        taskManager.deleteTasks();
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getTasks");

        Task task = new Task("", "");
        taskManager.addNewTaskItem(task);
        assertEquals(taskManager.getTasks().size(), 1);
        taskManager.deleteTasks();
        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        assertEquals(e2.getMessage(),"Ошибка выполнения метода getTasks");
    }

    @Test
    void deleteEpics() {
        taskManager.deleteEpics();
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getEpics());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getEpics");

        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22), 500);
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22), 500);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        assertEquals(taskManager.getEpics().size(), 1);
        taskManager.deleteEpics();

        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getEpics());
        assertEquals(e2.getMessage(),"Ошибка выполнения метода getEpics");
        MethodExecutionException e3 = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e3.getMessage(),"Ошибка выполнения метода getSubtasks");

    }

    @Test
    void deleteSubtasks() {
        taskManager.deleteSubtasks();
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getSubtasks");

        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22), 500);
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22), 500);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        assertEquals(taskManager.getSubtasks().size(), 2);
        taskManager.deleteSubtasks();
        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e2.getMessage(),"Ошибка выполнения метода getSubtasks");
        assertEquals(epic.getSubtaskIds().size(), 0);

    }

    @Test
    void getTaskById() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getTaskById(0));
        assertEquals(e.getMessage(), "Невозможно получить task by id");

        Task task = new Task("", "");
        taskManager.addNewTaskItem(task);
        assertEquals(taskManager.getHistory().size(), 0);
        assertEquals(taskManager.getTaskById(task.getId()), task);
        assertEquals(taskManager.getHistory().size(), 1);
    }

    @Test
    void getEpicById() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getEpicById(0));
        assertEquals(e.getMessage(), "Невозможно получить epic by id");

        Epic epic = new Epic("", "");
        taskManager.addNewEpicItem(epic);
        assertEquals(taskManager.getHistory().size(), 0);
        assertEquals(taskManager.getEpicById(epic.getId()), epic);
        assertEquals(taskManager.getHistory().size(), 1);
    }

    @Test
    void getSubtaskById() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtaskById(0));
        assertEquals(e.getMessage(),"Невозможно получить subtask by id");
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22), 500);
        subtask1.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        assertEquals(taskManager.getHistory().size(), 0);
        assertEquals(taskManager.getSubtaskById(subtask1.getId()), subtask1);
        assertEquals(taskManager.getHistory().size(), 1);

    }

    @Test
    void updateEpicInfo() {
        Epic epic = new Epic("Купить подарки на НГ", "Составить список");
        taskManager.addNewEpicItem(epic);
        assertEquals(epic.getStatus(), StatusManager.Statuses.NONE);
        /////////
        Epic epicN1 = new Epic("Купить подарки на НГ", "Составить список");
        Subtask subtaskEpicN1N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22), 500);
        Subtask subtaskEpicN1N2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 12, 21, 22), 500);
        taskManager.addNewEpicItem(epicN1);
        subtaskEpicN1N1.setEpicId(epicN1.getId());
        subtaskEpicN1N2.setEpicId(epicN1.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN1N1);
        taskManager.addNewSubtaskItem(subtaskEpicN1N2);
        taskManager.updateEpicInfo(epicN1);
        assertEquals(epicN1.getStatus(), StatusManager.Statuses.NEW);
        ///////
        Epic epicN3 = new Epic("Купить подарки на НГ", "Составить список");
        taskManager.addNewEpicItem(epicN3);
        Subtask subtaskEpicN3N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 13, 21, 22), 500);
        Subtask subtaskEpicN3N2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 14, 21, 22), 500);
        subtaskEpicN3N1.setEpicId(epicN3.getId());
        subtaskEpicN3N2.setEpicId(epicN3.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN3N1);
        taskManager.addNewSubtaskItem(subtaskEpicN3N2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtaskEpicN3N1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtaskEpicN3N2);
        assertEquals(epicN3.getStatus(), StatusManager.Statuses.DONE);
        //////
        Epic epicN4 = new Epic("Купить подарки на НГ", "Составить список");
        taskManager.addNewEpicItem(epicN4);
        Subtask subtaskEpicN4N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 15, 21, 22), 500);
        Subtask subtaskEpicN4N2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 16, 21, 22), 500);
        subtaskEpicN4N1.setEpicId(epicN4.getId());
        subtaskEpicN4N2.setEpicId(epicN4.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN4N1);
        taskManager.addNewSubtaskItem(subtaskEpicN4N2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtaskEpicN4N1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.NEW, subtaskEpicN4N2);
        assertEquals(epicN4.getStatus(), StatusManager.Statuses.IN_PROGRESS);
        /////////
        Epic epicN5 = new Epic("Купить подарки на НГ", "Составить список");
        taskManager.addNewEpicItem(epicN5);
        Subtask subtaskEpicN5N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 17, 21, 22), 500);
        Subtask subtaskEpicN5N2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 18, 21, 22), 500);
        subtaskEpicN5N1.setEpicId(epicN5.getId());
        subtaskEpicN5N2.setEpicId(epicN5.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN5N1);
        taskManager.addNewSubtaskItem(subtaskEpicN5N2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.IN_PROGRESS, subtaskEpicN5N1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.IN_PROGRESS, subtaskEpicN5N2);
        assertEquals(epicN5.getStatus(), StatusManager.Statuses.IN_PROGRESS);
        assertEquals(epicN5.getDuration(), subtaskEpicN5N1.getDuration() + subtaskEpicN5N2.getDuration());
        assertEquals(epicN5.getEndTime(), subtaskEpicN5N2.getEndTime());
    }

    @Test
    void deleteTaskById() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.deleteTaskById(0));
        assertEquals(e.getMessage(), "Невозможно удалить task by id");

        Task task = new Task("", "");
        taskManager.addNewTaskItem(task);
        taskManager.getTaskById(task.getId());
        assertEquals(taskManager.getHistory().size(), 1);
        assertEquals(taskManager.getTasks().size(), 1);
        taskManager.deleteTaskById(task.getId());
        assertEquals(taskManager.getHistory().size(), 0);
        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        assertEquals(e2.getMessage(), "Ошибка выполнения метода getTasks");
    }

    @Test
    void deleteEpicById() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.deleteEpicById(0));
        assertEquals(e.getMessage(), "Невозможно удалить epic by id");

        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22), 500);
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22), 500);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        assertEquals(taskManager.getEpics().size(), 1);
        assertEquals(epic.getSubtaskIds().size(), 2);
        taskManager.deleteEpicById(epic.getId());

        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getEpics());
        assertEquals(e2.getMessage(),"Ошибка выполнения метода getEpics");
        MethodExecutionException e3 = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e3.getMessage(),"Ошибка выполнения метода getSubtasks");
    }

    @Test
    void deleteSubtaskById() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.deleteSubtaskById(0));
        assertEquals(e.getMessage(),"Невозможно удалить subtask by id");

        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22), 500);
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22), 500);
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtask1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.NEW, subtask2);
        assertEquals(epic.getStatus(), StatusManager.Statuses.IN_PROGRESS);
        assertEquals(taskManager.getSubtasks().size(), 2);
        taskManager.deleteSubtaskById(subtask1.getId());
        assertEquals(taskManager.getSubtasks().size(), 1);
        assertEquals(epic.getSubtaskIds().size(), 1);
        assertEquals(epic.getStatus(), StatusManager.Statuses.NEW);
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