package test;

import manager.Managers;
import manager.TaskManager;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforeEachTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void checAddEpicAndGetHis() { //Проверяем что taskManager добавляет эпик и может его вернуть
        Epic epic = new Epic("", "");
        taskManager.addEpic(epic);
        Assertions.assertNotNull(taskManager.getEpicId(epic.getId()));
    }

    @Test
    public void checAddTaskAndGetHis() { //Проверяем что taskManager добавляет таску и может ее вернуть
        Task task = new Task("", "");
        taskManager.addTask(task);
        Assertions.assertNotNull(taskManager.getTaskId(task.getId()));
    }

    @Test
    public void checAddSubtaskAndGetHis() { //Проверяем что taskManager добавляет сабтаску и может ее вернуть
        Subtask subtask = new Subtask("", "", new Epic("", ""));
        taskManager.addSubTask(subtask);
        Assertions.assertNotNull(taskManager.getSubtaskId(subtask.getId()));
    }

    @Test
    public void checConflictBetweenGenerateIdAndSetId() { //Проверяем что task со сгенерированным id и заданным не конфликтуют
        Task task1 = new Task("", "");
        task1.setId(2);
        Task task2 = new Task("", "");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    public void checDontUpdateFieldsByAddManager() { //Проверяем что при добавление в мэнеджер таска не меняется
        Task task = new Task("1", "");
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskId(1));
    }

    @Test
    public void checDeleteSubtaskHisDeleteByEpic() { //Проверяем что при удалении сабтаски её id удаляется из эпик
        Epic epic = new Epic("1", "");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("1", "", epic);
        taskManager.addSubTask(subtask);
        taskManager.removeSubtask(subtask.getId());
        Subtask subtask2 = new Subtask("2", "", epic);
        taskManager.addSubTask(subtask2);
        assertNotEquals(epic.getSubTasks().get(0), subtask);
    }

    @Test
    public void checInfluenceSetterByManage() { //Проверяем влияние сеттеров на менеджер
        Task task = new Task("1", "");
        taskManager.addTask(task);
        task.setName("2");
        assertEquals(task, taskManager.getTaskId(task.getId()));
    }
}