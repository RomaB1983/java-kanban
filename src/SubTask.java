public class SubTask extends Task {
    private Epic epic;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
        int cntNew = 0;
        int cntDone = 0;
        int cntInProgress = 0;
        for (SubTask subTask : epic.getSubTasks()) {
            switch (subTask.getStatus()) {
                case NEW:
                    cntNew++;
                    break;
                case DONE:
                    cntDone++;
                    break;
                case IN_PROGRESS:
                    cntInProgress++;
                    break;
            }
            if (cntInProgress > 0) {
                break;
            }
        }
        if (cntInProgress > 0) {
            if (epic.getStatus() != TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
            return;
        }
        if (cntNew == epic.getSubTasks().size()) {
            if (epic.getStatus() != TaskStatus.NEW) {
                epic.setStatus(TaskStatus.NEW);
            }
            return;
        }
        if (cntDone == epic.getSubTasks().size()) {
            if (epic.getStatus() != TaskStatus.DONE) {
                epic.setStatus(TaskStatus.DONE);
            }

        }


    }

    @Override
    public String toString() {
        String result = "SubTask{id =  " + id +
                ", name = '" + name + '\'' +
                ", description = '" + description + '\'' +
                ", status = '" + status.name() + '\'';
        if (epic != null) {
            result = result + ", epic.id= " + epic.getId();
        } else {
            result = result + ", epic= null";
        }
        return result + "}";


    }
}
