import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int seqId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }


    public static int getSeqId() {
        return ++seqId;
    }

    public static void setSeqId(int seqId) {
        TaskManager.seqId = seqId;
    }


    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubTasksList() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<SubTask> getSubTasksByEpic(Epic epic) {
        return epic.getSubTasks();
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
    }

    public void deleteSubTasks() {
        subTasks.clear();
    }

    public void add(Task task) {
        if (!tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }

    }

    public void add(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void add(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
        }
    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void update(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        epics.remove(id);
    }

    public void deleteSubTask(int id) {
        subTasks.remove(id);
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void setStatus(Task task) {
        task.setStatus(task.getStatus());
    }
}
