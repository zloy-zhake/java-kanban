import org.junit.jupiter.api.Test;
import taskmanager.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    // проверьте, что наследники класса taskmanager.Task равны друг другу, если равен их id;
    @Test
    void testEpicsEquals() {
        ArrayList<Integer> subtasks1 = new ArrayList<>();
        subtasks1.add(1);
        ArrayList<Integer> subtasks2 = new ArrayList<>();
        subtasks2.add(2);
        Epic epic1 = new Epic("name1", "description1", 1, Status.NEW, subtasks1);
        Epic epic2 = new Epic("name2", "description2", 1, Status.IN_PROGRESS, subtasks2);
        assertEquals(epic1, epic2);
    }

    // Внутри эпиков не должно оставаться неактуальных id подзадач.
    @Test
    void testEpicsDoesNorContainDeletedSubtasks() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        int epic1_id = taskManager.addEpic(epic1);
        Subtask subtask_1_1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1-1.", epic1_id);
        int subtask_1_1_id = taskManager.addSubtask(subtask_1_1);
        Subtask subtask_1_2 = new Subtask("Подзадача 1-2", "Описание подзадачи 1-2.", epic1_id);
        int subtask_1_2_id = taskManager.addSubtask(subtask_1_2);
        assertTrue(epic1.getSubtaskIds().contains(subtask_1_1_id));
        assertTrue(epic1.getSubtaskIds().contains(subtask_1_2_id));
        taskManager.removeSubtaskById(subtask_1_2_id);
        assertTrue(epic1.getSubtaskIds().contains(subtask_1_1_id));
        assertFalse(epic1.getSubtaskIds().contains(subtask_1_2_id));
    }

    @Test
    void testTimeRelatedFields() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        int epic1_id = taskManager.addEpic(epic1);
        LocalDateTime st1StartTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        Duration st1Duration = Duration.ofHours(1);
        LocalDateTime st2StartTime = LocalDateTime.of(2025, 1, 1, 1, 0);
        Duration st2Duration = Duration.ofHours(1);
        Subtask subtask_1_1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1-1.", st1StartTime, st1Duration, epic1_id);
        int subtask_1_1_id = taskManager.addSubtask(subtask_1_1);
        Subtask subtask_1_2 = new Subtask("Подзадача 1-2", "Описание подзадачи 1-2.", st2StartTime, st2Duration, epic1_id);
        int subtask_1_2_id = taskManager.addSubtask(subtask_1_2);
        LocalDateTime expectedStartTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        Duration expectedDuration = Duration.ofHours(2);
        LocalDateTime expectedEndTime = LocalDateTime.of(2025, 1, 1, 2, 0);
        assertEquals(expectedStartTime, epic1.getStartTime());
        assertEquals(expectedDuration, epic1.getDuration());
        assertEquals(expectedEndTime, epic1.getEndTime());
    }


    // Для эпиков нужно проверить корректность расчёта статуса на основании состояния подзадач.
    @Test
    void testEpicStatuses() {
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        int epic1_id = taskManager.addEpic(epic1);

        // a. Все подзадачи со статусом NEW.
        Subtask subtask_1_1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1-1.", epic1_id);
        int subtask_1_1_id = taskManager.addSubtask(subtask_1_1);
        Subtask subtask_1_2 = new Subtask("Подзадача 1-2", "Описание подзадачи 1-2.", epic1_id);
        int subtask_1_2_id = taskManager.addSubtask(subtask_1_2);
        assertEquals(Status.NEW, epic1.getStatus());

        // b. Все подзадачи со статусом DONE.
        subtask_1_1.setStatus(Status.DONE);
        subtask_1_2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask_1_1);
        taskManager.updateSubtask(subtask_1_2);
        assertEquals(Status.DONE, epic1.getStatus());

        // c. Подзадачи со статусами NEW и DONE.
        subtask_1_1.setStatus(Status.NEW);
        taskManager.updateSubtask(subtask_1_1);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());

        // d. Подзадачи со статусом IN_PROGRESS.
        subtask_1_1.setStatus(Status.IN_PROGRESS);
        subtask_1_2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask_1_1);
        taskManager.updateSubtask(subtask_1_2);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    // проверьте, что объект taskmanager.Epic нельзя добавить в самого себя в виде подзадачи;
    // при добавлении эпика в эпик происходит ошибка компиляции
    // @Test
    // void testEpicCannotBeSubtask() {
    //     taskmanager.Managers taskManagerUtil = new taskmanager.Managers();
    //     taskmanager.TaskManager taskManager = taskManagerUtil.getDefault();
    //
    //     ArrayList<Integer> subtasks = new ArrayList<>();
    //     taskmanager.Epic epic = new taskmanager.Epic("name1", "description1", 1, taskmanager.Status.NEW, subtasks);
    //     taskmanager.Subtask wrongSubtask = new taskmanager.Subtask("name1", "description1", 1);
    //     taskManager.addEpic(epic);
    //     taskManager.addSubtask(epic);
    // }
}