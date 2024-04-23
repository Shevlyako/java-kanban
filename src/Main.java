public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("1", "1", TaskStatus.NEW));
        taskManager.addTask(new Task("2", "2", TaskStatus.NEW));
        System.out.println(taskManager.getTasks());
        taskManager.addEpic(new Epic("3.1", "1", TaskStatus.NEW));
        taskManager.addEpic(new Epic("3.2", "2", TaskStatus.NEW));
        System.out.println(taskManager.getEpics());
        taskManager.addSubTask(new SubTask("2.1", "1", TaskStatus.NEW, 3));
        taskManager.addSubTask(new SubTask("2.2", "2", TaskStatus.NEW, 4));
        System.out.println(taskManager.getSubTasks());
        System.out.println(" ");
        taskManager.removeTask(5);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());
        System.out.println(taskManager.getEpics());
        System.out.println("");
        System.out.println(taskManager.getTaskId(4));
    }
}
