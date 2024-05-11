package model;

public class SubTask extends Task {
    private Integer epicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public void setEpicId(Integer epicId) {
        if (epicId != id) {
            this.epicId = epicId;
        }
    }

    @Override
    public String toString() {
        return "model.SubTask{id =  " + id +
                ", name = '" + name + '\'' +
                ", description = '" + description + '\'' +
                ", status = '" + status.name() + '\'' +
                ", epic.id= '" + epicId + '\'' +
                "}";
    }
}
