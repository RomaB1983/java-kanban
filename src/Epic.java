import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, ArrayList<SubTask> subTasks) {
        super(name, description);
        this.subTasks = subTasks;
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toString() {
        return "Epic{id =  " + id +
                ", name = '"+ name + '\''+
                ", description = '" + description + '\''+
                ", status = '" + status.name()+ '\'' +
                ", subTasks = " + subTasks + " }"
                ;
    }
}
