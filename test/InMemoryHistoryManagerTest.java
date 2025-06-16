import org.junit.jupiter.api.Test;
import taskmanager.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryHistoryManagerTest {
    @Test
    void historyManagerLinksTask() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();
        Task task = new Task("name v1", "description V1");
        int taskId = taskManager.addTask(task);
        Task taskV1 = taskManager.getTaskById(taskId);

        ArrayList<Task> history = taskManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.getLast());
    }

    @Test
    void historyManagerRemovesTask() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();
        Task task = new Task("name v1", "description V1");
        int taskId = taskManager.addTask(task);
        Task taskV1 = taskManager.getTaskById(taskId);
        taskManager.removeTaskById(taskId);

        ArrayList<Task> history = taskManager.getHistory();

        assertEquals(0, history.size());
    }

    @Test
    void historyManagerDoesNotDuplicateTask() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();
        Task task = new Task("name v1", "description V1");
        int taskId = taskManager.addTask(task);
        Task taskV1 = taskManager.getTaskById(taskId);
        Task task_update = new Task(
                task.getName(), task.getDescription(), task.getId(), Status.IN_PROGRESS
        );
        taskManager.updateTask(task_update);
        Task taskV2 = taskManager.getTaskById(taskId);

        ArrayList<Task> history = taskManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.getLast());
    }

    @Test
    void removeFromEmptyHistoryDoesNotChangeIt() {
        Managers taskManagerUtil = new Managers();
        HistoryManager historyManager = taskManagerUtil.getDefaultHistory();
        int lenBefore = historyManager.getHistory().size();
        historyManager.remove(5);
        int lenAfter = historyManager.getHistory().size();
        assertEquals(lenBefore, lenAfter);
    }

    @Test
    void removeFromBeginning() {
        Managers taskManagerUtil = new Managers();
        HistoryManager historyManager = taskManagerUtil.getDefaultHistory();
        Task task1 = new Task("name1", "description1", 1, Status.NEW);
        Task task2 = new Task("name2", "description2", 2, Status.NEW);
        Task task3 = new Task("name3", "description3", 3, Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(1);
        assertFalse(historyManager.getHistory().contains(task1));
    }

    @Test
    void removeFromMiddle() {
        Managers taskManagerUtil = new Managers();
        HistoryManager historyManager = taskManagerUtil.getDefaultHistory();
        Task task1 = new Task("name1", "description1", 1, Status.NEW);
        Task task2 = new Task("name2", "description2", 2, Status.NEW);
        Task task3 = new Task("name3", "description3", 3, Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);
        assertFalse(historyManager.getHistory().contains(task2));
    }

    @Test
    void removeFromEnd() {
        Managers taskManagerUtil = new Managers();
        HistoryManager historyManager = taskManagerUtil.getDefaultHistory();
        Task task1 = new Task("name1", "description1", 1, Status.NEW);
        Task task2 = new Task("name2", "description2", 2, Status.NEW);
        Task task3 = new Task("name3", "description3", 3, Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(3);
        assertFalse(historyManager.getHistory().contains(task3));
    }
}