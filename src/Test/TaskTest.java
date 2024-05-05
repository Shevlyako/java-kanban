package Test;

import ModelTask.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void checTwoTaskEqalIfEqualId(){ // Проверяем что две таски с одинаковым id равны
        Task task1 = new Task("","");
        Task task2 = new Task("","");
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1,task2);
    }
}