import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    static String tmpFilePath;
    @Override
    FileBackedTaskManager createTaskManager() {
        Managers taskManagerUtil = new Managers();
        try {
            tmpFilePath = File.createTempFile("tasks", ".csv").getPath();
        }
        catch (IOException e) {
            System.out.println("Ошибка при создании временного файла для тестов.");
        }
        return  (FileBackedTaskManager) taskManagerUtil.getFileBackedTaskManager(tmpFilePath);
    }

    // сохранение и загрузка пустого файла;
    @Test
    void testSaveLoadEmptyFile() {
        Managers taskManagerUtil = new Managers();
        try {
            tmpFilePath = File.createTempFile("empty_tasks", ".csv").getPath();
        }
        catch (IOException e) {
            System.out.println("Ошибка при создании временного файла для тестов.");
        }
        FileBackedTaskManager emptyTaskManager = (FileBackedTaskManager) taskManagerUtil.getFileBackedTaskManager(tmpFilePath);
        File fileObj = new File(tmpFilePath);

        assertTrue(fileObj.exists());
        assertEquals(0, fileObj.length());

        emptyTaskManager = FileBackedTaskManager.loadFromFile(fileObj);

        assertNotNull(emptyTaskManager);
        assertNotNull(emptyTaskManager.getTasks());
        assertNotNull(emptyTaskManager.getEpics());
        assertNotNull(emptyTaskManager.getSubtasks());
    }

    // сохранение нескольких задач;
    @Test
    void testSaveTasks() {
        Managers taskManagerUtil = new Managers();
        try {
            tmpFilePath = File.createTempFile("empty_tasks", ".csv").getPath();
        }
        catch (IOException e) {
            System.out.println("Ошибка при создании временного файла для тестов.");
        }
        taskManager = (FileBackedTaskManager) taskManagerUtil.getFileBackedTaskManager(tmpFilePath);
        task1 = new Task("task name 1", "task description 1");
        taskId1 = taskManager.addTask(task1);
        Task task2 = new Task("task name 2", "task description 2");
        int taskId2 = taskManager.addTask(task2);
        long linesCount = 0;
        try {
            linesCount = Files.lines(Paths.get(tmpFilePath), Charset.defaultCharset()).count();
        }
        catch (IOException e) {
            System.out.println("Ошибка при чтении временного файла для тестов.");
        }

        assertEquals(3, linesCount);
    }

    // загрузка нескольких задач.
    @Test
    void testLoadTasks() {
        Managers taskManagerUtil = new Managers();
        try {
            tmpFilePath = File.createTempFile("empty_tasks", ".csv").getPath();
        }
        catch (IOException e) {
            System.out.println("Ошибка при создании временного файла для тестов.");
        }
        taskManager = (FileBackedTaskManager) taskManagerUtil.getFileBackedTaskManager(tmpFilePath);
        task1 = new Task("task name 1", "task description 1");
        taskId1 = taskManager.addTask(task1);
        Task task2 = new Task("task name 2", "task description 2");
        int taskId2 = taskManager.addTask(task2);

        File fileObj = new File(tmpFilePath);
        taskManager = FileBackedTaskManager.loadFromFile(fileObj);

        assertEquals(2, taskManager.getTasks().size());
    }

}