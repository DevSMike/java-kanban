import model.Epic;
import model.Subtask;
import model.Task;
import service.*;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;


public class MainForFiles {

    private static final String PATH = "src/resources/tasksData.csv";
    public static void main(String[] args) throws IOException {

        File file = new File(PATH);
        File file2 = new File ("src/resources/tasksData2.csv");

        TaskManager taskManager = Managers.getDefault();

        Main.printAllTasksInfo(taskManager);
        Printer.printTaskHistory(taskManager.getHistory());

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

        Subtask subtaskEpicN1N3 = new Subtask("Купить упаковку","Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 7, 19, 25),15);

        Subtask subtaskEpicN1N4 = new Subtask("Купить бантик","Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 15, 21, 22),22);

        Subtask subtaskEpicN1N5 = new Subtask("Купить Попить","Заказать на Яндекс Маркете"
                ,LocalDateTime.of(2023, Month.JANUARY, 8, 21, 22), 50);
        taskManager.addNewEpicItem(epicN1);
        subtaskEpicN1N1.setEpicId(epicN1.getId());
        subtaskEpicN1N2.setEpicId(epicN1.getId());
        subtaskEpicN1N3.setEpicId(epicN1.getId());
        subtaskEpicN1N4.setEpicId(epicN1.getId());
        subtaskEpicN1N5.setEpicId(epicN1.getId());

        taskManager.addNewSubtaskItem(subtaskEpicN1N1);
        taskManager.addNewSubtaskItem(subtaskEpicN1N2);
        taskManager.addNewSubtaskItem(subtaskEpicN1N3);
        taskManager.addNewSubtaskItem(subtaskEpicN1N4);
        taskManager.addNewSubtaskItem(subtaskEpicN1N5);



        Task newTask = new Task("Задание", "Нужно сделать");
        taskManager.addNewTaskItem(newTask);

        Task newTask2 = new Task("Игра", "Нужно сделать");
        taskManager.addNewTaskItem(newTask2);

        Main.printAllTasksInfo(taskManager);
        System.out.println("ВЫВОД СОРИТРОВАННЫХ ПРИОРИТЕТОВ ЗАДАЧ");
        System.out.println(taskManager.getPrioritizedTasks());

        // ВЫЗЫВАЕМ ИСТОРИЮ ПРОСМОТРА
        taskManager.getEpicById(epicN1.getId());
        taskManager.getTaskById(taskN1.getId());
        taskManager.getTaskById(taskN2.getId());
        taskManager.getTaskById(taskN1.getId());
        taskManager.getSubtaskById(subtaskEpicN1N1.getId());
        taskManager.getSubtaskById(subtaskEpicN1N2.getId());
        taskManager.getSubtaskById(subtaskEpicN1N4.getId());


        System.out.println();
        Main.printAllTasksInfo(taskManager);
        Printer.printTaskHistory(taskManager.getHistory());

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Создаём новый менеджер по данным старого!");
      //  FileBackedTasksManager newFb = new FileBackedTasksManager(file);

         FileBackedTasksManager  newFb = FileBackedTasksManager.loadFromFile(file);

        Task task3 = new Task("Отдых", "Украсить ёлку"
                ,LocalDateTime.of(2023, Month.JANUARY, 17, 21, 22), 20000);
        newFb.addNewTaskItem(task3);
        Main.printAllTasksInfo(newFb);
        Printer.printTaskHistory(newFb.getHistory());

        System.out.println("\n Третий файл менеджер");
        FileBackedTasksManager tempFb = new FileBackedTasksManager(file2);
        Task taskN3 = new Task("Поиграть", "КС"
                ,LocalDateTime.of(2023, Month.JANUARY, 12, 13, 22), 120);
        tempFb.addNewTaskItem(taskN3);
        System.out.println(tempFb.getTasks());





    }
}
