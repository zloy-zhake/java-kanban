import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int nextTaskId;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Subtask> subtasks;
    HashMap<Integer, Epic> epics;

    TaskManager() {
        nextTaskId = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public HashMap<Integer, Task> getTasks() {
        return this.tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return this.epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return this.subtasks;
    }

    public void removeAllTasks() {
        this.tasks.clear();
    }

    public void removeAllEpics()
    {
        this.epics.clear();
    }

    public void removeAllSubtasks()
    {
        this.subtasks.clear();
    }

    public Task getTaskById(int id) {
        if (this.tasks.containsKey(id)) {
            return this.tasks.get(id);
        }
        return null;
    }

    public Subtask getSubtaskById(int id) {
        if (this.subtasks.containsKey(id)) {
            return this.subtasks.get(id);
        }
        return null;
    }

    public Epic getEpicById(int id) {
        if (this.epics.containsKey(id)) {
            return this.epics.get(id);
        }
        return null;
    }

    public Task createTask(String name, String description) {
        Task newTask = new Task(name, description, this.nextTaskId);
        this.tasks.put(nextTaskId, newTask);
        this.nextTaskId++;
        return newTask;
    }

    public Subtask createSubtask(String name, String description, Epic epic) {
        Subtask newSubtask = new Subtask(name, description, this.nextTaskId, epic);
        this.subtasks.put(nextTaskId, newSubtask);
        epic.addSubtask(newSubtask);
        this.updateEpicStatus(epic);
        this.nextTaskId++;
        return newSubtask;
    }

    public Epic createEpic(String name, String description) {
        Epic newEpic = new Epic(name, description, this.nextTaskId);
        this.epics.put(nextTaskId, newEpic);
        this.nextTaskId++;
        return newEpic;
    }

    public void updateTask(Task newTask) {
        this.tasks.put(newTask.getId(), newTask);
    }

    public void updateSubtask(Subtask newSubtask) {
        this.subtasks.put(newSubtask.getId(), newSubtask);
        Epic epicToUpdate = this.getEpicById(newSubtask.getEpicId());
        this.updateEpicStatus(epicToUpdate);
    }

    public void updateEpic(Epic newEpic) {
        this.epics.put(newEpic.getId(), newEpic);
        this.updateEpicStatus(newEpic);
    }

    private void updateEpicStatus(Epic epicToUpdate) {
        if (epicToUpdate.getSubtaskIds().isEmpty()){
            epicToUpdate.setStatus(Status.NEW);
            return;
        }
        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;
        int i = 0;
        ArrayList<Integer> epicSubtaskIds = epicToUpdate.getSubtaskIds();
        while (!hasNew && !hasInProgress && !hasDone && i < epicSubtaskIds.size()) {
            Subtask curSubtask = this.getSubtaskById(epicSubtaskIds.get(i));
            switch (curSubtask.getStatus()) {
                case Status.NEW:
                    hasNew = true;
                    break;
                case Status.IN_PROGRESS:
                    hasInProgress = true;
                    break;
                case Status.DONE:
                    hasDone = true;
            }
            i++;
        }
        if (hasNew && !hasInProgress && !hasDone) {
            epicToUpdate.setStatus(Status.NEW);
        } else if (hasDone && !hasInProgress && !hasNew) {
            epicToUpdate.setStatus(Status.DONE);
        } else {
            epicToUpdate.setStatus(Status.IN_PROGRESS);
        }
    }


    public void removeTaskById(int idToRemove) {
        this.tasks.remove(idToRemove);
    }

    public void removeSubtaskById(int idToRemove) {
        this.subtasks.remove(idToRemove);
    }

    public void removeEpicById(int idToRemove) {
        Epic epicToRemove = this.getEpicById(idToRemove);
        if (epicToRemove != null) {
            for (int subtaskToRemoveId : epicToRemove.getSubtaskIds()) {
                this.removeSubtaskById(subtaskToRemoveId);
            }
            this.epics.remove(idToRemove);
        }
    }

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            epicSubtasks.add(this.getSubtaskById(subtaskId));
        }
        return epicSubtasks;
    }
}
