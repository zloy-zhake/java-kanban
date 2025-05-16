import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    // проверьте, что наследники класса Task равны друг другу, если равен их id;
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

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    // при добавлении эпика в эпик происходит ошибка компиляции
    // @Test
    // void testEpicCannotBeSubtask() {
    //     Managers taskManagerUtil = new Managers();
    //     TaskManager taskManager = taskManagerUtil.getDefault();
    //
    //     ArrayList<Integer> subtasks = new ArrayList<>();
    //     Epic epic = new Epic("name1", "description1", 1, Status.NEW, subtasks);
    //     Subtask wrongSubtask = new Subtask("name1", "description1", 1);
    //     taskManager.addEpic(epic);
    //     taskManager.addSubtask(epic);
    // }

}