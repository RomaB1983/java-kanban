package service;

import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getRestoreFromFile(File file) {
        return  new FileBackedTaskManager(file.getPath(),true);
    }

    public static FileBackedTaskManager getFileBackedManager(File file) {
        return new FileBackedTaskManager(file.getPath(),false);
    }
}
