package model;

import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Integer> subTaskIds;

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTasksIds(ArrayList<Integer> subTasksIds) {
        this.subTaskIds = subTasksIds;
    }

    @Override
    public String toString() {
        return "model.Epic{id =  " + id +
                ", name = '" + name + '\'' +
                ", description = '" + description + '\'' +
                ", status = '" + status.name() + '\'' +
                ", subTaskIds = " + subTaskIds + " }"
                ;
    }
}
