import model.Epic;
import model.Subtask;
import model.Task;
import service.StatusManager;
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

        taskManager.addNewEpicItem(epicN1);
        subtaskEpicN1N1.setEpicId(epicN1.getId());
        subtaskEpicN1N2.setEpicId(epicN1.getId());
        taskManager.addNewSubtaskItem(subtaskEpicN1N1);
        taskManager.addNewSubtaskItem(subtaskEpicN1N2);

        Epic epicN2 = new Epic("Продумать отдых", "Составить список");
        Subtask subtaskEpicN2N1 = new Subtask("Погулять в парке", "После учебы");
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
        taskManager.updateEpic(epicN3);
        Subtask subtaskEpicN3N1 = new Subtask("Пробежка", "Тренировка в лесу");
        subtaskEpicN3N1.setId(subtaskEpicN2N1.getId());
        subtaskEpicN3N1.setStatus(subtaskEpicN2N1.getStatus());
        taskManager.updateSubtask(subtaskEpicN3N1);
        printAllTasksInfo(taskManager);
        taskManager.deleteTaskById(taskN2.getId());
        taskManager.deleteEpicById(epicN2.getId());

        System.out.println("_____ПОСЛЕ УДАЛЕНИЯ_____");
        printAllTasksInfo(taskManager);
    }

    public static void printAllTasksInfo(TaskManager taskManager) {
        System.out.println("Списки всех задач:");
        System.out.println(taskManager.getTasks());
        System.out.println("Списки всех эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println("Списки всех подзадач:");
        System.out.println(taskManager.getSubtasks());
    }


}
