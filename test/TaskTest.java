package test;

import modeltask.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {
    @Test
    public void checTwoTaskEqalIfEqualId() { // Проверяем что две таски с одинаковым id равны
        Task task1 = new Task("", "", Duration.ofMinutes(2), LocalDateTime.now());
        Task task2 = new Task("", "", Duration.ofMinutes(2), LocalDateTime.now().plusHours(1));
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2);
    }
}