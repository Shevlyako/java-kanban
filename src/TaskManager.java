import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int id = 0;

    public TaskManager() {
    }

    public static int getId() {
        return id = id + 1;
    }

    public void addTask(Task task) { //Добавляем таску
        tasks.put(task.getId(), task);
    }

    public void addSubTask(SubTask subTask) { //Добавляем субтаску
        subTasks.put(subTask.getId(), subTask);
        Epic epic = (Epic) epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.addSubInEpic(subTask);
        }
    }

    public void addEpic(Epic epic) { //Добавляем эпик
        epics.put(epic.getId(), epic);
    }

    public HashMap<Integer, Task> getTasks() { //Выводим все таски
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() { //Выводим все субТаски
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() { //Выводим все эпики
        return epics;
    }
    public void clearTasks(){ //удаляем все таски
        tasks.clear();
    }
    public void clearSubTasks(){ //удаляем все субТаски
        subTasks.clear();
    }
    public void clearEpic(){ //Удаляем все эпики
        epics.clear();
    }
    public void removeTask(int id){ //удаляем таску по её id
        if(tasks.containsKey(id)){
            tasks.remove(id);
        } else if (subTasks.containsKey(id)) {
            subTasks.remove(id);
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        }
    }
    public Task getTaskId(int id){
        if(tasks.containsKey(id)){
            return tasks.get(id);
        } else if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        }
        return null;
    }

    public void updateTask(Task task){

    }
}
