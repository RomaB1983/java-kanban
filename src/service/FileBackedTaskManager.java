package service;

import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String filename;
    private static final CharSequence DELIMITER = ";";
    private static final String HEADER = "id,type,name,status,description,epic";

    public FileBackedTaskManager(String filename) {
        this.filename = filename;
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return super.getTasksList();
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return super.getEpicsList();
    }

    @Override
    public ArrayList<SubTask> getSubTasksList() {
        return super.getSubTasksList();
    }

    @Override
    public ArrayList<SubTask> getSubTasksByEpic(Integer id) {
        return super.getSubTasksByEpic(id);
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
    }

    @Override
    public void deleteSubTasks() {
        super.deleteSubTasks();
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

    @Override
    public Task getTask(int id) {
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(int id) {
        return super.getEpic(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        return super.getSubTask(id);
    }

    @Override
    public void setStatus(Epic epic) {
        super.setStatus(epic);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
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
                fileWriter.write(toString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileTaskManager = new FileBackedTaskManager(file.getPath());
        int taskId;
        int maxTaskId = 0;
        for (String line : getRows(file)) {
            taskId = restoreTasks(fromString(line), fileTaskManager);
            if (taskId > maxTaskId) {
                maxTaskId = taskId;
            }
        }
        if (maxTaskId > 0) {
            fileTaskManager.setSeqId(maxTaskId);
        }

        for (SubTask subTask : fileTaskManager.getSubTasksList()) {
            Epic epic = fileTaskManager.epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.getSubTaskIds().add(subTask.getId());
            }
        }

        return fileTaskManager;
    }

    private static int restoreTasks(Task task, FileBackedTaskManager fileManager) {
        if (task instanceof Epic) {
            fileManager.epics.put(task.getId(), (Epic) task);
        } else if (task instanceof SubTask) {
            fileManager.subTasks.put(task.getId(), (SubTask) task);
        } else {
            fileManager.tasks.put(task.getId(), task);
        }
        return task.getId();

    }

    private static List<String> getRows(File file) {
        List<String> ret = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
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

    public String toString(Task task) {
        if (task != null) {
            List<String> taskFields = new ArrayList<>();
            TaskType taskType;
            if (task instanceof Epic) {
                taskType = TaskType.EPIC;
            } else if (task instanceof SubTask) {
                taskType = TaskType.SUBTASK;
            } else {
                taskType = TaskType.TASK;
            }

            taskFields.add(String.valueOf(task.getId()));
            taskFields.add(taskType.name());
            taskFields.add(task.getName());
            taskFields.add(task.getStatus().name());
            taskFields.add(task.getDescription());
            if (taskType == TaskType.SUBTASK) {
                taskFields.add(String.valueOf(((SubTask) task).getEpicId()));
            }
            return String.join(DELIMITER, taskFields) + DELIMITER;
        }
        return null;
    }

    static Task fromString(String value) {
        String[] vals = value.split(String.valueOf(DELIMITER));
        int id = Integer.parseInt(vals[0]);
        TaskType taskType = TaskType.valueOf(vals[1]);
        String name = vals[2];
        TaskStatus taskStatus = TaskStatus.valueOf(vals[3]);
        String description = vals[4];

        Task task = null;
        switch (taskType) {
            case TASK -> task = new Task(name, description);
            case EPIC -> task = new Epic(name, description);
            case SUBTASK -> {
                task = new SubTask(name, description);
                ((SubTask) task).setEpicId(Integer.parseInt(vals[5]));
            }
        }
        if (task != null) {
            task.setId(id);
            task.setStatus(taskStatus);
        }
        return task;
    }
}
