import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task taskN1 = new Task("Прогулка", "Сходить в лес");
        Task taskN2 = new Task("Почитать", "Чтение перед сном");
        taskManager.addNewTaskItem(taskN1);
        taskManager.addNewTaskItem(taskN2);

        Epic epicN1 = new Epic("Купить подарки на НГ", "Составить список");
        Subtask subtaskEpicN1N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете");
        Subtask subtaskEpicN1N2 = new Subtask("Купить ленточку","Заказать на Яндекс Маркете");
        taskManager.addNewSubtaskItem(subtaskEpicN1N1);
        taskManager.addNewSubtaskItem(subtaskEpicN1N2);
        epicN1.setItems(subtaskEpicN1N1.getTaskId());
        epicN1.setItems(subtaskEpicN1N2.getTaskId());
        taskManager.addNewEpicItem(epicN1);

        Epic epicN2 = new Epic("Продумать отдых", "Составить список");
        Subtask subtaskEpicN2N1 = new Subtask("Погулять в парке", "После учебы");
        taskManager.addNewSubtaskItem(subtaskEpicN2N1);
        epicN2.setItems(subtaskEpicN2N1.getTaskId());
        taskManager.addNewEpicItem(epicN2);

        printAllTasksInfo(taskManager);

        taskManager.setTaskStatus(Task.Statuses.IN_PROGRESS, taskN1);
        taskManager.setTaskStatus(Task.Statuses.DONE, taskN2);
        taskManager.setSubtaskStatus(Task.Statuses.IN_PROGRESS, subtaskEpicN1N1);
        taskManager.setSubtaskStatus(Task.Statuses.DONE, subtaskEpicN2N1);

        System.out.println("_____ПОСЛЕ СМЕНЫ СТАТУСОВ_____");
        printAllTasksInfo(taskManager);

        taskManager.deleteTaskById(taskN2.getTaskId());
        taskManager.deleteEpicById(epicN2.getTaskId());

        System.out.println("_____ПОСЛЕ УДАЛЕНИЯ_____");
        printAllTasksInfo(taskManager);
    }

    public static void printAllTasksInfo(TaskManager taskManager) {
        System.out.println("Списки всех задач:");
        System.out.println(taskManager.printAllTasks());
        System.out.println("Списки всех эпиков:");
        System.out.println(taskManager.printAllEpics());
        System.out.println("Списки всех подзадач:");
        System.out.println(taskManager.printAllSubtasks());
    }


}
