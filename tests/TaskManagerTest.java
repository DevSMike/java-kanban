
import exception.MethodExecutionException;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.StatusManager;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {

    T taskManager;

    void setTaskManager (T taskManager) {
        this.taskManager = taskManager;
    }

    @Test
    void addNewTaskItemShouldAddIfTaskIsNotNull() {
        Task task = new Task("Задача", "Нужно выполнить");
        taskManager.addNewTaskItem(task);
        Task testTask = taskManager.getTaskById(task.getId());
        assertNotNull(testTask, "Таска равна null");
        assertEquals(task.getName(), testTask.getName(), "Задачи не равны!");
        assertEquals(task.getStatus(), StatusManager.Statuses.NEW,"Статусы не равны");
        assertEquals(task.getId(), 1, "Id не равны");
    }

    @Test
    void addNewEpicItemShouldAddIfEpicIsNotNull() {
        Epic epic = new Epic("Задача", "Что делаем");
        taskManager.addNewEpicItem(epic);
        Epic testEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(testEpic, "Эпик равен null");
        assertEquals(epic.getName(), testEpic.getName(), "Эпики не равны");
        Subtask subs = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                , LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22), Duration.ofMinutes(500));
        subs.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subs);
        assertEquals(epic.getStatus(), StatusManager.Statuses.NEW, "Статусы не равны");
        List<Integer> list = Collections.singletonList(subs.getId());
        assertEquals(epic.getSubtaskIds().get(0), list.get(0), "Листы subtaskIds не равны");
    }

    @Test
    void addNewSubtaskItemShouldAddIfSubtaskIsNotNull() {
        Epic testEpic = new Epic("Задача", "Купить подарок на НГ");
        taskManager.addNewEpicItem(testEpic);
        Subtask testSubtask = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22),  Duration.ofMinutes(500));
        testSubtask.setEpicId(testEpic.getId());
        assertEquals(testSubtask.getEpicId(), testEpic.getId(), "Эпик Id сабтаска неверный");
        taskManager.addNewSubtaskItem(testSubtask);
        Subtask newSubtask = taskManager.getSubtaskById(testSubtask.getId());
        assertNotNull(newSubtask, "newSubtask == null");
        assertEquals(newSubtask.getEpicId(), testEpic.getId(), "Эпик Id сабтаска неверный");
    }

    @Test
    void getTasksShouldBeExceptionWhileNoTasks() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getTasks", "Ошибки не равны");
    }

    @Test
    void getTasksShouldReturnTasks() {
        Task task = new Task("Задача", "Выполнить");
        Task task2 = new Task("Задача", "Выполнить");
        taskManager.addNewTaskItem(task);
        taskManager.addNewTaskItem(task2);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task2);
        assertEquals(taskManager.getTasks().get(0), tasks.get(0), "Листы с задачами не равны");
    }

    @Test
    void getEpicsShouldBeExceptionWhileNoEpics() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getEpics());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getEpics", "Ошибки не равны");
    }

    @Test
    void getEpicsShouldReturnEpics() {
        Epic epic1 = new Epic("Задача", "Выполнить");
        Epic epic2 = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic1);
        taskManager.addNewEpicItem(epic2);
        ArrayList<Epic> epics = new ArrayList<>();
        epics.add(epic1);
        epics.add(epic2);
        assertEquals(taskManager.getEpics().get(0), epics.get(0), "Листы эпиков не равны");
    }

    @Test
    void getSubtasksShouldBeExceptionWhileNoSubtasks() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getSubtasks", "Ошибки не равны");
    }

    @Test
    void getSubtasksShouldReturnSubtasks() {
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        assertEquals(taskManager.getSubtasks().get(0), subtasks.get(0), "Листы сабтасков не равны");
    }

    @Test
    void deleteTasksShouldBeExceptionWhileNoTasks() {
        taskManager.deleteTasks();
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getTasks", "Ошибки не равны");
    }

    @Test
    void deleteTasksShouldDeleteAllTasksIfExists() {
        Task task = new Task("", "");
        taskManager.addNewTaskItem(task);
        assertEquals(taskManager.getTasks().size(), 1, "Списков тасков неверный");
        taskManager.deleteTasks();
        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        assertEquals(e2.getMessage(),"Ошибка выполнения метода getTasks", "Ошибки не равны");
    }

    @Test
    void deleteEpicsShouldBeExceptionWhileNoEpics() {
        taskManager.deleteEpics();
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getEpics());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getEpics", "Ошибки не равны");
    }

    @Test
    void deleteEpicsShouldDeleteAllEpicsAndSubtasksIfExists() {
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        assertEquals(taskManager.getEpics().size(), 1, "Список эпиков неверный");
        taskManager.deleteEpics();

        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getEpics());
        assertEquals(e2.getMessage(),"Ошибка выполнения метода getEpics", "Ошибки не равны");
        MethodExecutionException e3 = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e3.getMessage(),"Ошибка выполнения метода getSubtasks", "Ошибки не равны");
    }

    @Test
    void deleteSubtasksShouldBeExceptionWhileNoSubtasks() {
        taskManager.deleteSubtasks();
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e.getMessage(),"Ошибка выполнения метода getSubtasks", "Ошибки не равны");
    }

    @Test
    void deleteSubtasksShouldDeleteAllSubtasksIfEpicsAndSubtasksAreExist() {
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        assertEquals(taskManager.getSubtasks().size(), 2, "Список сабтасков неверный");
        taskManager.deleteSubtasks();
        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e2.getMessage(),"Ошибка выполнения метода getSubtasks", "Ошибки не равны");
        assertEquals(epic.getSubtaskIds().size(), 0);

    }

    @Test
    void getTaskByIdShouldBeExceptionWhenNoTasks() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getTaskById(0));
        assertEquals(e.getMessage(), "Невозможно получить task by id", "Ошибки не равны");
    }

    @Test
    void getTaskByIdShouldReturnCorrectTaskAndAddInHistory() {
        Task task = new Task("", "");
        taskManager.addNewTaskItem(task);
        MethodExecutionException eh = assertThrows(MethodExecutionException.class, () -> taskManager.getHistory()
                , "Исключение не бросается");
        assertEquals(eh.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не равны");
        assertEquals(taskManager.getTaskById(task.getId()).getName(), task.getName(), "Задача не равны");
        assertEquals(taskManager.getHistory().size(), 1, "Список истории неверный");
    }

    @Test
    void getEpicByIdShouldBeExceptionWhenNoEpics() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getEpicById(0));
        assertEquals(e.getMessage(), "Невозможно получить epic by id", "Ошибки не равны");
    }

    @Test
    void getEpicByIdShouldReturnCorrectEpicAndAddInHistory() {
        Epic epic = new Epic("", "");
        taskManager.addNewEpicItem(epic);
        MethodExecutionException eh = assertThrows(MethodExecutionException.class, () -> taskManager.getHistory()
                , "Исключение не бросается");
        assertEquals(eh.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не равны");
        assertEquals(taskManager.getEpicById(epic.getId()).getName(), epic.getName(), "Эпики не равны");
        assertEquals(taskManager.getHistory().size(), 1, "Список истории неверный");
    }

    @Test
    void getSubtaskByIdShouldBeExceptionWhenNoSubtasks() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtaskById(0));
        assertEquals(e.getMessage(),"Невозможно получить subtask by id", "Ошибки не равны");
    }

    @Test
    void getSubtaskByIdShouldReturnCorrectSubtaskAndAddInHistory()  {
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        MethodExecutionException eh = assertThrows(MethodExecutionException.class, () -> taskManager.getHistory()
                , "Исключение не бросается");
        assertEquals(eh.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не равны");
        assertEquals(taskManager.getSubtaskById(subtask1.getId()).getName(), subtask1.getName()
                , "Сабтаски не совпадают");
        assertEquals(taskManager.getHistory().size(), 1, "Список истории неверный");
    }

    @Test
    void updateEpicInfoShouldBeNoneWhenNoSubtasksInEpic() {
        Epic epic = new Epic("Купить подарки на НГ", "Составить список");
        taskManager.addNewEpicItem(epic);
        assertEquals(epic.getStatus(), StatusManager.Statuses.NONE, "Статусы не равны");
    }

    @Test
    void updateEpicInfoShouldBeNewWhenAllSubtasksInEpicHaveNew() {
        Epic epicN1 = new Epic("Купить подарки на НГ", "Составить список");
        Subtask subtaskEpicN1N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22),  Duration.ofMinutes(500));
        Subtask subtaskEpicN1N2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 12, 21, 22),  Duration.ofMinutes(500));
        taskManager.addNewEpicItem(epicN1);
        subtaskEpicN1N1.setEpicId(epicN1.getId());
        subtaskEpicN1N2.setEpicId(epicN1.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN1N1);
        taskManager.addNewSubtaskItem(subtaskEpicN1N2);
        taskManager.updateEpicInfo(epicN1);
        assertEquals(epicN1.getStatus(), StatusManager.Statuses.NEW, "Статусы не равны");
    }

    @Test
    void updateEpicInfoShouldBeDoneWhenAllSubtasksInEpicHaveDone() {
        Epic epicN3 = new Epic("Купить подарки на НГ", "Составить список");
        taskManager.addNewEpicItem(epicN3);
        Subtask subtaskEpicN3N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 13, 21, 22),  Duration.ofMinutes(500));
        Subtask subtaskEpicN3N2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 14, 21, 22),  Duration.ofMinutes(500));
        subtaskEpicN3N1.setEpicId(epicN3.getId());
        subtaskEpicN3N2.setEpicId(epicN3.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN3N1);
        taskManager.addNewSubtaskItem(subtaskEpicN3N2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtaskEpicN3N1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtaskEpicN3N2);
        assertEquals(epicN3.getStatus(), StatusManager.Statuses.DONE, "Статусы не равны");
    }

    @Test
    void updateEpicInfoShouldBeInProgressWhenOneSubtaskInEpicNewAndDone() {
        Epic epicN4 = new Epic("Купить подарки на НГ", "Составить список");
        taskManager.addNewEpicItem(epicN4);
        Subtask subtaskEpicN4N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 15, 21, 22), Duration.ofMinutes(500));
        Subtask subtaskEpicN4N2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 16, 21, 22), Duration.ofMinutes(500));
        subtaskEpicN4N1.setEpicId(epicN4.getId());
        subtaskEpicN4N2.setEpicId(epicN4.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN4N1);
        taskManager.addNewSubtaskItem(subtaskEpicN4N2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtaskEpicN4N1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.NEW, subtaskEpicN4N2);
        assertEquals(epicN4.getStatus(), StatusManager.Statuses.IN_PROGRESS, "Статусы не равны");
    }

    @Test
    void updateEpicInfoShouldBeInProgressWhenAllSubtasksInEpicHaveInProgress() {
        Epic epicN5 = new Epic("Купить подарки на НГ", "Составить список");
        taskManager.addNewEpicItem(epicN5);
        Subtask subtaskEpicN5N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 17, 21, 22), Duration.ofMinutes(500));
        Subtask subtaskEpicN5N2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 18, 21, 22), Duration.ofMinutes(500));
        subtaskEpicN5N1.setEpicId(epicN5.getId());
        subtaskEpicN5N2.setEpicId(epicN5.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN5N1);
        taskManager.addNewSubtaskItem(subtaskEpicN5N2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.IN_PROGRESS, subtaskEpicN5N1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.IN_PROGRESS, subtaskEpicN5N2);
        assertEquals(epicN5.getStatus(), StatusManager.Statuses.IN_PROGRESS, "Статусы не равны");
        assertEquals(epicN5.getDuration().toMinutes(), subtaskEpicN5N1.getDuration().toMinutes()
                + subtaskEpicN5N2.getDuration().toMinutes(), "Duration не равны");
        assertEquals(epicN5.getEndTime(), subtaskEpicN5N2.getEndTime(), "Время конца не равны");
    }

    @Test
    void deleteTaskByIdShouldBeExceptionWhenNoTasks() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.deleteTaskById(0));
        assertEquals(e.getMessage(), "Невозможно удалить task by id", "Ошибки не равны");
    }

    @Test
    void deleteTaskByIdShouldDeleteTaskAndDeleteFromHistoryIfExist() {
        Task task = new Task("", "");
        taskManager.addNewTaskItem(task);
        taskManager.getTaskById(task.getId());
        assertEquals(taskManager.getHistory().size(), 1, "Размер списка истории неверный");
        assertEquals(taskManager.getTasks().size(), 1, "Размер списка тасков неверный");
        taskManager.deleteTaskById(task.getId());
        MethodExecutionException eh = assertThrows(MethodExecutionException.class, () -> taskManager.getHistory()
                , "Исключение не бросается");
        assertEquals(eh.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не равны");
        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getTasks());
        assertEquals(e2.getMessage(), "Ошибка выполнения метода getTasks", "Ошибки не равны");
    }

    @Test
    void deleteEpicByIdShouldBeExceptionWhenNoEpics() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.deleteEpicById(0));
        assertEquals(e.getMessage(), "Невозможно удалить epic by id", "Ошибки не равны");
    }

    @Test
    void deleteEpicByIdShouldDeleteEpicAndItSubtasksAndDeleteFromHistoryIfExist() {
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        assertEquals(taskManager.getEpics().size(), 1, "Значение списка эпиков неверно");
        assertEquals(epic.getSubtaskIds().size(), 2, "Значение списков сабтасков неверно");
        taskManager.deleteEpicById(epic.getId());

        MethodExecutionException e2 = assertThrows(MethodExecutionException.class, () -> taskManager.getEpics());
        assertEquals(e2.getMessage(),"Ошибка выполнения метода getEpics", "Ошибки не равны");
        MethodExecutionException e3 = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtasks());
        assertEquals(e3.getMessage(),"Ошибка выполнения метода getSubtasks", "Ошибки не равны");
    }

    @Test
    void deleteSubtaskByIdShouldBeExceptionWhenNoSubtasks() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.deleteSubtaskById(0));
        assertEquals(e.getMessage(),"Невозможно удалить subtask by id", "Ошибки не равны");
    }

    @Test
    void deleteSubtaskByIdShouldDeleteSubtaskIfExistAndMakeCorrectEpicStatus() {
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtask1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.NEW, subtask2);
        assertEquals(epic.getStatus(), StatusManager.Statuses.IN_PROGRESS, "Статусы не равны");
        assertEquals(taskManager.getSubtasks().size(), 2, "Размеры листов не равны");
        taskManager.deleteSubtaskById(subtask1.getId());
        assertEquals(taskManager.getSubtasks().size(), 1, "Размеры листов не равны");
        assertEquals(epic.getSubtaskIds().size(), 1, "Размер листа сабтасков не совпадает");
        assertEquals(epic.getStatus(), StatusManager.Statuses.NEW, "Статусы не равны");
    }

    @Test
    void getSubtasksForEpicShouldBeExceptionWhenNoSubtasksForEpic() {
        Epic epic = new Epic("","");
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getSubtaskForEpic(epic));
        assertEquals(e.getMessage(), "У данного эпика: " + epic + " нет сабтасков", "Ошибки не равны");
    }

    @Test
    void getSubtasksForEpicShouldReturnCorrectListOfNotNullSubtasksIfExists() {
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        Subtask subtask2 = new Subtask("Купить попить", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 20, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        subtask2.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        taskManager.addNewSubtaskItem(subtask2);
        ArrayList<Integer> subsIds = new ArrayList<>();
        subsIds.add(subtask1.getId());
        subsIds.add(subtask2.getId());
        assertNotNull(epic.getSubtaskIds(), "Список Ids null");
        assertEquals(epic.getSubtaskIds().get(0), subsIds.get(0), "Листы не равны");
     }

    @Test
    void updateTaskShouldNotUpdateIfNotContains() {
        Task task = new Task("","");
        taskManager.updateTask(task);
        assertEquals(task.getId(), 0);
    }

    @Test
    void updateTaskShouldNotUpdateIfDateCompareToSomeTask() {
        Task taskN1 = new Task("Прогулка", "Сходить в лес",
                LocalDateTime.of(2023, Month.JANUARY, 16, 21, 22),  Duration.ofMinutes(10));
        Task taskN2 = new Task("Почитать", "Чтение перед сном"
                ,LocalDateTime.of(2023, Month.JANUARY, 16, 22, 22),  Duration.ofMinutes(100));
        taskManager.addNewTaskItem(taskN1);
        taskN2.setId(taskN1.getId());
        taskManager.updateTask(taskN2);
        Task testTask = taskManager.getTaskById(taskN1.getId());
        assertEquals(testTask, taskN2);
    }

    @Test
    void updateTaskShouldUpdateIfContainsAndNormalDate() {
        Task taskN1 = new Task("Прогулка", "Сходить в лес",
                LocalDateTime.of(2023, Month.JANUARY, 16, 21, 22),  Duration.ofMinutes(200));
        Task taskN2 = new Task("Почитать", "Чтение перед сном"
                ,LocalDateTime.of(2023, Month.JANUARY, 18, 22, 22),  Duration.ofMinutes(100));
        taskManager.addNewTaskItem(taskN1);
        taskN2.setId(taskN1.getId());
        taskManager.updateTask(taskN2);
        Task testTask = taskManager.getTaskById(taskN1.getId());
        assertEquals(testTask, taskN2);
    }

    @Test
    void updateEpicShouldNotUpdateIfNotContains() {
        Epic epic = new Epic("","");
        taskManager.updateEpic(epic);
        assertEquals(epic.getId(), 0);
    }

    @Test
    void updateEpicShouldUpdateIfContains() {
        Epic epic = new Epic("1"," а ");
        Epic epic2 = new Epic("2"," б ");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        epic2.setId(epic.getId());
        taskManager.updateEpic(epic2);
        Epic testEpic = taskManager.getEpicById(epic.getId());
        assertEquals(testEpic, epic2);
    }

    @Test
    void updateSubtasksShouldNotUpdateIfNotContainsAndNoEpicForThisSubtask() {
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        taskManager.updateSubtask(subtask1);
        assertEquals(subtask1.getId(), 0);
    }

    @Test
    void updateSubtaskShouldUpdateAndChangeEpicStatusIfEpicAndDateAreNormal() {
        Task taskN1 = new Task("Прогулка", "Сходить в лес",
                LocalDateTime.of(2023, Month.JANUARY, 22, 21, 22),  Duration.ofMinutes(10));
        taskManager.addNewTaskItem(taskN1);
        Epic epic1 = new Epic(" ", " ");
        taskManager.addNewEpicItem(epic1);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 7, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic1.getId());
        taskManager.addNewSubtaskItem(subtask1);
        Subtask subtask2 = new Subtask("Купить бантик", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 8, 21, 26),  Duration.ofMinutes(800));
        subtask2.setId(subtask1.getId());
        subtask2.setStatus(StatusManager.Statuses.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);
        Subtask testSubtask = taskManager.getSubtaskById(subtask1.getId());
        assertEquals(testSubtask, subtask2);
        assertEquals(epic1.getStatus(), StatusManager.Statuses.IN_PROGRESS);
    }

    @Test
    void updateSubtaskShouldNotUpdateIfDateCompareToSomeTask () {
        Task taskN1 = new Task("Прогулка", "Сходить в лес",
                LocalDateTime.of(2023, Month.JANUARY, 16, 21, 22),  Duration.ofMinutes(200));
        taskManager.addNewTaskItem(taskN1);
        Epic epic1 = new Epic(" ", " ");
        taskManager.addNewEpicItem(epic1);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic1.getId());
        taskManager.addNewSubtaskItem(subtask1);
        Subtask subtask2 = new Subtask("Купить бантик", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 16, 21, 26),  Duration.ofMinutes(100));
        subtask2.setId(subtask1.getId());
        taskManager.updateSubtask(subtask2);
        Subtask testSubtask = taskManager.getSubtaskById(subtask1.getId());
        assertNotEquals(testSubtask, subtask2);
    }

    @Test
    void setTaskStatusShouldBeExceptionWhenNotExist() {
        Task task = new Task("", "");
        MethodExecutionException e = assertThrows(MethodExecutionException.class
                , () -> taskManager.setTaskStatus(StatusManager.Statuses.NEW, task), "Исключение не выпадает");
        assertEquals(e.getMessage(), "Невозможно задать статус таску", "Ошибки не равны");
    }

    @Test
     void setTaskStatusShouldSetStatusExist() {
        Task task = new Task(" ", " ");
        taskManager.addNewTaskItem(task);
        taskManager.setTaskStatus(StatusManager.Statuses.IN_PROGRESS, task);
        assertEquals(task.getStatus(), StatusManager.Statuses.IN_PROGRESS, "Статусы не равны!");
    }

    @Test
    void setSubtaskStatusShouldBeExceptionWhenNotExist() {
       Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22),  Duration.ofMinutes(500));
        MethodExecutionException e = assertThrows(MethodExecutionException.class
                , () -> taskManager.setSubtaskStatus(StatusManager.Statuses.NEW, subtask1), "Исключение не выпадает");
        assertEquals(e.getMessage(), "Невозможно задать статус сабтаску", "Ошибки не равны");
    }

    @Test
    void setSubtaskStatusShouldSetStatusAndChangeEpicsIsExist() {
        Epic epic = new Epic("Задача", "Выполнить");
        taskManager.addNewEpicItem(epic);
        Subtask subtask1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 8, 21, 22),  Duration.ofMinutes(500));
        subtask1.setEpicId(epic.getId());
        taskManager.addNewSubtaskItem(subtask1);
        assertEquals(subtask1.getStatus(), StatusManager.Statuses.NEW, "Статусы не совпадают");
        assertEquals(epic.getStatus(), StatusManager.Statuses.NEW, "Статус эпика не совпадает");
        taskManager.setSubtaskStatus(StatusManager.Statuses.IN_PROGRESS, subtask1);
        assertEquals(subtask1.getStatus(), StatusManager.Statuses.IN_PROGRESS, "Статусы не совпадают");
        assertEquals(epic.getStatus(), StatusManager.Statuses.IN_PROGRESS, "Статус эпика не совпадает");
    }

    @Test
    void getHistoryShouldBeExceptionWhenIsEmpty() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class
        , () -> taskManager.getHistory(), "Исключение не было брошено");
        assertEquals(e.getMessage(), "Ошибка в выводе истории просмотра", "Ошибки не равны");
    }

    @Test
    void getHistoryShouldGetHistoryIfIsNotEmpty() {
        Task task1 = new Task("Задача1", "Описание1");
        Task task2 = new Task("Задача2", "Описание2");
        taskManager.addNewTaskItem(task1);
        taskManager.addNewTaskItem(task2);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        assertEquals(taskManager.getHistory().size(), 2);
    }

    @Test
    void getPrioritizedTasksShouldBeExceptionWhenTasksAreEmpty() {
        MethodExecutionException e = assertThrows(MethodExecutionException.class, () -> taskManager.getPrioritizedTasks()
        ,"Исключение не выкидывается");
        assertEquals(e.getMessage(), "Ошибка в выводе приоритета задач", "Ошибки не равны");
    }

    @Test
    void getPrioritizedTasksShouldGetPrioritizedTasksIfAreNotEmpty() {
        Task task1 = new Task("Почитать", "Чтение перед сном"
                ,LocalDateTime.of(2023, Month.JANUARY, 17, 21, 22), Duration.ofMinutes(10));
        Task task2 = new Task("Поиграть", "КС"
                ,LocalDateTime.of(2023, Month.JANUARY, 18, 21, 22), Duration.ofMinutes(10));
        Task task3 = new Task("Посмотреть", "Гром"
                ,LocalDateTime.of(2023, Month.JANUARY, 19, 21, 22), Duration.ofMinutes(10));
        Task task4 = new Task("Задача", "Сделать");
        taskManager.addNewTaskItem(task1);
        taskManager.addNewTaskItem(task2);
        taskManager.addNewTaskItem(task3);
        taskManager.addNewTaskItem(task4);
        Task firstTask = taskManager.getPrioritizedTasks().first();
        Task lastTask = taskManager.getPrioritizedTasks().last();
        assertEquals(firstTask, task1, "Задачи не равны");
        assertEquals(lastTask, task4, "Задачи не равны");
    }
}