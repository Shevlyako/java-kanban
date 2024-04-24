public class Main {

    public static void main(String[] args) {
        System.out.println("Создание задач:");
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("Создать первую задачу", "Создать первую задачу", TaskStatus.NEW));
        taskManager.addTask(new Task("Сдать ТЗ", "Выполнить задание ТЗ без ошибок и сдать на проверку"
                , TaskStatus.NEW));
        taskManager.addEpic(new Epic("Купить продукты", "Купить молоко и сыр", TaskStatus.NEW));
        taskManager.addEpic(new Epic("Купить еду для собаки", "Купить роял", TaskStatus.NEW));
        taskManager.addSubTask(new SubTask("Купить молоко", "Купить молоко", TaskStatus.NEW, 3));
        taskManager.addSubTask(new SubTask("Купить сыр", "Купить сыр", TaskStatus.NEW, 3));
        taskManager.addSubTask(new SubTask("Купить роял", "Купить роял", TaskStatus.NEW, 4));
        System.out.println("Список Task:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список Epic:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список SubTask:");
        System.out.println(taskManager.getSubTasks());
        System.out.println();

        System.out.println("Обновление задач:");
        taskManager.updateTask(new Task("Первая задача", "Создать задачу номер 1",
                TaskStatus.IN_PROGRESS, 1));
        taskManager.updateTask(new Task("Сдать ТЗ",
                "Выполнить задание ТЗ без ошибок и сдать на проверку", TaskStatus.IN_PROGRESS, 2));
        taskManager.updateSubTask(new SubTask("Купить молоко", "Купить молоко", TaskStatus.DONE,
                5, 3));
        taskManager.updateSubTask(new SubTask("Купить роял", "Купить роял", TaskStatus.DONE,
                7, 4));
        System.out.println("Список Task:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список Epic:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список SubTask:");
        System.out.println(taskManager.getSubTasks());
        System.out.println();

        System.out.println("Удаление задач:");
        taskManager.removeTask(1);
        taskManager.removeTask(4);
        System.out.println("Список Task:");
        System.out.println(taskManager.getTasks());
        System.out.println("Список Epic:");
        System.out.println(taskManager.getEpics());
        System.out.println("Список SubTask:");
        System.out.println(taskManager.getSubTasks());
        System.out.println();
    }
}
