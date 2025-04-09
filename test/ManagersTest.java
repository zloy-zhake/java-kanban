import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    void testDefaultTaskManager() {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();
        assertNotNull(taskManager);
        assertNotNull(taskManager.getTasks());
        assertNotNull(taskManager.getEpics());
        assertNotNull(taskManager.getSubtasks());
    }

    @Test
    void testDefaultHistory() {
        Managers managers = new Managers();
        HistoryManager historyManager = managers.getDefaultHistory();
        assertNotNull(historyManager);
        assertNotNull(historyManager.getHistory());
    }
}