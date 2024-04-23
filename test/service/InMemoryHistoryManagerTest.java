package service;

import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static TaskManager taskManager;
    static HistoryManager historyManager;

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void isShoulbeEqualSizeHistory10WhenGetTask10() {
        for (int i = 1; i < 11; i++) {
            Task task = new Task("таск " + i, "таск " + i);
            taskManager.addTask(task);
            taskManager.getTask(task.getId());
        }
        assertEquals(10, taskManager.getHistory().size());
    }

    @Test
    void isShoulbeRemoveInHistoryWhenRemoveById() {
        Task task = new Task("таск ", "таск " );
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void isShoulbeOneTaskInHistoryWhenAddTaskTwice() {
        Task task = new Task("таск ", "таск " );
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        taskManager.getTask(task.getId());
        taskManager.getTask(task.getId());
        assertEquals(1, taskManager.getHistory().size());
    }

}