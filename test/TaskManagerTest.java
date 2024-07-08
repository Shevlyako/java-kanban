package test;

import manager.TaskManager;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;
import modeltask.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void manager() {
        taskManager = createTaskManager();
    }

    @Test
    public void addTaskTest() {
        Task task = new Task("111", "222", Duration.ofMinutes(2), LocalDateTime.now());
        taskManager.addTask(task);
        int id = task.getId();
        Task taskSave = taskManager.getTaskId(id);

        assertNotNull(taskSave, "Задача не найдена.");
        assertEquals(task, taskSave, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addSubtaskTest() {
        Epic epic = new Epic("Четверг", "маленькая пятница");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("утро", "проснуться", Duration.ofMinutes(2), LocalDateTime.now(), epic.getId());
        taskManager.addSubTask(subtask);
        Subtask subtask2 = new Subtask("утро", "проснуться", Duration.ofMinutes(6), LocalDateTime.now().plusMinutes(30), epic.getId());
        taskManager.addSubTask(subtask2);
        int id = subtask.getId();
        Subtask subtaskSave = taskManager.getSubtaskId(id);

        assertNotNull(subtaskSave, "Задача не найдена.");
        assertEquals(subtask, subtaskSave);

        final List<Subtask> subtasks = taskManager.getSubtasksEpic(epic.getId());
        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");

        assertEquals(subtask2.getStartTime().plus(subtask2.getDuration()), epic.getEndTime(),
                "Время окончания выполнения эпика не обновилось при добавлении подзадач");
    }

    @Test
    public void addEpicTest() {
        Epic epic = new Epic("111", "111");
        taskManager.addEpic(epic);
        int id = epic.getId();
        Epic epicSave = taskManager.getEpicId(id);

        assertNotNull(epicSave, "Задача не найдена.");
        assertEquals(epic, epicSave);

        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void getTasksTest() {
        Task task1 = new Task("111", "111", Duration.ofMinutes(2), LocalDateTime.of(2024, 7,
                6, 11, 50));
        taskManager.addTask(task1);
        Task task2 = new Task("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024, 7,
                6, 15, 50));
        taskManager.addTask(task2);

        List<Task> comparable = new ArrayList<>();
        comparable.add(task1);
        comparable.add(task2);

        List<Task> actual = taskManager.getTasks();

        assertEquals(comparable, actual, "Ошибка в возврате списка всех задач");
    }

    @Test
    public void getSubtasksTest() {
        Epic epic = new Epic("111", "111");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 6, 11, 50), epic.getId());
        taskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("333", "333", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 6, 13, 50), epic.getId());
        taskManager.addSubTask(subtask2);

        List<Subtask> comparable = new ArrayList<>();
        comparable.add(subtask1);
        comparable.add(subtask2);

        List<Subtask> actual = taskManager.getSubTasks();

        assertEquals(comparable, actual, "Ошибка в возврате списка всех подзадач");
    }

    @Test
    public void getEpicsTest() {
        Epic epic1 = new Epic("111", "111");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("222", "222");
        taskManager.addEpic(epic2);

        List<Epic> comparable = new ArrayList<>();
        comparable.add(epic1);
        comparable.add(epic2);

        List<Epic> actual = taskManager.getEpics();

        assertEquals(comparable, actual, "Ошибка в возврате списка всех эпиков");
    }

    @Test
    public void clearTasksTest() {
        Task task1 = new Task("111", "111", Duration.ofMinutes(2), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = new Task("222", "222", Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(49));
        taskManager.addTask(task2);

        taskManager.clearTasks();
        int actual2 = taskManager.getTasks().size();
        assertEquals(0, actual2, "Не все задачи удалены");
    }

    @Test
    void clearSubTasksTest() {
        Epic epic = new Epic("111", "111");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 6, 11, 50), epic.getId());
        taskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 6, 13, 50), epic.getId());
        taskManager.addSubTask(subtask2);

        assertEquals(subtask2.getStartTime().plus(subtask2.getDuration()), epic.getEndTime(),
                "Время окончания выполнения эпика не обновилось при добавлении подзадач");

        taskManager.clearSubTasks();
        int actual2 = taskManager.getSubTasks().size();
        assertEquals(0, actual2, "Не все задачи удалены");
        assertNull(epic.getStartTime(), "При удалении всех подзадач, сохраняется стартовое время эпика");
        assertEquals(Duration.ZERO, epic.getDuration(), "При удалении всех подзадач, сохраняется продолжительность эпика");
    }

    @Test
    void clearEpicTest() {
        Epic epic1 = new Epic("111", "111");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("222", "222");
        taskManager.addEpic(epic2);

        taskManager.clearEpic();
        int actual2 = taskManager.getEpics().size();
        assertEquals(0, actual2, "Не все задачи удалены");
    }

    @Test
    void removeTaskTest() {
        Task task1 = new Task("Ночь", "...", Duration.ofMinutes(2), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = new Task("День", "...", Duration.ofMinutes(2), LocalDateTime.now().plusMinutes(49));
        taskManager.addTask(task2);

        taskManager.removeTask(task2.getId());
        assertNull(taskManager.getTaskId(task2.getId()), "Задача по ID не удалена");
    }

    @Test
    void removeSubtaskTest() {
        Epic epic = new Epic("111", "111");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("...", "...", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 6, 11, 50), epic.getId());
        taskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("...", "...", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 6, 14, 50), epic.getId());
        taskManager.addSubTask(subtask2);

        Integer idDelete = subtask2.getId();
        ArrayList<Integer> test = epic.getSubTasks();
        boolean idInSubtaskList = test.contains(idDelete);
        assertTrue(idInSubtaskList, "Id подзадачи, которая будет удалена изначально не добавилась в список");

        taskManager.removeSubtask(idDelete);
        int expected = 1;
        int actual = taskManager.getSubTasks().size();
        assertEquals(expected, actual, "Задача по ID не удалена");
        assertEquals(subtask1.getStartTime().plus(subtask1.getDuration()), epic.getEndTime(),
                "Время окончания выполнения эпика не обновилось при удалении подзадачи");
        assertEquals(subtask1.getDuration(), epic.getDuration(),
                "Продолжительность выполнения эпика не обновилось при удалении подзадачи");

        ArrayList<Integer> test2 = epic.getSubTasks();
        boolean idInSubtaskList2 = test2.contains(idDelete);
        assertFalse(idInSubtaskList2, "Id удвленной подзадачи осталось в списке эпика");
    }

    @Test
    void removeEpicTest() {
        Epic epic1 = new Epic("111", "111");
        taskManager.addEpic(epic1);
        int id = epic1.getId();
        Epic epic2 = new Epic("222", "222");
        taskManager.addEpic(epic2);

        taskManager.removeEpic(id);
        assertNull(taskManager.getTaskId(epic2.getId()), "Задача по ID не удалена");
    }

    @Test
    void getTaskIdTest() {
        Task task1 = new Task("111", "111", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 7, 11, 50));
        taskManager.addTask(task1);
        Task task2 = new Task("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 7, 15, 50));
        taskManager.addTask(task2);

        assertEquals(task2, taskManager.getTaskId(task2.getId()), "Ошибка при получении задачи по Id");
    }

    @Test
    void getSubtaskIdTest() {
        Epic epic = new Epic("111", "111");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 7, 11, 50), epic.getId());
        taskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("333", "333", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 7, 14, 50), epic.getId());
        taskManager.addSubTask(subtask2);

        assertEquals(subtask2, taskManager.getSubtaskId(subtask2.getId()), "Ошибка при получении подзадачи по Id");
    }

    @Test
    void getEpicIdTest() {
        Epic epic1 = new Epic("111", "111");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("222", "222");
        taskManager.addEpic(epic2);

        assertEquals(epic2, taskManager.getEpicId(epic2.getId()), "Ошибка при получении эпика по Id");
    }

    @Test
    void updateTaskTest() {
        Task task = new Task("111", "111", Duration.ofMinutes(2), LocalDateTime.of(2024, 7,
                7, 11, 50));
        taskManager.addTask(task);
        task = new Task("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024, 7,
                7, 13, 50));
        taskManager.updateTask(task);

        assertNotNull(task, "Задача пустая");
        assertNotEquals("111", task.getName(), "Задача не обновилась");
    }

    @Test
    void updateEpicTest() {
        Epic epic = new Epic("111", "111");
        taskManager.addEpic(epic);
        epic = new Epic("222", "222");
        taskManager.updateEpic(epic);

        assertNotNull(epic, "Задача пустая");
        assertNotEquals("111", epic.getDescription());
    }

    @Test
    void updateSubTaskTest() {
        Epic epic = new Epic("111", "111");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 7, 11, 50), epic.getId());
        taskManager.addSubTask(subtask);
        LocalDateTime startTimeExpected = epic.getStartTime();
        LocalDateTime endTimeExpected = epic.getEndTime();
        Duration durationExpected = epic.getDuration();

        subtask.setStatus(TaskStatus.DONE);
        subtask.setStartTime(LocalDateTime.of(2024, 7, 7, 17, 00));
        subtask.setDuration(Duration.ofMinutes(34));
        taskManager.updateSubTask(subtask);

        assertNotNull(subtask, "Задача пустая");
        assertNotEquals(TaskStatus.NEW, subtask.getStatus());
        assertNotEquals(startTimeExpected, subtask.getStartTime(), "Стартовое время не обновилось");
        assertNotEquals(endTimeExpected, subtask.getEndTime(), "Время окончания не обновилось");
        assertNotEquals(durationExpected, subtask.getDuration(), "Продолжительность выполнения не обновилась");
    }

    @Test
    void getPrioritizedTasksTest() {
        Task task3 = new Task("111", "111", Duration.ofMinutes(25), LocalDateTime.of(2024,
                7, 7, 14, 50));
        taskManager.addTask(task3);
        Task task1 = new Task("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024,
                7, 7, 9, 50));
        taskManager.addTask(task1);
        Epic epic = new Epic("333", "333");
        taskManager.addEpic(epic);
        Subtask subtask2 = new Subtask("444", "444", Duration.ofMinutes(22), LocalDateTime.of(2024,
                7, 7, 11, 50), epic.getId());
        taskManager.addSubTask(subtask2);

        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(subtask2);
        expected.add(task3);

        List<Task> sortTasks = taskManager.getPrioritizedTasks();
        assertEquals(expected, sortTasks, "Ошибка сортировки при добавлении задач");

        taskManager.removeTask(task1.getId());
        sortTasks = taskManager.getPrioritizedTasks();
        expected.removeFirst();
        assertEquals(expected, sortTasks, "Ошибка сортировки при удалении задач по Id");

        taskManager.clearEpic();
        sortTasks = taskManager.getPrioritizedTasks();
        expected.removeFirst();
        assertEquals(expected, sortTasks, "Ошибка сохранения подзадач при удалении всех эпиков");

        Epic epic2 = new Epic("555", "555");
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("666", "666", Duration.ofMinutes(22), LocalDateTime.of(2024,
                7, 7, 6, 0), epic2.getId());
        taskManager.addSubTask(subtask1);

        taskManager.removeEpic(epic2.getId());
        sortTasks = taskManager.getPrioritizedTasks();
        assertEquals(expected, sortTasks, "Ошибка сохранения подзадач при удалени эпиков по id");
    }

    @Test
    void getHistoryTest() {
        Task task1 = new Task("111", "111", Duration.ofMinutes(25), LocalDateTime.of(2024,
                12, 31, 14, 50));
        taskManager.addTask(task1);
        Task task2 = new Task("222", "222", Duration.ofMinutes(2), LocalDateTime.of(2024,
                4, 13, 11, 50));
        taskManager.addTask(task2);
        Epic epic = new Epic("333", "333");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("444", "444", Duration.ofMinutes(22), LocalDateTime.of(2024,
                12, 31, 9, 12), epic.getId());
        taskManager.addSubTask(subtask);

        taskManager.getTaskId(task1.getId());
        taskManager.getTaskId(task2.getId());
        taskManager.getEpicId(epic.getId());
        taskManager.getSubtaskId(subtask.getId());

        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        expected.add(epic);
        expected.add(subtask);

        assertEquals(expected, taskManager.getHistory());
    }
}