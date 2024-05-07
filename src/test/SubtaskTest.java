package test;

import modeltask.Epic;
import modeltask.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {
    @Test
    public void checTwoSubtaskEqalIfEqualId() { // Проверяем что две сабтаски с одинаковым id равны
        Subtask subtask1 = new Subtask("", "", new Epic("",""));
        Subtask subtask2 = new Subtask("", "", new Epic("",""));
        subtask1.setId(1);
        subtask2.setId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }

}