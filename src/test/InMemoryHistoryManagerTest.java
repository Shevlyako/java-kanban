package test;

import manager.Managers;
import manager.TaskManager;
import modeltask.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforEach(){
        taskManager = Managers.getDefault();
    }

    @Test
    public void checSaveInHistoryManagerFirstVersionTask(){ //Проверяем что в истории сохраняется старая версия Таски
        Task task = new Task("1", "");
        taskManager.addTask(task);
        taskManager.getTaskId(1);
        task.setName("2");
        taskManager.updateTask(task);
        Assertions.assertNotEquals(taskManager.getHistory().get(0).getName(), task.getName());
    }
}