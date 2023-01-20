
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

    @Test
    void getHistoryShouldBeEmptyIfEmptyHistory() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getHistory()
        ,"Исключение не кидается");
        assertEquals(e.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не равны");
    }

    @Test
    void getHistoryShouldBeOneTaskIfTwoIsSimilar() {
        Task task1 = new Task ("1", "D");
        historyManager.add(task1);
        historyManager.add(task1);

        assertEquals(historyManager.getHistory().size(), 1, "Размер листа не совпадает");
        assertEquals(historyManager.getHistory().get(0), task1, "Таски не равны");
    }


    @Test
     void isContainsIdShouldReturnFalseIfNotContains() {
        Task task = new Task("a", "b");
        taskManager.addNewTaskItem(task);
        assertFalse(historyManager.isContainsId(task.getId()), "Ошибка, содержит");
    }

    @Test
    void isContainsIdShouldBeTrueIfContains() {
        Task task = new Task("a", "b");
        taskManager.addNewTaskItem(task);
        historyManager.add(task);
        assertTrue(historyManager.isContainsId(task.getId()), "Ошибка, не содержит");
    }

    @Test
    void addShouldNotAddIfDataEqualsNull() {
        Epic epic = null;
        historyManager.add(epic);
        assertEquals(historyManager.getHistory().size(), 0, "Значения не равны");
    }

    @Test
     void addShouldAddInTheEnd() {
        Task task1 = new Task("1", "D");
        Task task2 = new Task("2", "D");
        task1.setId(0);
        task2.setId(1);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(historyManager.getHistory().get(1).getName(), task2.getName(), "Таски не равны");
    }

    @Test
     void removeShouldDeleteFirstElement() {
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

    @Test
    void removeShouldDeleteMiddleElement() {
        Task task1 = new Task("1", "D");
        Task task2 = new Task("2", "D");
        Task task3 = new Task("3", "D");
        task1.setId(0);
        task2.setId(1);
        task3.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());
        assertEquals(historyManager.getHistory().size(), 2, "Размеры не равны");
        assertEquals(historyManager.getHistory().get(0).getName(), task1.getName(), "Задачи не совпадают");
    }

    @Test
    void removeShouldDeleteLastElement() {
        Task task1 = new Task("1", "D");
        Task task2 = new Task("2", "D");
        task1.setId(0);
        task2.setId(1);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task2.getId());
        assertEquals(historyManager.getHistory().size(), 1, "Размеры не равны");
        assertEquals(historyManager.getHistory().get(0).getName(), task1.getName(), "Задачи не совпадают");
    }

}