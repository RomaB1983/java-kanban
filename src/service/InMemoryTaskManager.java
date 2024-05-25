package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.exceptions.ManagerException;
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
                .map(this::getTask)
                .toList();
    }

    @Override
    public List<Epic> getEpicsList() {
        return epics.keySet().stream()
                .map(this::getEpic)
                .toList();

    }

    @Override
    public List<SubTask> getSubTasksList() {
        return subTasks.keySet().stream()
                .map(this::getSubTask)
                .toList();
    }

    @Override
    public List<SubTask> getSubTasksByEpic(Integer id) {
        return getEpic(id).getSubTaskIds().stream()
                .map(this::getSubTask)
                .toList();
    }

    private void historyRemove(Collection<Integer> ids) {
        ids.forEach(historyManager::remove);
    }

    @Override
    public void deleteTasks() {
        historyRemove(tasks.keySet());
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        historyRemove(subTasks.keySet());
        historyRemove(epics.keySet());
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteSubTasks() {
        epics.values().forEach(e -> e.getSubTaskIds().clear());
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
        LocalDateTime minDateTime = LocalDateTime.now();
        LocalDateTime maxDateTime = LocalDateTime.now();
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

    private void addToPriorityTasks(Task task) {
        if (task.getStartTime() != null)
            priorityTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return priorityTasks;
    }

    static Boolean isIntersect(Task task1, Task task2) {
        if (task1.getEndTime().isBefore(task2.getStartTime()) || task1 == task2) {
            return false;
        } else return !task1.getStartTime().isAfter(task2.getEndTime());
    }

    private Boolean isNotIntersectedTask(Task task) {
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
