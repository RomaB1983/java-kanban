package service;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static TaskManager taskManager;
    static HistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
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

    @Test
    void isShoulbeRemoveInHistoryWhenRemoveLast() {
        Task task = new Task("таск ", "таск " );
        Task task2 = new Task("таск2 ", "таск2 " );
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.getTask(task.getId());
        taskManager.deleteTask(task2.getId());
        assertEquals(task, taskManager.getHistory().get(0));
    }

    @Test
    void isShoulbeRemoveInHistoryWhenRemoveFirst() {
        Task task = new Task("таск ", "таск " );
        Task task2 = new Task("таск2 ", "таск2 " );
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.getTask(task.getId());
        taskManager.deleteTask(task.getId());
        assertEquals(task2, taskManager.getHistory().get(0));
    }

    @Test
    void isShoulbeRemoveInHistoryWhenRemoveMedium() {
        Task task = new Task("таск ", "таск " );
        Task task2 = new Task("таск2 ", "таск2 " );
        Task task3 = new Task("таск3 ", "таск3 " );
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTask(task.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.deleteTask(task2.getId());
        assertEquals(task, taskManager.getHistory().get(0));
        assertEquals(task3, taskManager.getHistory().get(1));
    }
}