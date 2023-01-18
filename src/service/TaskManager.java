package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface TaskManager {

     void addNewTaskItem(Task task);

     void addNewEpicItem(Epic epic) ;

     void addNewSubtaskItem(Subtask subtask);

     ArrayList<Task> getTasks();

     ArrayList<Epic> getEpics();

     ArrayList<Subtask> getSubtasks();

     void deleteTasks();

     void deleteEpics();

     void deleteSubtasks();

     Task getTaskById(int id);

     Epic getEpicById(int id);

     Subtask getSubtaskById(int id);

     void updateEpicInfo(Epic epic);

     void deleteTaskById(int id);

     void deleteEpicById(int id);

     void deleteSubtaskById(int id);

     ArrayList<Subtask> getSubtaskForEpic(Epic epic);

     void updateTask(Task task);

     void updateEpic(Epic epic);

     void updateSubtask(Subtask subtask);

     void setTaskStatus(StatusManager.Statuses status, Task task);

     void setSubtaskStatus(StatusManager.Statuses status, Subtask subtask);

     List<Task> getHistory();

     TreeSet<Task> getPrioritizedTasks();
}
