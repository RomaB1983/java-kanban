package service.interfaces;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getTasksList();

    List<Epic> getEpicsList();

    List<SubTask> getSubTasksList();

    List<SubTask> getSubTasksByEpic(Integer epicId);

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}
