package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    static FileBackedTaskManager taskManager;
    static SubTask subTask1 = new SubTask("сабтаск1",
            "описание сабтаск1");
    static SubTask subTask2 = new SubTask("сабтаск2",
            "описание сабтаск2");
    static Task task1 = new Task("Вызвать такси", "Вызвать грузовое такси");
    static Epic epic1 = new Epic("Очень важный эпик", "Очень важный эпик");

    @BeforeEach
    void beforeEach() {

        try {
            taskManager = Managers.loadFromFile(File.createTempFile("tasks", "tmp"));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        taskManager.addTask(task1);
        taskManager.addEpic(epic1);

        subTask1.setStatus(TaskStatus.NEW);
        subTask1.setEpicId(epic1.getId());
        taskManager.addSubTask(subTask1);

        subTask2.setStatus(TaskStatus.NEW);
        subTask2.setEpicId(epic1.getId());
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask2);
    }

    @Test
    void testToString() {
        assertEquals("1;TASK;Вызвать такси;NEW;Вызвать грузовое такси;", taskManager.toString(task1));
    }

    @Test
    void testFromString() {
        assertEquals(FileBackedTaskManager.fromString("1;TASK;Вызвать такси;NEW;Вызвать грузовое такси;"), task1);
    }
}