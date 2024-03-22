package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int seqId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }


    public int getSeqId() {
        return ++seqId;
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

    public ArrayList<SubTask> getSubTasksByEpic(Integer epicId) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic.getSubTaskIds() != null) {
            for (Integer subTaskId : epic.getSubTaskIds()) {
                subTasks.add(this.subTasks.get(subTaskId));
            }

        }
        return subTasks;

    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            for (Integer subTaskId : epic.getSubTaskIds()) {
                subTasks.remove(subTaskId);
            }
        }
        epics.clear();
    }

    public void deleteSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
        }
        subTasks.clear();
    }

    public void addTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            task.setId(getSeqId());
            tasks.put(task.getId(), task);
        }

    }

    public void addEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            epic.setId(getSeqId());
            epics.put(epic.getId(), epic);
        }
    }

    public void addSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            subTask.setId(getSeqId());
            subTasks.put(subTask.getId(), subTask);

            Epic epic = epics.get(subTask.getEpicId());
            ArrayList<Integer> subTaskIds;
            if (epic.getSubTaskIds() == null) {
                subTaskIds = new ArrayList<>();
            } else {
                subTaskIds = epic.getSubTaskIds();
            }
            subTaskIds.add(subTask.getId());
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (tasks.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            setStatus(epics.get(subTask.getEpicId()));
        }
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
                default:
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
}
