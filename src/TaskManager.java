import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    int addTask(Task newTask);

    int addSubtask(Subtask newSubtask);

    int addEpic(Epic newEpic);

    void updateTask(Task newTask);

    void updateSubtask(Subtask newSubtask);

    void updateEpic(Epic newEpic);

    void removeTaskById(int idToRemove);

    void removeSubtaskById(int idToRemove);

    void removeEpicById(int idToRemove);

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);

    void updateEpicStatus(Epic epicToUpdate);

    ArrayList<Task> getHistory();
}
