import Manager.TaskManager;
import ModelTask.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Создание задач:");
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("Создать первую задачу", "Создать первую задачу"));
        taskManager.addTask(new Task("Сдать ТЗ", "Выполнить задание ТЗ без ошибок и сдать на проверку"));
        taskManager.addEpic(new Epic("Купить продукты", "Купить молоко и сыр"));
        taskManager.addEpic(new Epic("Купить еду для собаки", "Купить роял"));
        taskManager.addSubTask(new Subtask("Купить молоко", "Купить молоко", 3));
        taskManager.addSubTask(new Subtask("Купить сыр", "Купить сыр", 3));
        taskManager.addSubTask(new Subtask("Купить роял", "Купить роял", 4));
        System.out.println("Список ModelTask.Task:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список ModelTask.Epic:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список ModelTask.Subtask:");
        System.out.println(taskManager.getSubTasks());
        System.out.println();

        System.out.println("Обновление задач:");
        taskManager.getTaskId(1).setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(taskManager.getTaskId(1));
        taskManager.getTaskId(2).setDescription("123");
        taskManager.updateTask(taskManager.getTaskId(2));
        taskManager.getSubtaskId(5).setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(taskManager.getSubtaskId(5));
        taskManager.getSubtaskId(7).setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(taskManager.getSubtaskId(7));
        System.out.println("Список ModelTask.Task:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список ModelTask.Epic:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список ModelTask.Subtask:");
        System.out.println(taskManager.getSubTasks());
        System.out.println();

        System.out.println("Удаление задач:");
        taskManager.removeTask(1);
        taskManager.removeSubtask(7);
        System.out.println("Список ModelTask.Task:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список ModelTask.Epic:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список ModelTask.Subtask:");
        System.out.println(taskManager.getSubTasks());
        System.out.println();
    }
}
