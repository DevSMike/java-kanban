package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @BeforeEach
    void createNewInMemoryTaskManager() {
        super.setTaskManager(taskManager);
    }

    @Test
    void createNewInMemoryTaskManagerShouldCreateThreeEmptyMaps() {
        InMemoryTaskManager testManager = new InMemoryTaskManager();
        assertNotNull(testManager);
        assertTrue(testManager.isMapsCreated());
    }

}
