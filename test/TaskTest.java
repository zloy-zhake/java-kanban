import org.junit.jupiter.api.Test;
import taskmanager.Status;
import taskmanager.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    // проверьте, что экземпляры класса taskmanager.Task равны друг другу, если равен их id;
    @Test
    void testTasksEquals() {
        Task task1 = new Task("name1", "description1", 1, Status.NEW);
        Task task2 = new Task("name2", "description2", 1, Status.IN_PROGRESS);
        assertEquals(task1, task2);
    }

    // С помощью сеттеров экземпляры задач позволяют изменить любое своё поле, но это может повлиять на данные внутри менеджера.
    // Протестируйте эти кейсы и подумайте над возможными вариантами решения проблемы.
    @Test
    void testTasksSetters() {
        Task task = new Task("name1", "description1", 1, Status.NEW);
        task.setId(2);
        assertEquals(2, task.getId());
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
    }

    @Test
    void testTimeRelatedFields() {
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        Duration duration = Duration.ofHours(1);
        Task task = new Task("name1", "description1", 1, Status.NEW, startTime, duration);
        LocalDateTime newStartTime = LocalDateTime.of(2025, 6, 5, 12, 34);
        task.setStartTime(newStartTime);
        assertEquals(newStartTime, task.getStartTime());
        Duration newDuration = Duration.ofHours(2);
        task.setDuration(newDuration);
        assertEquals(newDuration, task.getDuration());
        LocalDateTime expectedEndTime = LocalDateTime.of(2025, 6, 5, 14, 34);
        assertEquals(expectedEndTime, task.getEndTime());
    }

    // Убедиться, что реализован корректный расчёт пересечения временных интервалов задач,
    // чтобы предотвратить конфликтные ситуации.
    @Test
    void testIsOverlapped() {
        LocalDateTime startTime1 = LocalDateTime.of(2025, 6, 6, 1, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2025, 6, 6, 1, 30);
        LocalDateTime startTime3 = LocalDateTime.of(2025, 6, 6, 2, 0);
        Duration duration = Duration.ofHours(1);
        Task task1 = new Task("name1", "description1", 1, Status.NEW, startTime1, duration);
        Task task2 = new Task("name2", "description2", 2, Status.NEW, startTime2, duration);
        Task task3 = new Task("name3", "description3", 3, Status.NEW, startTime3, duration);

        assertFalse(task1.isOverlapped(task3));
        assertFalse(task3.isOverlapped(task1));
        assertTrue(task1.isOverlapped(task2));
        assertTrue(task2.isOverlapped(task1));
        assertTrue(task2.isOverlapped(task3));
        assertTrue(task3.isOverlapped(task2));
    }
}