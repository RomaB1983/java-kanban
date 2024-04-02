package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.interfaces.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    static TaskManager taskManager;
    static SubTask subTask1 = new SubTask("Упаковать вещи", "Упаоквать вещи в коробку и перемотать скотчем");
    static Task task1 = new Task("Вызвать такси", "Вызвать грузовое такси");
    static Epic epic1 = new Epic("Очень важный эпик", "Очень важный эпик");

    @BeforeAll
    static void beforeAll() {
        taskManager = Managers.getDefault();
        taskManager.addSubTask(subTask1);
        taskManager.addEpic(epic1);
        taskManager.addTask(task1);

    }

    @Test
    void isPositiveFoundSubTaskById() {
        assertEquals(taskManager.getSubTask(subTask1.getId()), subTask1, "Subtask не найден по ID");
    }

    @Test
    void isPositiveFoundTaskById() {
        assertEquals(taskManager.getTask(task1.getId()), task1, "Task не найден по ID");
    }

    @Test
    void isPositiveFoundEpicById() {
        assertEquals(taskManager.getEpic(epic1.getId()), epic1, "Epic не найден по ID");
    }
}
