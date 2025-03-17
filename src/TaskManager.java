import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TaskManager {
    int nextTaskId;
    ArrayList<Task> tasks;
    ArrayList<Subtask> subtasks;
    ArrayList<Epic> epics;

//    HashMap<Epic, ArrayList<Subtask>> epics;

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public ArrayList<Epic> getEpics() {
        return this.epics;
    }

    public ArrayList<Subtask> getSubtasks(){
        return this.subtasks;
    }

    public void removeAllTasks(){
        this.tasks.clear();
    }

    public void removeAllEpics(){
        this.epics.clear();
    }

    public void removeAllSubtasks(){
        this.subtasks.clear();
    }

    public Task getTaskById(int id){
        for (Task task : this.tasks) {
            if (task.getId() == id){
                return task;
            }
        }
    }

    public Subtask getSubtaskById(int id){
        for (Subtask subtask : this.subtasks) {
            if (subtask.getId() == id){
                return subtask;
            }
        }
    }

    public Epic getEpicById(int id){
        for (Epic epic : this.epics) {
            if (epic.getId() == id){
                return epic;
            }
        }
    }
}
