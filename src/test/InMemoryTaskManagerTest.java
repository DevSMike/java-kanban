package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    void createNewInMemoryTaskManager() {
        super.setTaskManager(taskManager);

    }
    @Test
    void addNewTaskItem() {
        super.addNewTaskItem();
    }

    @Test
    void addNewEpicItem() {
        super.addNewEpicItem();
    }

    @Test
    void addNewSubtaskItem() {
       super.addNewSubtaskItem();
    }

    @Test
    void getTasks() {
        super.getTasks();
    }

    @Test
    void getEpics() {
        super.getEpics();
    }


    @Test
    void getSubtasks() {
        super.getSubtasks();
    }

    @Test
    void deleteTasks() {
        super.deleteTasks();
    }

    @Test
    void deleteEpics() {
        super.deleteEpics();
    }

    @Test
    void deleteSubtasks() {
        super.deleteSubtasks();

    }

    @Test
    void getTaskById() {
        super.getTaskById();
    }

    @Test
    void getEpicById() {
       super.getEpicById();
    }

    @Test
    void getSubtaskById() {
      super.getSubtaskById();

    }

    @Test
    void updateEpicInfo() {
        super.updateEpicInfo();
    }

    @Test
    void deleteTaskById() {
        super.deleteTaskById();
    }

    @Test
    void deleteEpicById() {
        super.deleteEpicById();
    }

    @Test
    void deleteSubtaskById() {
        super.deleteSubtaskById();
    }

    @Test
    void getSubtaskForEpic() {
      super.getSubtaskForEpic();

    }

    @Test
    void updateTask() {
        super.updateTask();
    }

    @Test
    void updateEpic() {
        super.updateEpic();
    }

    @Test
    void updateSubtask() {
        super.updateSubtask();
    }

    @Test
    void setTaskStatus() {
        super.setTaskStatus();
    }

    @Test
    void setSubtaskStatus() {
        super.setSubtaskStatus();
    }

    @Test
    void getHistory() {
        super.getHistory();
    }

    @Test
    void getPrioritizedTasks() {
        super.getPrioritizedTasks();
    }
}