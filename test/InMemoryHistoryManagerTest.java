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
    public void checWhatOldVersionTaskDeleteToNewViewing(){ //Проверяем что при просмотре новой версии старая удаляется
        Task task = new Task("1", "");
        taskManager.addTask(task);
        taskManager.getTaskId(task.getId());
        task.setName("2");
        taskManager.updateTask(task);
        taskManager.getTaskId(task.getId());
        Assertions.assertEquals(taskManager.getHistory().get(task.getId()).getName(), task.getName());
    }

    @Test
    public void checWhatByDeleteTaskAndDeleteFromHistory(){ // проверяем что при удалении задачи она удаляется из истории
        Task task = new Task("1", "");
        taskManager.addTask(task);
        taskManager.getTaskId(task.getId());
        Task task2 = new Task("2", "");
        taskManager.addTask(task2);
        taskManager.getTaskId(task2.getId());
        taskManager.removeTask(task.getId());
        Assertions.assertNotEquals(taskManager.getHistory().get(0), task);
    }

    @Test
    public void checWhatTaskAddToHistory(){ // проверяем что при удалении задачи она удаляется из истории
        Task task = new Task("1", "");
        taskManager.addTask(task);
        taskManager.getTaskId(task.getId());
        Assertions.assertEquals(taskManager.getHistory().get(0), task);
    }
}