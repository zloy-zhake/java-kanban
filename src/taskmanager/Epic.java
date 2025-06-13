package taskmanager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description, int id, Status status, ArrayList<Integer> subtaskIds) {
        super(name, description, id, status);
        this.subtaskIds = subtaskIds;
    }

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description, int id, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, id, status, startTime, duration);
        this.subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description, int id, Status status, LocalDateTime startTime, Duration duration, ArrayList<Integer> subtaskIds) {
        super(name, description, id, status, startTime, duration);
        this.subtaskIds = subtaskIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
        return "taskmanager.Epic{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + "'" +
                ", description='" + this.getDescription() + "'" +
                ", subtaskIds=" + this.subtaskIds +
                ", status=" + this.getStatus() +
                ", startTime=" + this.getStartTime() +
                ", duration=" + this.getDuration() +
                '}';
    }
}