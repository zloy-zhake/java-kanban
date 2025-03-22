public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task task_1 = taskManager.createTask("Задача 1", "Описание задачи 1.");
        Task task_2 = taskManager.createTask("Задача 2", "Описание задачи 2.");

        Epic epic_1 = taskManager.createEpic("Эпик 1", "Описание эпика 1");
        Subtask subtask_1_1 = taskManager.createSubtask("Подзадача 1-1", "Описание подзадачи 1-1.", epic_1);
        Subtask subtask_1_2 = taskManager.createSubtask("Подзадача 1-2", "Описание подзадачи 1-2.", epic_1);

        Epic epic_2 = taskManager.createEpic("Эпик 2", "Описание эпика 2");
        Subtask subtask_2_1 = taskManager.createSubtask("Подзадача 2-1", "Описание подзадачи 2-1.", epic_2);

        System.out.println("Epics: " + taskManager.getEpics());
        System.out.println();
        System.out.println("Tasks: " + taskManager.getTasks());
        System.out.println();
        System.out.println("Subtasks: " + taskManager.getSubtasks());
        System.out.println();
    }
}
