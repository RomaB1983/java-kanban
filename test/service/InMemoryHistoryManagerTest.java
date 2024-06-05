package service;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static HistoryManager historyManager;
    static Task task = new Task("таск ", "таск ", Duration.ofMinutes(15),
            LocalDateTime.parse("2024-05-05T11:50:55"));
    static Task task2 = new Task("таск2 ", "таск2 ",Duration.ofMinutes(20),
            LocalDateTime.parse("2024-05-06T11:50:55"));
    static Task task3 = new Task("таск3 ", "таск3 ",Duration.ofMinutes(30),
            LocalDateTime.parse("2024-05-10T11:50:55"));

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        task.setId(1);
        task2.setId(2);
        task3.setId(3);

        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
    }

    @Test
    void isShoulbeEqualSizeHistory10WhenGetTask10() {
        for (int i = 1; i < 11; i++) {
            Task task = new Task("таск " + i, "таск " + i,Duration.ofMinutes(12),
                    LocalDateTime.parse("2024-05-10T11:50:55"));
            task.setId(i);
            historyManager.add(task);
        }
        assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    void isShoulbeRemoveInHistoryWhenRemoveById() {
        historyManager.remove(task.getId());
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void isShoulbeOneTaskInHistoryWhenAddTaskTwice() {
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    void isShoulbeRemoveInHistoryWhenRemoveLast() {

        historyManager.remove(task3.getId());
        assertEquals(task, historyManager.getHistory().get(0));
        assertEquals(task2, historyManager.getHistory().get(1));
    }

    @Test
    void isShoulbeRemoveInHistoryWhenRemoveFirst() {
        historyManager.remove(task.getId());
        assertEquals(task2, historyManager.getHistory().get(0));
        assertEquals(task3, historyManager.getHistory().get(1));
    }

    @Test
    void isShoulbeRemoveInHistoryWhenRemoveMedium() {
        historyManager.remove(task2.getId());
        assertEquals(task, historyManager.getHistory().get(0));
        assertEquals(task3, historyManager.getHistory().get(1));
    }
}