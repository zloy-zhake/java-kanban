import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TaskManager {
    int nextTaskId;
    ArrayList<Task> tasks;
    HashMap<Epic, ArrayList<Subtask>> epics;

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Set<Epic> getEpics() {
        return epics.keySet();
    }

    public ArrayList<Subtask> getSubtasks(){
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        for (Epic epic : epics.keySet()) {
            ArrayList<Subtask> epicSubtasks = epics.get(epic);
            for (Subtask subtask : epicSubtasks) {
                allSubtasks.add(subtask);
            }
        }
        return allSubtasks;
    }

    public void removeAllTasks(){
        this.tasks.clear();
    }

    public void removeAllEpics(){
        this.epics.clear();
    }

    public void removeAllSubtasks(){
        for (Epic epic : epics.keySet()) {
            ArrayList<Subtask> epicSubtasks = epics.get(epic);
            epicSubtasks.clear();
        }
    }
}
