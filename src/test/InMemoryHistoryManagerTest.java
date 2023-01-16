package test;

import exception.MethodExecutionException;
import model.Epic;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager;
    InMemoryTaskManager taskManager;

    @BeforeEach
    void createManagers() {
        historyManager = new InMemoryHistoryManager();
        taskManager  = new InMemoryTaskManager();
    }

    private void shouldBeEmptyHistoryIfEmpty() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getHistory()
        ,"Исключение не кидается");
        assertEquals(e.getMessage(), "Ошибка в выводе истории просмотра", "ошибки не равны");
    }

    private void shouldBeOneTaskIfTwoIsSimilarInHistory() {
        Task task1 = new Task ("1", "D");
        historyManager.add(task1);
        historyManager.add(task1);

        assertEquals(historyManager.getHistory().size(), 1, "Размер листа не совпадает");
        assertEquals(historyManager.getHistory().get(0), task1, "Таски не равны");
    }

    @Test
    void getHistory() {
        shouldBeEmptyHistoryIfEmpty();
        shouldBeOneTaskIfTwoIsSimilarInHistory();
    }

    private void shouldReturnFalseIfNotContains() {
        Task task = new Task("a", "b");
        taskManager.addNewTaskItem(task);
        assertFalse(historyManager.isContainsId(task.getId()), "Ошибка, содержит");
    }

    private void shouldBeTrueIfContains() {
        List<Task> tasks = taskManager.getTasks();
        historyManager.add(tasks.get(0));
        assertTrue(historyManager.isContainsId(tasks.get(0).getId()), "Ошибка, не содержит");
    }

    @Test
    void isContainsId() {
        shouldReturnFalseIfNotContains();
        shouldBeTrueIfContains();
    }

    private void shouldNotAddIfDataEqualsNull() {
        Epic epic = null;
        historyManager.add(epic);
        assertEquals(historyManager.getHistory().size(), 0, "Значения не равны");
    }

    private void shouldAddInTheEnd() {
        Task task1 = new Task("1", "D");
        Task task2 = new Task("2", "D");
        task1.setId(0);
        task2.setId(1);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(historyManager.getHistory().get(1).getName(), task2.getName(), "Таски не равны");
    }

    @Test
    void add() {
        shouldNotAddIfDataEqualsNull();
        shouldAddInTheEnd();
    }

    private void shouldDeleteFirstElement() {
        Task task1 = new Task("1", "D");
        Task task2 = new Task("2", "D");
        Task task3 = new Task("3", "D");
        Task task4 = new Task("4", "D");
        task1.setId(0);
        task2.setId(1);
        task3.setId(2);
        task4.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.remove(task1.getId());
        assertEquals(historyManager.getHistory().size(), 3, "Размеры не равны");
        assertEquals(historyManager.getHistory().get(0).getName(), task2.getName(), "Задачи не совпадают");
    }

    private void shouldDeleteMiddleElement() {
        List<Task> tasks = historyManager.getHistory();
        historyManager.remove(tasks.get(1).getId());
        assertEquals(historyManager.getHistory().size(), 2, "Размеры не равны");
        assertEquals(historyManager.getHistory().get(0).getName(), tasks.get(0).getName(), "Задачи не совпадают");
    }

    private  void shouldDeleteLastElement() {
        List<Task> tasks = historyManager.getHistory();
        historyManager.remove(tasks.get(1).getId());
        assertEquals(historyManager.getHistory().size(), 1, "Размеры не равны");
        assertEquals(historyManager.getHistory().get(0).getName(), tasks.get(0).getName(), "Задачи не совпадают");
    }

    @Test
    void remove() {
        shouldDeleteFirstElement();
        shouldDeleteMiddleElement();
        shouldDeleteLastElement();
    }
}