package service.interfaces;

import model.Task;

import java.util.Collection;
import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    void remove(Collection<Integer> ids);

    List<Task> getHistory();

}
