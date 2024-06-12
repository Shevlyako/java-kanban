package test;

import modeltask.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {
    @Test
    public void checTwoSubtaskEqalIfEqualId() { // Проверяем что два эпика с одинаковым id равны
        Epic epic1 = new Epic("", "");
        Epic epic2 = new Epic("", "");
        epic1.setId(1);
        epic2.setId(1);
        Assertions.assertEquals(epic1, epic2);
    }
}