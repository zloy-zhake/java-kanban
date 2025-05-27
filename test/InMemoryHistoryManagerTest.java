import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    // тест стал неактуальным, так как предыдущие версии больше не сохраняются
//    @Test
//    void historyManagerSavesVersions() {
//        Managers taskManagerUtil = new Managers();
//        TaskManager taskManager = taskManagerUtil.getDefault();
//
//        Task task = new Task("name v1", "description V1");
//        int taskId = taskManager.addTask(task);
//        Task taskV1 = taskManager.getTaskById(taskId);
//        Task taskUpdate = new Task("name v2", "description v2", taskId, Status.IN_PROGRESS);
//        taskManager.updateTask(taskUpdate);
//        Task taskV2 = taskManager.getTaskById(taskId);
//
//        ArrayList<Task> history = taskManager.getHistory();
//
//        assertEquals(history.get(0).getName(), taskV1.getName());
//        assertEquals(history.get(0).getDescription(), taskV1.getDescription());
//        assertEquals(history.get(0).getStatus(), taskV1.getStatus());
//
//        assertEquals(history.get(1).getName(), taskV2.getName());
//        assertEquals(history.get(1).getDescription(), taskV2.getDescription());
//        assertEquals(history.get(1).getStatus(), taskV2.getStatus());
//    }
}