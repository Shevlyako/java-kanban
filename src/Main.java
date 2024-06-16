import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        File fileTest = new File("fileTaskManager.csv");

        FileBackedTaskManager fileManager1 = new FileBackedTaskManager(fileTest);

        Task task1 = new Task("Задача 1", "Описание 1");
        fileManager1.addTask(task1);
        Task task2 = new Task("Задача 2", "Описание 2");
        fileManager1.addTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание 1");
        fileManager1.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "...", epic1);
        Subtask subtask2 = new Subtask("Подзадача 2", "...", epic1);
        fileManager1.addSubTask(subtask1);
        fileManager1.addSubTask(subtask2);

        FileBackedTaskManager fileManager2 = FileBackedTaskManager.loadFromFile(fileTest);

        if (fileManager1.getTasks().size() != fileManager2.getTasks().size()) {
            System.out.println("Количество задач не совпадает");
        }
        if (fileManager1.getEpics().size() != fileManager2.getEpics().size()) {
            System.out.println("Количество эпиков не совпадает");
        }
        if (fileManager1.getSubTasks().size() != fileManager2.getSubTasks().size()) {
            System.out.println("Количество эпиков не совпадает");
        }
    }
}
