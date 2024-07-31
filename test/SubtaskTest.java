package test;

import modeltask.Epic;
import modeltask.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {
    @Test
    public void checTwoSubtaskEqalIfEqualId() { // Проверяем что две сабтаски с одинаковым id равны
        Subtask subtask1 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now(),
                new Epic("", ""));
        Subtask subtask2 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now().plusHours(1),
                new Epic("", ""));
        subtask1.setId(1);
        subtask2.setId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }

}