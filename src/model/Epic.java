package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskIds;

    public Epic(String name, String description) {
        super(name, description);
        subTaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    @Override
    public String toString() {
        return "model.Epic{id =  " + id +
                ", name = '" + name + '\'' +
                ", description = '" + description + '\'' +
                ", status = '" + status.name() + '\'' +
                ", subTaskIds = " + subTaskIds + " }";
    }
}
