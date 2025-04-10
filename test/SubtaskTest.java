import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
}