import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final String name;
    private final String description;
    private int id;
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, int id, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (this.getStartTime() != null && this.getDuration() != null) {
            return this.startTime.plus(this.duration);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(this.id, otherTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + this.id +
                ", name='" + this.name + "'" +
                ", description='" + this.description + "'" +
                ", status=" + this.status +
                ", startTime=" + this.startTime +
                ", duration=" + this.duration +
                '}';
    }

    public boolean isOverlapped(Task otherTask) {
        LocalDateTime task1Start = this.getStartTime();
        LocalDateTime task1End = this.getEndTime();
        LocalDateTime task2Start = otherTask.getStartTime();
        LocalDateTime task2End = otherTask.getEndTime();
        if (task1Start == null || task1End == null || task2Start == null || task2End == null) {
            return false;
        }
        if (task1Start.isAfter(task2Start) && task1Start.isBefore(task2End)) {
            return true;
        }
        if (task2Start.isAfter(task1Start) && task2Start.isBefore(task1End)) {
            return true;
        }
        return false;
    }
}