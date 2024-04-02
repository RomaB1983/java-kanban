package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    static TaskManager taskManager;
    static Epic epic1 = new Epic("Очень важный эпик", "Очень важный эпик");
    static Epic epic2 = new Epic("Переезд", "Переезд на дачу");

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
    }

    @Test
    void isPositiveEqualsEpicsWhenIdEquals() {
        epic2.setId(epic1.getId());
        assertEquals(epic1, epic2, "Id у epic одинаковые, но они не равны");
    }
}