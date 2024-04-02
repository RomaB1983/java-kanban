package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    static TaskManager taskManager;
    static Task task1 = new Task("Вызвать такси", "Вызвать грузовое такси");
    static Task task2 = new Task("Вызвать такси2", "Вызвать грузовое такси2");

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