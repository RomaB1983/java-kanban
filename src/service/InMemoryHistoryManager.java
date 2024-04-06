package service;

import model.Task;
import service.interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int SIZE_HISTORY_LIST = 10;
    private final List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>(SIZE_HISTORY_LIST);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() == SIZE_HISTORY_LIST) {
                history.remove(0);
            }
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
