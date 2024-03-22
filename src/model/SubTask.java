package model;

public class SubTask extends Task {
    private Integer epicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
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
