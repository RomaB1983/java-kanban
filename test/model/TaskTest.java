package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.interfaces.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    static TaskManager taskManager;
    static Task task1 = new Task("Вызвать такси", "Вызвать грузовое такси",
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-09-01T11:50:55"));
    static Task task2 = new Task("Вызвать такси2", "Вызвать грузовое такси2",
            Duration.ofMinutes(30),
            LocalDateTime.parse("2024-05-01T11:50:55"));

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
        taskManager.addTask(task1);
        taskManager.addTask(task2);
    }

    @Test
    void isPositiveEqualsTaskWhenIdEquals() {
        task2.setId(task1.getId());
        assertEquals(task1, task2, "Id у Task одинаковые, но они не равны");
    }
}