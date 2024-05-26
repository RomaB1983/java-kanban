package service;

import org.junit.jupiter.api.Test;
import utility.StringWorker;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends AbstractTaskManagerTest {
    static FileBackedTaskManager taskManagerFromFile;

    static final String path = "C:\\temp\\tasks.csv";

    public FileBackedTaskManagerTest() {
        super(Managers.getFileBackedManager(new File(path)));
  }

    @Test
    void testToString() {
        assertEquals("1;TASK;Обновление задачи;NEW;Обновление задачи desc;20;2024-05-02T11:50:55;",
                StringWorker.toString(task1));
    }

    @Test
    void testFromString() {
        assertEquals(StringWorker
                .fromString("1;TASK;Вызвать такси;NEW;Вызвать грузовое такси;12;2024-05-10T11:50:55;"), task1);
    }

    @Test
    void isEaqualListOfTasksWnehLoadFromFileAndSaveFromAnothermanager() {
        taskManagerFromFile = Managers.getRestoreFromFile(new File(path));
        assertEquals(taskManager.getTasksList(), taskManagerFromFile.getTasksList());
        assertEquals(taskManager.getEpicsList(), taskManagerFromFile.getEpicsList());
        assertEquals(taskManager.getSubTasksList(), taskManagerFromFile.getSubTasksList());
    }
}