package service;

import model.*;
import service.exceptions.ManagerSaveException;
import utility.StringWorker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String filename;

    private static final String HEADER = "id,type,name,status,description,epic,duration,starttime";

    public FileBackedTaskManager(String filename, boolean isLoadFromFile) {
        this.filename = filename;
        if (isLoadFromFile) {
            loadFromFile();
        }
    }

    private void loadFromFile() {
        int taskId;
        int maxTaskId = 0;

        for (String line : getRows()) {
            taskId = restoreTask(StringWorker.fromString(line));
            if (taskId > maxTaskId) {
                maxTaskId = taskId;
            }
        }
        if (maxTaskId > 0) {
            setSeqId(maxTaskId);
        }

        for (SubTask subTask : getSubTasksList()) {
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.getSubTaskIds().add(subTask.getId());
            }
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    private void save() {
        saveListToFile(getAllTasks());
    }

    private List<? extends Task> getAllTasks() {
        List<Task> result = new ArrayList<>(getTasksList().size() + getEpicsList().size()
                + getSubTasksList().size());
        result.addAll(getTasksList());
        result.addAll(getEpicsList());
        result.addAll(getSubTasksList());
        return result;
    }

    private void saveListToFile(List<? extends Task> list) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filename, StandardCharsets.UTF_8))) {
            fileWriter.write(HEADER + "\n");
            for (Task task : list) {
                fileWriter.write(StringWorker.toString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private int restoreTask(Task task) {
        switch (task.getType()) {
            case TASK -> {
                tasks.put(task.getId(), task);
                addToPriorityTasks(task);
            }
            case EPIC -> epics.put(task.getId(), (Epic) task);
            case SUBTASK -> {
                subTasks.put(task.getId(), (SubTask) task);
                addToPriorityTasks(task);
            }
        }

        return task.getId();

    }

    private List<String> getRows() {
        List<String> ret = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            boolean isHeader = true;
            while (fileReader.ready()) {
                if (isHeader) {
                    fileReader.readLine();
                    isHeader = false;
                    continue;
                }
                ret.add(fileReader.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        return ret;
    }
}