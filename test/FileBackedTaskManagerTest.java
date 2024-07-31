package test;

import manager.FileBackedTaskManager;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManagerTest extends test.TaskManagerTest<FileBackedTaskManager> {
    // Сохранение пустого файла 
    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("test.csv"));
    }

    @Test
    void savingAnEmptyFileTest() {
        String title = "id,type,name,status,description,epic, startTime, duration, endTime";
        String lineSeparator = System.lineSeparator();
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

            String[] lines = Files.readString(file.toPath()).split("\n");
            Assertions.assertEquals(lines.length, 1, "Ошибка загрузки пустого файла");
            Assertions.assertEquals(lines[0], title, "Первая строка не титульная");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Загрузка из пустого файла
    @Test
    void loadingAnEmptyFileTest() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

            Assertions.assertEquals(fileManager.getTasks().size(), 0);
            Assertions.assertEquals(fileManager.getEpics().size(), 0);
            Assertions.assertEquals(fileManager.getSubTasks().size(), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //сохранение нескольких задач
    @Test
    void savingTasksTest() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

            Task task1 = new Task("Задача 1", "Описание 1", Duration.ofMinutes(2),
                    LocalDateTime.now());
            fileManager.addTask(task1);
            Task task2 = new Task("Задача 2", "Описание 2", Duration.ofMinutes(2),
                    LocalDateTime.now().plusHours(1));
            fileManager.addTask(task2);
            Epic epic1 = new Epic("Эпик1", "Описание 1");
            fileManager.addEpic(epic1);
            Subtask subtask1 = new Subtask("Подзадача 1", "...", Duration.ofMinutes(2),
                    LocalDateTime.now().plusHours(2), epic1);
            Subtask subtask2 = new Subtask("Подзадача 2", "...", Duration.ofMinutes(2),
                    LocalDateTime.now().plusHours(3), epic1);
            fileManager.addSubTask(subtask1);
            fileManager.addSubTask(subtask2);

            Assertions.assertEquals(fileManager.getTasks().size(), 2, "Количество задач не совпадает");
            Assertions.assertEquals(fileManager.getEpics().size(), 1, "Количество эпиков не совпадает");
            Assertions.assertEquals(fileManager.getSubTasks().size(), 2, "Количество подзадач не совпадает");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //загрузкa нескольких задач
    @Test
    void loadingTasksTest() {
        try {
            File file = File.createTempFile("test", "csv");

            try (FileWriter writer = new FileWriter(file)) {

                writer.write("""
                        id,type,name,status,description,epic, startTime, duration, endTime
                        1,TASK,Задача 1,NEW,Описание задачи,2024-07-08T20:41:36.631908300,PT2M,2024-07-08T20:43:36.631908300
                        2,EPIC,Эпик 1,NEW,Описание эпика,2024-07-08T21:41:36.631908300,PT2M,2024-07-08T20:43:36.631908300
                        3,SUBTASK,Подзадача 1,NEW,Описание подзадачи,1,2024-07-08T22:41:36.631908300,PT2M,2024-07-08T20:43:36.631908300""");
            }
            FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);
            Assertions.assertEquals(fileManager.getTasks().size(), 1, "Количество задач не совпадает");
            Assertions.assertEquals(fileManager.getEpics().size(), 1, "Количество эпиков не совпадает");
            Assertions.assertEquals(fileManager.getSubTasks().size(), 1, "Количество подзадач не совпадает");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}