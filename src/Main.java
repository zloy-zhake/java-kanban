public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        System.out.println();
        Managers taskManagerUtil = new Managers();
        TaskManager taskManager = taskManagerUtil.getDefault();

        System.out.println("Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.");
        System.out.println("Распечатайте списки эпиков, задач и подзадач через System.out.println(..)");
        System.out.println();
        Task task1 = new Task("Задача 1", "Описание задачи 1.");
        int task1_id = taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание задачи 2.");
        int task2_id = taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        int epic1_id = taskManager.addEpic(epic1);
        Subtask subtask_1_1 = new Subtask("Подзадача 1-1", "Описание подзадачи 1-1.", epic1_id);
        int subtask_1_1_id = taskManager.addSubtask(subtask_1_1);
        Subtask subtask_1_2 = new Subtask("Подзадача 1-2", "Описание подзадачи 1-2.", epic1_id);
        int subtask_1_2_id = taskManager.addSubtask(subtask_1_2);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");
        int epic2_id = taskManager.addEpic(epic2);
        Subtask subtask_2_1 = new Subtask("Подзадача 2-1", "Описание подзадачи 2-1.", epic2_id);
        int subtask_2_1_id = taskManager.addSubtask(subtask_2_1);

        System.out.println("Epics: " + taskManager.getEpics());
        System.out.println();
        System.out.println("Tasks: " + taskManager.getTasks());
        System.out.println();
        System.out.println("Subtasks: " + taskManager.getSubtasks());
        System.out.println();

        printHistory(taskManager);
        System.out.println();

        System.out.println("Измените статусы созданных объектов, распечатайте их. ");
        System.out.println(
                "Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач."
        );
        System.out.println();
        Task task1_managed = taskManager.getTaskById(task1_id);
        Task task1_update = new Task(
                task1_managed.getName(), task1_managed.getDescription(), task1_managed.getId(), Status.IN_PROGRESS
        );
        taskManager.updateTask(task1_update);
        System.out.println("Task 1: " + taskManager.getTaskById(task1_id));
        System.out.println();
        task1_update = new Task(
                task1_managed.getName(), task1_managed.getDescription(), task1_managed.getId(), Status.DONE
        );
        taskManager.updateTask(task1_update);
        System.out.println("Task 1: " + taskManager.getTaskById(task1_id));
        System.out.println();

        printHistory(taskManager);
        System.out.println();

        Epic epic1_managed = taskManager.getEpicById(epic1_id);
        Epic epic1_update = new Epic(
                epic1_managed.getName(),
                epic1_managed.getDescription(),
                epic1_managed.getId(),
                Status.IN_PROGRESS,
                epic1_managed.getSubtaskIds());
        taskManager.updateEpic(epic1_update);
        System.out.println("Epic 1: " + epic1);
        System.out.println();
        epic1_update = new Epic(
                epic1_managed.getName(),
                epic1_managed.getDescription(),
                epic1_managed.getId(),
                Status.DONE,
                epic1_managed.getSubtaskIds());
        taskManager.updateEpic(epic1_update);
        System.out.println("Epic 1: " + epic1);
        System.out.println();

        Subtask subtask_2_1_managed = taskManager.getSubtaskById(subtask_2_1_id);
        Subtask subtask_2_1_update = new Subtask(
                subtask_2_1_managed.getName(),
                subtask_2_1_managed.getDescription(),
                subtask_2_1_managed.getId(),
                Status.IN_PROGRESS,
                subtask_2_1_managed.getEpicId());
        taskManager.updateSubtask(subtask_2_1_update);
        System.out.println("Subtask 2-1: " + taskManager.getSubtaskById(subtask_2_1_id));
        System.out.println("Epic 2: " + taskManager.getEpicById(epic2_id));
        System.out.println();
        subtask_2_1_update = new Subtask(
                subtask_2_1_managed.getName(),
                subtask_2_1_managed.getDescription(),
                subtask_2_1_managed.getId(),
                Status.DONE,
                subtask_2_1_managed.getEpicId());
        taskManager.updateSubtask(subtask_2_1_update);
        System.out.println("Subtask 2-1: " + taskManager.getSubtaskById(subtask_2_1_id));
        System.out.println("Epic 2: " + taskManager.getEpicById(epic2_id));
        System.out.println();

        printHistory(taskManager);
        System.out.println();

        System.out.println("И, наконец, попробуйте удалить одну из задач и один из эпиков.");
        System.out.println();
        taskManager.removeTaskById(task2_id);
        System.out.println("Tasks: " + taskManager.getTasks());
        System.out.println();

        taskManager.removeEpicById(epic1_id);
        System.out.println("Epics: " + taskManager.getEpics());
        System.out.println();
        System.out.println("Subtasks: " + taskManager.getSubtasks());
        System.out.println();

        System.out.println("История просмотра задач: " + taskManager.getHistory());
        System.out.println();

        System.out.println("Проверка удаления всех подзадач.");
        System.out.println();
        taskManager.removeAllSubtasks();
        System.out.println("Subtasks: " + taskManager.getSubtasks());
        System.out.println();
        System.out.println("Epics: " + taskManager.getEpics());
        System.out.println();

        printHistory(taskManager);
        System.out.println();
    }

    private static void printHistory(TaskManager taskManager) {
        System.out.println("История просмотра задач:");
        for (Task historyItem : taskManager.getHistory()) {
            System.out.println(historyItem);
        }
    }
}
