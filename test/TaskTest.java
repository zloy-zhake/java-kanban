import org.junit.jupiter.api.Test;

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

}