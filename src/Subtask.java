public class Subtask extends Task{
    Epic epic;

    public Subtask(String name, String description, int id, Epic epic) {
        super(name, description, id);
        this.epic = epic;
        this.epic.updateEpicStatus();
    }

    @Override
    public void setStatus(Status status){
        super.setStatus(status);
        this.epic.updateEpicStatus();
    }
}
