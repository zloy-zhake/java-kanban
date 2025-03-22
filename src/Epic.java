import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    Epic(String name, String description, int id){
        super(name, description, id);
        this.subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    void addSubtask(Subtask subtask){
        this.subtaskIds.add(subtask.getId());
    }

    void removeSubtask(Subtask subtaskToRemove){
        this.subtaskIds.remove(subtaskToRemove.getId());
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + this.id +
                ", name='" + this.name + "'" +
                ", description='" + this.description + "'" +
                ", subtaskIds=" + this.subtaskIds +
                ", status=" + this.status +
                '}';
    }
}
