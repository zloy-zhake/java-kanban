import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryTaskManagerTest {

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;

    static TaskManager taskManager;
    static Task task1;
    static int taskId1;
    static Epic epic1;
    static int epicId1;
    static Subtask subtask1;
    static int subtaskId1;

    @BeforeAll
    static void initManagers() {
        Managers taskManagerUtil = new Managers();
        taskManager = taskManagerUtil.getDefault();

        task1 = new Task("task name 1", "task description 1");
        taskId1 = taskManager.addTask(task1);

        epic1 = new Epic("epic name 1", "epic description 1");
        epicId1 = taskManager.addEpic(epic1);

        subtask1 = new Subtask("subtask name 1", "subtask description 1", epicId1);
        subtaskId1 = taskManager.addSubtask(subtask1);
    }

    @Test
    void testAddTask() {
        Task task = new Task("name", "description");
        taskManager.addTask(task);
        Task addedTask = taskManager.getTasks().getLast();

        assertEquals(task.getName(), addedTask.getName());
        assertEquals(task.getDescription(), addedTask.getDescription());
    }

    @Test
    void testGetTaskById() {
        Task task = taskManager.getTaskById(taskId1);
        assertEquals(task.getName(), task1.getName());
        assertEquals(task.getDescription(), task1.getDescription());
    }

    @Test
    void testAddEpic() {
        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        Epic addedEpic = taskManager.getEpics().getLast();

        assertEquals(epic.getName(), addedEpic.getName());
        assertEquals(epic.getDescription(), addedEpic.getDescription());
    }

    @Test
    void testGetEpicById() {
        Epic epic = taskManager.getEpicById(epicId1);
        assertEquals(epic.getName(), epic1.getName());
        assertEquals(epic.getDescription(), epic1.getDescription());
    }

    @Test
    void testAddSubtask() {
        Subtask subtask = new Subtask("name", "description", epicId1);
        taskManager.addSubtask(subtask);
        Subtask addedSubtask = taskManager.getSubtasks().getLast();

        assertEquals(subtask.getName(), addedSubtask.getName());
        assertEquals(subtask.getDescription(), addedSubtask.getDescription());
    }

    @Test
    void testGetSubtaskById() {
        Subtask subtask = taskManager.getSubtaskById(subtaskId1);
        assertEquals(subtask.getName(), subtask1.getName());
        assertEquals(subtask.getDescription(), subtask1.getDescription());
    }

    // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    void testTaskIds() {
        Task task = new Task("task name 1", "task description 1");
        int id = taskManager.addTask(task);

        Task taskWithConflictingId = new Task("task name 2", "task description 2", id, Status.IN_PROGRESS);
        int new_id = taskManager.addTask(taskWithConflictingId);

        assertNotEquals(id, new_id);
    }

    // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void testTaskDoesNotChangeAfterAdding() {
        Task task = new Task("task name", "task description");
        int id = taskManager.addTask(task);
        Task taskAdded = taskManager.getTaskById(id);

        assertEquals(task.getName(), taskAdded.getName());
        assertEquals(task.getDescription(), taskAdded.getDescription());
        assertEquals(task.getStatus(), taskAdded.getStatus());
    }

}