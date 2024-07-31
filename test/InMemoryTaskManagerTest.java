package test;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;
import modeltask.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends test.TaskManagerTest<InMemoryTaskManager> {
    protected TaskManager taskManager;

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

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
        Task task = new Task("", "", Duration.ofMinutes(2), LocalDateTime.now());
        taskManager.addTask(task);
        Assertions.assertNotNull(taskManager.getTaskId(task.getId()));
    }

    @Test
    public void checAddSubtaskAndGetHis() { //Проверяем что taskManager добавляет сабтаску и может ее вернуть
        Subtask subtask = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now(), new Epic("", ""));
        taskManager.addSubTask(subtask);
        Assertions.assertNotNull(taskManager.getSubtaskId(subtask.getId()));
    }

    @Test
    public void checConflictBetweenGenerateIdAndSetId() { //Проверяем что task со сгенерированным id и заданным не конфликтуют
        Task task1 = new Task("", "", Duration.ofMinutes(2), LocalDateTime.now());
        task1.setId(2);
        Task task2 = new Task("", "", Duration.ofMinutes(2), LocalDateTime.now());
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    public void checDontUpdateFieldsByAddManager() { //Проверяем что при добавление в мэнеджер таска не меняется
        Task task = new Task("1", "", Duration.ofMinutes(2), LocalDateTime.now());
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskId(task.getId()));
    }

    @Test
    public void checDeleteSubtaskHisDeleteByEpic() { //Проверяем что при удалении сабтаски её id удаляется из эпик
        Epic epic = new Epic("1", "");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("1", "", Duration.ofMinutes(2), LocalDateTime.now(), epic);
        taskManager.addSubTask(subtask);
        taskManager.removeSubtask(subtask.getId());
        Subtask subtask2 = new Subtask("2", "", Duration.ofMinutes(2), LocalDateTime.now().plusHours(3),
                epic);
        taskManager.addSubTask(subtask2);
        assertNotEquals(epic.getSubTasks().get(0), subtask);
    }

    @Test
    public void checInfluenceSetterByManage() { //Проверяем влияние сеттеров на менеджер
        Task task = new Task("1", "", Duration.ofMinutes(2), LocalDateTime.now());
        taskManager.addTask(task);
        task.setName("2");
        assertEquals(task, taskManager.getTaskId(task.getId()));
    }

    @Test
    public void checStatusEpicCalculateWhenAllSubtaskNew() { //проверяем расчет статуса эпик когда все сабтаски новые
        Epic epic = new Epic("", "");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now(), epic);
        taskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now().plusHours(1), epic);
        taskManager.addSubTask(subtask2);
        assertTrue(epic.getStatus() == TaskStatus.NEW);
    }

    @Test
    public void checStatusEpicCalculateWhenAllSubtaskDone() { //проверяем расчет статуса эпик когда все сабтаски выполнены
        Epic epic = new Epic("", "");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now(), epic);
        taskManager.addSubTask(subtask1);
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subtask1);
        Subtask subtask2 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now().plusHours(1), epic);
        taskManager.addSubTask(subtask2);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subtask2);
        assertTrue(epic.getStatus() == TaskStatus.DONE);
    }

    @Test
    public void checStatusEpicCalculateWhenSubtaskDoneAndSubtaskNew() { //проверяем расчет статуса эпик когда сабтаск новые и выполнены
        Epic epic = new Epic("", "");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now(), epic);
        taskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now().plusHours(1), epic);
        taskManager.addSubTask(subtask2);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subtask2);
        assertTrue(epic.getStatus() == TaskStatus.IN_PROGRESS);
    }

    @Test
    public void checStatusEpicCalculateWhenAllSubtaskInProgress() { //проверяем расчет статуса эпик когда все сабтаски в прогрессе
        Epic epic = new Epic("", "");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now(), epic);
        taskManager.addSubTask(subtask1);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subtask1);
        Subtask subtask2 = new Subtask("", "", Duration.ofMinutes(2), LocalDateTime.now().plusHours(1), epic);
        taskManager.addSubTask(subtask2);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subtask2);
        assertTrue(epic.getStatus() == TaskStatus.IN_PROGRESS);
    }
}