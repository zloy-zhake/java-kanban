import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    int nextTaskId;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Subtask> subtasks;
    HashMap<Integer, Epic> epics;
    HistoryManager historyManager;
    TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.nextTaskId = 0;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        Managers managers = new Managers();
        this.historyManager = managers.getDefaultHistory();
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
    public void removeAllTasks() {
        this.tasks.clear();
        this.updatePrioritizedTasks();
    }

    @Override
    public void removeAllEpics() {
        this.epics.clear();
        this.subtasks.clear();
        this.updatePrioritizedTasks();
    }

    // В методе для удаления подзадач необходимо очищать все подзадачи из коллекции, в которой они хранятся,
    // а также удалять их из всех эпиков, с которыми они связаны,
    // при этом необходимо обновить статус соответствующих эпиков
    @Override
    public void removeAllSubtasks() {
        HashSet<Epic> epicsToUpdate = new HashSet<>();
        for (Subtask subtask : this.getSubtasks()) {
            Epic connectedEpic = this.getEpicById(subtask.getEpicId());
            epicsToUpdate.add(connectedEpic);
            int subtaskId = subtask.getId();
            connectedEpic.removeSubtaskId(subtaskId);
            this.historyManager.remove(subtaskId);
        }
        for (Epic epic : epicsToUpdate) {
            this.updateEpicStatus(epic);
        }
        this.subtasks.clear();
        this.updatePrioritizedTasks();
    }

    @Override
    public Task getTaskById(int id) {
        if (this.tasks.containsKey(id)) {
            Task taskToGet = this.tasks.get(id);
            this.historyManager.add(taskToGet);
            return taskToGet;
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (this.subtasks.containsKey(id)) {
            Subtask subtaskToGet = this.subtasks.get(id);
            this.historyManager.add(subtaskToGet);
            return subtaskToGet;
        }
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        if (this.epics.containsKey(id)) {
            Epic epicToGet = this.epics.get(id);
            this.historyManager.add(epicToGet);
            return epicToGet;
        }
        return null;
    }

    @Override
    public int addTask(Task newTask) {
        newTask.setId(this.getNextId());
        this.tasks.put(newTask.getId(), newTask);
        this.updatePrioritizedTasks();
        return newTask.getId();
    }

    // при создании подзадачи необходимо:
    // - присвоить корректный id
    // - добавить её в HashMap подзадач
    // - добавить её в связанный эпик
    // - пересчитать статус связанного эпика
    @Override
    public int addSubtask(Subtask newSubtask) {
        newSubtask.setId(this.getNextId());
        this.subtasks.put(newSubtask.getId(), newSubtask);
        Epic connectedEpic = getEpicById(newSubtask.getEpicId());
        connectedEpic.addSubtaskId(newSubtask.getId());
        this.updateEpicStatus(connectedEpic);
        this.updatePrioritizedTasks();
        return newSubtask.getId();
    }

    @Override
    public int addEpic(Epic newEpic) {
        newEpic.setId(this.getNextId());
        this.epics.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    @Override
    public void updateTask(Task newTask) {
        this.tasks.put(newTask.getId(), newTask);
        this.updatePrioritizedTasks();
    }

    // при обновлении подзадачи необходимо:
    // - обновить её в HashMap'е подзадач
    // - пересчитать статус связанного эпика
    @Override
    public void updateSubtask(Subtask newSubtask) {
        this.subtasks.put(newSubtask.getId(), newSubtask);
        Epic epicToUpdate = this.getEpicById(newSubtask.getEpicId());
        this.updateEpicStatus(epicToUpdate);
        this.updatePrioritizedTasks();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        this.epics.put(newEpic.getId(), newEpic);
        this.updateEpicStatus(newEpic);
    }

    @Override
    public void removeTaskById(int idToRemove) {
        this.tasks.remove(idToRemove);
        this.historyManager.remove(idToRemove);
        this.updatePrioritizedTasks();
    }

    // при удалении подзадачи необходимо:
    // - удалить её из связанного эпика
    // - пересчитать статус связанного эпика
    // - удалить её из HashMap'а подзадач
    @Override
    public void removeSubtaskById(int idToRemove) {
        Subtask subtaskToRemove = this.getSubtaskById(idToRemove);
        Epic connectedEpic = this.getEpicById(subtaskToRemove.getEpicId());
        connectedEpic.removeSubtaskId(idToRemove);
        this.updateEpicStatus(connectedEpic);
        this.subtasks.remove(idToRemove);
        this.historyManager.remove(idToRemove);
        this.updatePrioritizedTasks();
    }

    // При удалении эпика необходимо:
    // - удалить из HashMap'а подзадач все его подзадачи
    // - удалить его HashMap'а эпиков
    @Override
    public void removeEpicById(int idToRemove) {
        Epic epicToRemove = this.getEpicById(idToRemove);
        if (epicToRemove != null) {
            ArrayList<Integer> subtaskIds = new ArrayList<>(epicToRemove.getSubtaskIds());
            for (int subtaskToRemoveId : subtaskIds) {
                this.removeSubtaskById(subtaskToRemoveId);
            }
            this.epics.remove(idToRemove);
            this.historyManager.remove(idToRemove);
            this.updatePrioritizedTasks();
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            epicSubtasks.add(this.getSubtaskById(subtaskId));
        }
        return epicSubtasks;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return this.historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(this.prioritizedTasks);
    }

    // внутри updateEpicStatus() вызываем updateEpicDurationInfo(),
    // так как оба методы должны вызывать в одних и тех же ситуациях
    protected void updateEpicStatus(Epic epicToUpdate) {
        // если у эпика нет подзадач, то статус должен быть NEW.
        if (epicToUpdate.getSubtaskIds().isEmpty()) {
            epicToUpdate.setStatus(Status.NEW);
            return;
        }
        boolean hasNew = false;
        boolean hasInProgress = false;
        boolean hasDone = false;
        int i = 0;
        ArrayList<Integer> epicSubtaskIds = epicToUpdate.getSubtaskIds();
        // перебираем все подзадачи эпика и проверяем, если ли в нём:
        // - подзадачи со статусом NEW
        // - подзадачи со статусом IN_PROGRESS
        // - подзадачи со статусом DONE
//        while (!hasNew && !hasInProgress && !hasDone && i < epicSubtaskIds.size()) {
        while (i < epicSubtaskIds.size()) {
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
            // если у эпика все подзадачи имеют статус NEW, то статус должен быть NEW.
            epicToUpdate.setStatus(Status.NEW);
        } else if (hasDone && !hasInProgress && !hasNew) {
            // если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE
            epicToUpdate.setStatus(Status.DONE);
        } else {
            // во всех остальных случаях статус должен быть IN_PROGRESS.
            epicToUpdate.setStatus(Status.IN_PROGRESS);
        }
        this.updateEpicDurationInfo(epicToUpdate);
    }

    protected void updateEpicDurationInfo(Epic epicToUpdate){
        this.updateEpicStartTime(epicToUpdate);
        this.updateEpicEndTime(epicToUpdate);
        this.updateEpicDuration(epicToUpdate);
    }

    protected void updateEpicStartTime(Epic epicToUpdate) {
        epicToUpdate.setStartTime(
                epicToUpdate.getSubtaskIds().stream()
                        .map(this::getSubtaskById)
                        .map(Task::getStartTime)
                        .filter(Objects::nonNull)
                        .min(LocalDateTime::compareTo)
                        .orElse(null)
        );
    }

    protected void updateEpicEndTime(Epic epicToUpdate) {
        epicToUpdate.setEndTime(
                epicToUpdate.getSubtaskIds().stream()
                        .map(this::getSubtaskById)
                        .map(Task::getEndTime)
                        .filter(Objects::nonNull)
                        .max(LocalDateTime::compareTo)
                        .orElse(null)
        );
    }

    protected void updateEpicDuration(Epic epicToUpdate) {
        if (epicToUpdate.getStartTime() == null || epicToUpdate.getEndTime() == null) {
            epicToUpdate.setDuration(null);
        } else {
            epicToUpdate.setDuration(Duration.between(epicToUpdate.getStartTime(), epicToUpdate.getEndTime()));
        }
    }

    // единая точка генерации id для всех видов задач
    private int getNextId() {
        this.nextTaskId++;
        return this.nextTaskId;
    }

    private void updatePrioritizedTasks() {
        for (Task task : this.getTasks()) {
            if (task.getStartTime() != null) {
                this.prioritizedTasks.remove(task);
                this.prioritizedTasks.add(task);
            }
        }
        for (Subtask subtask : this.getSubtasks()) {
            if (subtask.getStartTime() != null) {
                this.prioritizedTasks.remove(subtask);
                this.prioritizedTasks.add(subtask);
            }
        }
    }
}
