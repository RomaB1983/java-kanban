package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.StringWorker;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    static FileBackedTaskManager taskManager;
    static FileBackedTaskManager taskManagerFromFile;
    static SubTask subTask1 = new SubTask("сабтаск1", "описание сабтаск1",
            Duration.ofMinutes(15),
            LocalDateTime.parse("2024-05-05T11:50:55"));
    static SubTask subTask2 = new SubTask("сабтаск2", "описание сабтаск2",
            Duration.ofMinutes(10),
            LocalDateTime.parse("2024-05-06T11:50:55"));
    static Task task1 = new Task("Вызвать такси", "Вызвать грузовое такси",
            Duration.ofMinutes(12),
            LocalDateTime.parse("2024-05-10T11:50:55"));
    static Epic epic1 = new Epic("Очень важный эпик", "Очень важный эпик",
            Duration.ofMinutes(25),
            LocalDateTime.parse("2024-05-07T11:50:55"));

    static final String path = "C:\\temp\\tasks.csv";

    @BeforeEach
    void beforeEach() {

        taskManager = Managers.getFileBackedManager(new File(path));

        taskManager.addTask(task1);
        taskManager.addEpic(epic1);

        subTask1.setStatus(TaskStatus.NEW);
        subTask1.setEpicId(epic1.getId());
        taskManager.addSubTask(subTask1);

        subTask2.setStatus(TaskStatus.NEW);
        subTask2.setEpicId(epic1.getId());
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask2);

        taskManagerFromFile = Managers.getRestoreFromFile(new File(path));
    }

    @Test
    void testToString() {
        assertEquals("1;TASK;Вызвать такси;NEW;Вызвать грузовое такси;12;2024-05-10T11:50:55;",
                StringWorker.toString(task1));
    }

    @Test
    void testFromString() {
        assertEquals(StringWorker
                .fromString("1;TASK;Вызвать такси;NEW;Вызвать грузовое такси;12;2024-05-10T11:50:55;"), task1);
    }

    @Test
    void isEaqualListOfTasksWnehLoadFromFileAndSaveFromAnothermanager() {
        assertEquals(taskManager.getTasksList(), taskManagerFromFile.getTasksList());
        assertEquals(taskManager.getEpicsList(), taskManagerFromFile.getEpicsList());
        assertEquals(taskManager.getSubTasksList(), taskManagerFromFile.getSubTasksList());
    }

}