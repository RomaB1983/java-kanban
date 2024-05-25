package utility;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StringWorker {
    private static final CharSequence DELIMITER = ";";

    public static String toString(Task task) {
        if (task != null) {
            List<String> taskFields = new ArrayList<>();
            taskFields.add(String.valueOf(task.getId()));
            taskFields.add(task.getType().name());
            taskFields.add(task.getName());
            taskFields.add(task.getStatus().name());
            taskFields.add(task.getDescription());
            taskFields.add(String.valueOf(task.getDuration().toMinutes()));
            taskFields.add(String.valueOf(task.getStartTime()));
            if (task.getType() == TaskType.SUBTASK) {
                taskFields.add(String.valueOf(((SubTask) task).getEpicId()));
            }
            return String.join(DELIMITER, taskFields) + DELIMITER;
        }
        return null;
    }

    public static Task fromString(String value) {
        String[] vals = value.split(String.valueOf(DELIMITER));
        int id = Integer.parseInt(vals[0]);
        TaskType taskType = TaskType.valueOf(vals[1]);
        String name = vals[2];
        TaskStatus taskStatus = TaskStatus.valueOf(vals[3]);
        String description = vals[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(vals[5]));
        LocalDateTime startTime = LocalDateTime.parse(vals[6]);

        Task task = null;
        switch (taskType) {
            case TASK -> task = new Task(name, description,duration,startTime);
            case EPIC -> task = new Epic(name, description,duration,startTime);
            case SUBTASK -> {
                task = new SubTask(name, description,duration,startTime);
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
