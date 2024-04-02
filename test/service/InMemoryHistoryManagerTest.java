package service;

import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static TaskManager taskManager;

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
    }

    @Test
    void isPositiveSizeHistory10WhenGetTask11() {
        for (int i = 1; i < 12; i++) {
            taskManager.addTask(new Task("таск " + i, "таск " + i));
            taskManager.getTasksList();
        }
        assertEquals(10, taskManager.getHistory().size());
    }
}