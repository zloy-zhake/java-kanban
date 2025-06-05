import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id;
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
}