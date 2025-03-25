public class Subtask extends Task{
    private Integer epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + this.epicId +
                ", id=" + this.id +
                ", name='" + this.name + "'" +
                ", description='" + this.description + "'" +
                ", status=" + this.status +
                '}';
    }
}
