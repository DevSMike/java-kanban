import model.Epic;
import model.Subtask;
import model.Task;
import service.*;

import javax.sound.midi.Soundbank;

public class MainForFiles {
    public static void main(String[] args) {

        FileBackedTasksManager fbManager = new FileBackedTasksManager();

        Main.printAllTasksInfo(fbManager);
        Printer.printTaskHistory(fbManager.getHistory());

        Task taskN1 = new Task("Прогулка", "Сходить в лес");
        Task taskN2 = new Task("Почитать", "Чтение перед сном");
        fbManager.addNewTaskItem(taskN1);
        fbManager.addNewTaskItem(taskN2);

        Epic epicN1 = new Epic("Купить подарки на НГ", "Составить список");
        Subtask subtaskEpicN1N1 = new Subtask("Купить коробку", "Заказать на Яндекс Маркете");
        Subtask subtaskEpicN1N2 = new Subtask("Купить ленточку","Заказать на Яндекс Маркете");
        fbManager.addNewEpicItem(epicN1);
        subtaskEpicN1N1.setEpicId(epicN1.getId());
        subtaskEpicN1N2.setEpicId(epicN1.getId());
        fbManager.addNewSubtaskItem(subtaskEpicN1N1);
        fbManager.addNewSubtaskItem(subtaskEpicN1N2);

        // ВЫЗЫВАЕМ ИСТОРИЮ ПРОСМОТРА
        fbManager.getEpicById(epicN1.getId());
        fbManager.getTaskById(taskN1.getId());
        fbManager.getTaskById(taskN2.getId());
        fbManager.getTaskById(taskN1.getId());
        fbManager.getSubtaskById(subtaskEpicN1N1.getId());
        fbManager.getSubtaskById(subtaskEpicN1N2.getId());


        System.out.println();
        Main.printAllTasksInfo(fbManager);
        Printer.printTaskHistory(fbManager.getHistory());

    }
}
