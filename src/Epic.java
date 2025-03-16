import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> subtasks;

    Epic(String name, String description, int id){
        super(name, description, id);
    }

    void addSubtask(Subtask subtask){
        this.subtasks.add(subtask);
        this.updateEpicStatus();
    }

    void removeSubtask(Subtask subtaskToRemove){
        for (int i = 0; i < this.subtasks.size(); i++) {
            if (this.subtasks.get(i).equals(subtaskToRemove)) {
                this.subtasks.remove(i);
                break;
            }
        }
        this.updateEpicStatus();
    }

    public void updateEpicStatus() {
        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;
        int i = 0;
        while (!hasNew && !hasInProgress && !hasDone && i < this.subtasks.size()) {
            switch (this.subtasks.get(i).getStatus()) {
                case Status.NEW:
                    hasNew = true;
                    break;
                case Status.IN_PROGRESS:
                    hasInProgress = true;
                    break;
                case Status.DONE:
                    hasDone = true;
            }
            i++;
        }
        if (hasNew && !hasInProgress && !hasDone) {
            this.setStatus(Status.NEW);
        } else if (hasDone && !hasInProgress && !hasNew) {
            this.setStatus(Status.DONE);
        } else {
            this.setStatus(Status.IN_PROGRESS);
        }
    }
}
