package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.exceptions.ManagerException;
import service.exceptions.NotFoundException;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int seqId;
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, SubTask> subTasks;
    protected final Set<Task> priorityTasks;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
        this.priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    private int getSeqId() {
        return ++seqId;
    }

    protected void setSeqId(int num) {
        seqId = num;
    }

    @Override
    public List<Task> getTasksList() {
        return tasks.keySet().stream()
                .map(tasks::get)
                .toList();
    }

    @Override
    public List<Epic> getEpicsList() {
        return epics.keySet().stream()
                .map(epics::get)
                .toList();

    }

    @Override
    public List<SubTask> getSubTasksList() {
        return subTasks.keySet().stream()
                .map(subTasks::get)
                .toList();
    }

    @Override
    public List<SubTask> getSubTasksByEpic(Integer id) {
        return epics.get(id).getSubTaskIds().stream()
                .map(subTasks::get)
                .toList();
    }

    private void historyRemove(Collection<Integer> ids) {
        ids.forEach(historyManager::remove);
    }

    private void prioritizedRemove(Collection<? extends Task> listTasks) {
        listTasks.forEach(priorityTasks::remove);
    }

    @Override
    public void deleteTasks() {
        historyRemove(tasks.keySet());
        prioritizedRemove(getTasksList());
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        prioritizedRemove(getSubTasksList());
        historyRemove(subTasks.keySet());
        historyRemove(epics.keySet());
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteSubTasks() {
        epics.values().forEach(e -> e.getSubTaskIds().clear());
        prioritizedRemove(getSubTasksList());
        historyRemove(subTasks.keySet());
        subTasks.clear();
    }

    @Override
    public void addTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            if (isNotIntersectedTask(task)) {
                task.setId(getSeqId());
                tasks.put(task.getId(), task);
                addToPriorityTasks(task);
            }
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            epic.setId(getSeqId());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            if (isNotIntersectedTask(subTask)) {
                subTask.setId(getSeqId());
                subTasks.put(subTask.getId(), subTask);
                addToPriorityTasks(subTask);
                Epic epic = epics.get(subTask.getEpicId());
                if (epic != null) {
                    epic.getSubTaskIds().add(subTask.getId());
                }
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (isNotIntersectedTask(task)) {
                tasks.put(task.getId(), task);
                addToPriorityTasks(task);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (tasks.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            if (isNotIntersectedTask(subTask)) {
                subTasks.put(subTask.getId(), subTask);
                addToPriorityTasks(subTask);
                setRequiredFields(epics.get(subTask.getEpicId()));
            }
        }
    }

    @Override
    public void deleteTask(int id) {
        priorityTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            prioritizedRemove(getSubTasksByEpic(id));
            epic.getSubTaskIds().clear();
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        priorityTasks.remove(subTasks.get(id));
        SubTask subtask = subTasks.remove(id);
        epics.get(subtask.getEpicId()).getSubTaskIds().remove((Integer) id);
        historyManager.remove(id);
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача по id = " + id + " не найдена");
        }
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    private void setRequiredFields(Epic epic) {
        int cntNew = 0;
        int cntDone = 0;
        long duration = 0;
        LocalDateTime minDateTime = LocalDateTime.MAX;
        LocalDateTime maxDateTime = LocalDateTime.MIN;
        TaskStatus taskStatus = TaskStatus.IN_PROGRESS;

        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTasks.get(subTaskId);

            duration += subTask.getDuration().toMinutes();
            if (subTask.getStartTime().isBefore(minDateTime)) minDateTime = subTask.getStartTime();
            if (subTask.getEndTime().isAfter(maxDateTime)) maxDateTime = subTask.getEndTime();

            switch (subTask.getStatus()) {
                case NEW -> cntNew++;
                case DONE -> cntDone++;
            }
        }
        if (cntNew == epic.getSubTaskIds().size()) {
            if (epic.getStatus() != TaskStatus.NEW) {
                taskStatus = TaskStatus.NEW;
            }
        } else if (cntDone == epic.getSubTaskIds().size()) {
            if (epic.getStatus() != TaskStatus.DONE) {
                taskStatus = TaskStatus.DONE;
            }
        }

        epic.setDuration(Duration.ofMinutes(duration));
        epic.setStartTime(minDateTime);
        epic.setEndTime(maxDateTime);
        epic.setStatus(taskStatus);

    }

    void addToPriorityTasks(Task task) {
        if (task.getStartTime() != null) {
            priorityTasks.remove(task);
            priorityTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(priorityTasks);
    }

    private Boolean isIntersect(Task task1, Task task2) {
        return (task1 != task2) &&
                (task1.getStartTime().isEqual(task2.getEndTime()) || task1.getStartTime().isBefore(task2.getEndTime())) &&
                (task1.getEndTime().isEqual(task2.getStartTime()) || task1.getEndTime().isAfter(task2.getStartTime()));
    }

    private Boolean isNotIntersectedTask(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) return false;
        Optional<Task> optionalTask = priorityTasks.stream()
                .filter(t -> isIntersect(t, task))
                .findFirst();
        if (optionalTask.isPresent()) {
            throw new ManagerException("Задача \"" + task +
                    "\"\n пересекается с задачей \n\"" + optionalTask.get() +
                    "\" \n по времени выполнения. Поменяйте время");
        }
        return true;
    }
}
