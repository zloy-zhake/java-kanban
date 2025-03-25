import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds;

    Epic(String name, String description){
        super(name, description);
        this.subtaskIds = new ArrayList<>();
    }

    Epic(String name, String description, int id, Status status, ArrayList<Integer> subtaskIds){
        super(name, description, id, status);
        this.subtaskIds = subtaskIds;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    void addSubtaskId(int subtaskId){
        this.subtaskIds.add(subtaskId);
    }

    void removeSubtaskId(int subtaskIdToRemove){
        this.subtaskIds.remove((Integer) subtaskIdToRemove);
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
