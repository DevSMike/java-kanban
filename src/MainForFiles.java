import model.Epic;
import model.Subtask;
import model.Task;
import service.*;


import java.io.File;
import java.io.IOException;

public class MainForFiles {

    private static final String PATH = "src/resources/tasksData.csv";
    public static void main(String[] args) throws IOException {

        File file = new File(PATH);
        File file2 = new File ("src/resources/tasksData2.csv");

        FileBackedTasksManager fbManager = new FileBackedTasksManager(file);
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

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Создаеём новый менеджер по данным старого!");
        FileBackedTasksManager newFb = new FileBackedTasksManager(file);
        try {
            newFb = FileBackedTasksManager.loadFromFile(file);
        } catch (IOException e) {
            System.out.println("Считать информацию из файла - невышло. Создался обычный экземпляр FileBackedManager");
        }
        Task task3 = new Task("Одых", "Украсить ёлку");
        newFb.addNewTaskItem(task3);
        Main.printAllTasksInfo(newFb);
        Printer.printTaskHistory(newFb.getHistory());

        System.out.println("\n Третий файл менеджер");
        FileBackedTasksManager tempFb = new FileBackedTasksManager(file2);
        Task taskN3 = new Task("Поиграть", "КС");
        tempFb.addNewTaskItem(taskN3);
        System.out.println(tempFb.getTasks());

    }
}
