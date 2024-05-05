package Test;

import Manager.Managers;
import Manager.TaskManager;
import ModelTask.Epic;
import ModelTask.Subtask;
import ModelTask.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    public void beforEach() { //Перед каждым тестом создаем новый пустой taskManager
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
        Subtask subtask = new Subtask("","", new Epic("",""));
        taskManager.addSubTask(subtask);
        Assertions.assertNotNull(taskManager.getSubtaskId(subtask.getId()));
    }

    @Test
    public void checConflictBetweenGenerateIdAndSetId(){ //Проверяем что task со сгенерированным id и заданным не конфликтуют
        Task task1 = new Task("", "");
        task1.setId(2);
        Task task2 = new Task("", "");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    public void checDontUpdateFieldsByAddManager(){ //Проверяем что при добавление в мэнеджер таска не меняется
        Task task = new Task("1", "");
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskId(1));
    }
}