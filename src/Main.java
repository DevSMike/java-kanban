import model.Epic;
import model.Subtask;
import model.Task;
import service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;



public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();


        Task taskN1 = new Task("Прогулка", "Сходить в лес",
                LocalDateTime.of(2023, Month.JANUARY, 16, 21, 22), 200);
        //вылетает
        Task taskN2 = new Task("Почитать", "Чтение перед сном"
                ,LocalDateTime.of(2023, Month.JANUARY, 17, 21, 22), 100);
        taskManager.addNewTaskItem(taskN1);
        taskManager.addNewTaskItem(taskN2);

        Epic epicN1 = new Epic("Купить подарки на НГ", "Составить список");
        Subtask subtaskEpicN1N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 11, 21, 22), 500);
        Subtask subtaskEpicN1N2 = new Subtask("Купить ленточку","Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 8, 21, 22),10);

        taskManager.addNewEpicItem(epicN1);
        subtaskEpicN1N1.setEpicId(epicN1.getId());
        subtaskEpicN1N2.setEpicId(epicN1.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN1N1);
        taskManager.addNewSubtaskItem(subtaskEpicN1N2);

        Epic epicN2 = new Epic("Продумать отдых", "Составить список");
        Subtask subtaskEpicN2N1 = new Subtask("Купить упаковку","Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 7, 19, 25),15);
        taskManager.addNewEpicItem(epicN2);
        subtaskEpicN2N1.setEpicId(epicN2.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN2N1);

        printAllTasksInfo(taskManager);

        taskManager.setTaskStatus(StatusManager.Statuses.IN_PROGRESS, taskN1);
        taskManager.setTaskStatus(StatusManager.Statuses.DONE, taskN2);
        taskManager.setSubtaskStatus(StatusManager.Statuses.IN_PROGRESS, subtaskEpicN1N1);
        taskManager.setSubtaskStatus(StatusManager.Statuses.DONE, subtaskEpicN2N1);

        System.out.println("_____ПОСЛЕ СМЕНЫ СТАТУСОВ_____");
        printAllTasksInfo(taskManager);

        System.out.println("_____ПОСЛЕ ОБНОВЛЕНИЯ  ЭПИКОВ_____");

        Epic epicN3 = new Epic("Тренировка", "План пробежки");
        epicN3.setId(epicN2.getId());
        epicN3.updateSubtaskIds(epicN2);
        taskManager.updateEpic(epicN3);
        epicN2 = null;

        Subtask subtaskEpicN3N1 = new Subtask("Купить бантик","Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 15, 21, 22),22);
        subtaskEpicN3N1.setId(subtaskEpicN2N1.getId());
        subtaskEpicN3N1.setStatus(subtaskEpicN2N1.getStatus());
        subtaskEpicN3N1.setEpicId(subtaskEpicN2N1.getEpicId());
        taskManager.updateSubtask(subtaskEpicN3N1);
        subtaskEpicN2N1 = null;

        printAllTasksInfo(taskManager);

        System.out.println();

        taskManager.getTaskById(taskN1.getId());
        taskManager.getTaskById(taskN2.getId());
        taskManager.getSubtaskById(subtaskEpicN1N1.getId());
        taskManager.getSubtaskById(subtaskEpicN3N1.getId());
        taskManager.getEpicById(epicN1.getId());
        taskManager.getEpicById(epicN1.getId());
        taskManager.getEpicById(epicN3.getId());
        taskManager.getTaskById(taskN1.getId());
        taskManager.getTaskById(taskN1.getId());
        taskManager.getTaskById(taskN2.getId());
        taskManager.getEpicById(epicN3.getId());

        Printer.printTaskHistory(taskManager.getHistory());
        System.out.println();

        taskManager.deleteTaskById(taskN2.getId());
        Printer.printTaskHistory(taskManager.getHistory());
    }

    public static void printAllTasksInfo(TaskManager inMemoryTaskManager) {
        System.out.println("Списки всех задач:");
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println("Списки всех эпиков:");
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println("Списки всех подзадач:");
        System.out.println(inMemoryTaskManager.getSubtasks());
    }


}
