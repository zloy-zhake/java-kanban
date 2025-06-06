import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubtaskTest {

    // проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    void testSubtasksEquals() {
        Subtask subtask1 = new Subtask("name1", "description1", 1, Status.NEW, 1);
        Subtask subtask2 = new Subtask("name2", "description2", 1, Status.IN_PROGRESS, 2);
        assertEquals(subtask1, subtask2);
    }

    // проверьте, что объект Subtask нельзя сделать своим же эпиком;
    @Test()
    void testSubtaskCannotBeEpic() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();

        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        int epicId = taskManager.getEpics().getLast().getId();

        Subtask subtask = new Subtask("name1", "description1", epicId);
        taskManager.addSubtask(subtask);
        int subtaskId = taskManager.getSubtasks().getLast().getId();

        Subtask subtaskUpdated = new Subtask("name2", "description2", subtaskId, Status.NEW, subtaskId);

        assertThrows(NullPointerException.class, () -> taskManager.updateSubtask(subtaskUpdated));
    }

    @Test
    void testTimeRelatedFields() {
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        Duration duration = Duration.ofHours(1);
        Subtask subtask = new Subtask("name1", "description1", 1, Status.NEW, startTime, duration, 2);
        LocalDateTime newStartTime = LocalDateTime.of(2025, 6, 5, 12, 34);
        subtask.setStartTime(newStartTime);
        assertEquals(newStartTime, subtask.getStartTime());
        Duration newDuration = Duration.ofHours(2);
        subtask.setDuration(newDuration);
        assertEquals(newDuration, subtask.getDuration());
        LocalDateTime expectedEndTime = LocalDateTime.of(2025, 6, 5, 14, 34);
        assertEquals(expectedEndTime, subtask.getEndTime());
    }

    // Для подзадач необходимо дополнительно убедиться в наличии связанного эпика.
    @Test
    void testConnectedEpicExists() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();

        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        int epicId = taskManager.getEpics().getLast().getId();

        Subtask subtask = new Subtask("name1", "description1", epicId);
        taskManager.addSubtask(subtask);

        assertEquals(epicId, subtask.getEpicId());
    }
}