import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void testFileBackedTaskManager() {
        Managers managers = new Managers();
        TaskManager taskManager = null;
        try {
            taskManager = managers.getFileBackedTaskManager(File.createTempFile("tasks", ".csv").getPath());
        } catch (IOException e) {
            System.out.println("Ошибка при создании временного файла для тестов.");
        }
        assertNotNull(taskManager);
        assertNotNull(taskManager.getTasks());
        assertNotNull(taskManager.getEpics());
        assertNotNull(taskManager.getSubtasks());
    }
}