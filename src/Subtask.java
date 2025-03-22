public class Subtask extends Task{
    private final Integer epicId;

    public Subtask(String name, String description, int id, Epic epic) {
        super(name, description, id);
        this.epicId = epic.getId();
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
