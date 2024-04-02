package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    static TaskManager taskManager;
    static SubTask subTask1 = new SubTask("Упаковать вещи", "Упаоквать вещи в коробку и перемотать скотчем");
    static SubTask subTask2 = new SubTask("Собрать тарелки", "Собрать тарелки в стопки");

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
    }

    @Test
    void isPositiveEqualsSubTaskWhenIdEquals() {
        subTask2.setId(subTask1.getId());
        assertEquals(subTask1, subTask2, "Id у subTask одинаковые, но они не равны");
    }

    @Test
    void isNegativeMakeSubTaskToEpic() {
        subTask1.setEpicId(subTask1.getId());
        assertEquals(subTask1, subTask2, "Id у subTask одинаковые, но они не равны");
    }
}