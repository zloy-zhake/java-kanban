import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void historyManagerSavesVersions() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();
        HistoryManager historyManager = taskManager.getHistoryManager();

        Task task = new Task("name v1", "description V1");
        int taskId = taskManager.addTask(task);
        Task taskV1 = taskManager.getTaskById(taskId);
        Task taskUpdate = new Task("name v2", "description v2", taskId, Status.IN_PROGRESS);
        taskManager.updateTask(taskUpdate);
        Task taskV2 = taskManager.getTaskById(taskId);

        ArrayList<Task> history = historyManager.getHistory();

        assertEquals(history.get(0).getName(), taskV1.getName());
        assertEquals(history.get(0).getDescription(), taskV1.getDescription());
        assertEquals(history.get(0).getStatus(), taskV1.getStatus());

        assertEquals(history.get(1).getName(), taskV2.getName());
        assertEquals(history.get(1).getDescription(), taskV2.getDescription());
        assertEquals(history.get(1).getStatus(), taskV2.getStatus());
    }
}