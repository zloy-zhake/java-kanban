import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    void testTasksEquals() {
        Task task1 = new Task("name1", "description1", 1, Status.NEW);
        Task task2 = new Task("name2", "description2", 1, Status.IN_PROGRESS);
        assertEquals(task1, task2);
    }

}