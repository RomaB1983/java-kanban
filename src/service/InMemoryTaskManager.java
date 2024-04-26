package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int seqId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    private int getSeqId() {
        return ++seqId;
    }

    @Override
    public ArrayList<Task> getTasksList() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            tasksList.add(getTask(id));
        }
        return tasksList;
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer id : epics.keySet()) {
            epicList.add(getEpic(id));
        }
        return epicList;
    }

    @Override
    public ArrayList<SubTask> getSubTasksList() {
        ArrayList<SubTask> subTasksList = new ArrayList<>();
        for (Integer id : subTasks.keySet()) {
            subTasksList.add(getSubTask(id));
        }
        return subTasksList;
    }

    @Override
    public ArrayList<SubTask> getSubTasksByEpic(Integer id) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        Epic epic = getEpic(id);
        if (epic != null) {
            for (Integer subTaskId : epic.getSubTaskIds()) {
                subTasks.add(getSubTask(subTaskId));
            }
        }
        return subTasks;
    }

    private void historyRemove(Collection<Integer> ids) {
        for (Integer id : ids) {
            historyManager.remove(id);
        }
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
        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
        }
        historyRemove(subTasks.keySet());
        subTasks.clear();
    }

    @Override
    public void addTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            task.setId(getSeqId());
            tasks.put(task.getId(), task);
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
            subTask.setId(getSeqId());
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.getSubTaskIds().add(subTask.getId());
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
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
            subTasks.put(subTask.getId(), subTask);
            setStatus(epics.get(subTask.getEpicId()));
        }
    }

    @Override
    public void deleteTask(int id) {
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

    @Override
    public void setStatus(Epic epic) {
        int cntNew = 0;
        int cntDone = 0;
        for (Integer subTaskId : epic.getSubTaskIds()) {
            switch (subTasks.get(subTaskId).getStatus()) {
                case NEW:
                    cntNew++;
                    break;
                case DONE:
                    cntDone++;
                    break;
            }
        }

        if (cntNew == epic.getSubTaskIds().size()) {
            if (epic.getStatus() != TaskStatus.NEW) {
                epic.setStatus(TaskStatus.NEW);
            }
            return;
        }
        if (cntDone == epic.getSubTaskIds().size()) {
            if (epic.getStatus() != TaskStatus.DONE) {
                epic.setStatus(TaskStatus.DONE);
            }
            return;
        }
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
