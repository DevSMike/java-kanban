package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public class Printer {

    public static void printTaskHistory(ArrayList<Task> tasksHistory) {
        int indexNumber = 0;
        for (Task task : tasksHistory) {
            if (task instanceof Epic) {
                Epic epic = (Epic)task;
                System.out.print(++indexNumber+ " " + epic);
            } else if (task instanceof Subtask) {
                Subtask subtask = (Subtask)task;
                System.out.print(++indexNumber+ " " + subtask);
            } else {
                System.out.print(++indexNumber+ " " +task);
            }
        }
    }
}
