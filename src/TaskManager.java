import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int nextTaskId;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Subtask> subtasks;
    HashMap<Integer, Epic> epics;

    public TaskManager() {
        nextTaskId = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    //    единая точка генерации id для всех видов задач
    private int getNextId() {
        this.nextTaskId++;
        return this.nextTaskId;
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

    public void removeAllEpics() {
        this.epics.clear();
    }

    public void removeAllSubtasks() {
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

    public int addTask(Task newTask) {
        int newTaskId = this.getNextId();
        newTask.setId(newTaskId);
        this.tasks.put(newTaskId, newTask);
        return newTask.getId();
    }

    //    при создании подзадачи необходимо:
//    - присвоить корректный id
//    - добавить её в HashMap подзадач
//    - добавить её в связанный эпик
//    - пересчитать статус связанного эпика
    public int addSubtask(Subtask newSubtask) {
        int newTaskId = this.getNextId();
        newSubtask.setId(newTaskId);
        this.subtasks.put(newTaskId, newSubtask);
        Epic connectedEpic = getEpicById(newSubtask.getEpicId());
        connectedEpic.addSubtaskId(newTaskId);
        this.updateEpicStatus(connectedEpic);
        return newSubtask.getId();
    }

    public int addEpic(Epic newEpic) {
        int newTaskId = this.getNextId();
        newEpic.setId(newTaskId);
        this.epics.put(newTaskId, newEpic);
        return newEpic.getId();
    }

    public void updateTask(Task newTask) {
        this.tasks.put(newTask.getId(), newTask);
    }

    //    при обновлении подзадачи необходимо:
//    - обновить её в HashMap'е подзадач
//    - пересчитать статус связанного эпика
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
//        если у эпика нет подзадач, то статус должен быть NEW.
        if (epicToUpdate.getSubtaskIds().isEmpty()) {
            epicToUpdate.setStatus(Status.NEW);
            return;
        }
        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;
        int i = 0;
        ArrayList<Integer> epicSubtaskIds = epicToUpdate.getSubtaskIds();
//        перебираем все подзадачи эпика и проверяем, если ли в нём:
//        - подзадачи со статусом NEW
//        - подзадачи со статусом IN_PROGRESS
//        - подзадачи со статусом DONE
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
//        если у эпика все подзадачи имеют статус NEW, то статус должен быть NEW.
            epicToUpdate.setStatus(Status.NEW);
        } else if (hasDone && !hasInProgress && !hasNew) {
//        если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE
            epicToUpdate.setStatus(Status.DONE);
        } else {
//        во всех остальных случаях статус должен быть IN_PROGRESS.
            epicToUpdate.setStatus(Status.IN_PROGRESS);
        }
    }

    public void removeTaskById(int idToRemove) {
        this.tasks.remove(idToRemove);
    }

    //    при удалении подзадачи необходимо:
//    - удалить её из связанного эпика
//    - пересчитать статус связанного эпика
//    - удалить её из HashMap'а подзадач
    public void removeSubtaskById(int idToRemove) {
        Subtask subtaskToRemove = this.getSubtaskById(idToRemove);
        Epic connectedEpic = this.getEpicById(subtaskToRemove.getEpicId());
        connectedEpic.removeSubtaskId(idToRemove);
        this.updateEpicStatus(connectedEpic);
        this.subtasks.remove(idToRemove);
    }

    //    При удалении эпика необходимо:
//    - удалить из HashMap'а подзадач все его подзадачи
//    - удалить его HashMap'а эпиков
    public void removeEpicById(int idToRemove) {
        Epic epicToRemove = this.getEpicById(idToRemove);
        if (epicToRemove != null) {
            ArrayList<Integer> subtaskIds = new ArrayList<>(epicToRemove.getSubtaskIds());
            for (int subtaskToRemoveId : subtaskIds) {
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
