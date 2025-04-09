import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;

    Epic(String name, String description) {
        super(name, description);
        this.subtaskIds = new ArrayList<>();
    }

    Epic(String name, String description, int id, Status status, ArrayList<Integer> subtaskIds) {
        super(name, description, id, status);
        this.subtaskIds = subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    void addSubtaskId(int subtaskId) {
        this.subtaskIds.add(subtaskId);
    }

    void removeSubtaskId(int subtaskIdToRemove) {
        this.subtaskIds.remove((Integer) subtaskIdToRemove);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + "'" +
                ", description='" + this.getDescription() + "'" +
                ", subtaskIds=" + this.subtaskIds +
                ", status=" + this.getStatus() +
                '}';
    }
}
