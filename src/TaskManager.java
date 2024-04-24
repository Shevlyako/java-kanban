import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int id = 0;

    public static int getId() { //Счетчик тасок
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
    public Task getTaskId(int id){ //получаем таску по её id
        if(tasks.containsKey(id)){
            return tasks.get(id);
        } else if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        }
        return null;
    }

    public void updateTask(Task task){ //Обновляем таску
        if(tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
        }
    }

    public  void updateEpicStatus(int id){ //Метод для обновления статуса эпика при изменении статуса субтаски
        int nev = 0;
        int done = 0;
        for (SubTask subTask:(epics.get(id).getSubTasks().values())){ //Проходимся по таскам эпика и считаем их статусы
            if(subTask.getStatus() == TaskStatus.NEW) {
                nev++;
            }
            if (subTask.getStatus() == TaskStatus.DONE) {
                done++;
            }
        }
        if (nev == epics.get(id).getSubTasks().size()){ //проверяем равны ли все таски новым
            epics.get(id).setStatus(TaskStatus.NEW);
        }else if (done == epics.get(id).getSubTasks().size()){ //проверяем равныли все таски выполненым
            epics.get(id).setStatus(TaskStatus.DONE);
        } else {
            epics.get(id).setStatus(TaskStatus.IN_PROGRESS);
        }
    }
    public void updateEpic(Epic epic){ //Обновление эпика
        if(epics.containsKey(epic.getId())){
            epics.put(epic.getId(), epic);
        }
    }
    public void updateSubTask(SubTask subTask){ //Обновление субтаски
        if(subTasks.containsKey(subTask.getId())){
            subTasks.put(subTask.getId(), subTask);
            Epic epic = (Epic) epics.get(subTask.getEpicId());
            if (epic != null) { //Проверка эпика к которому пренадлежит таска на null
                epic.addSubInEpic(subTask); //обновляем таску в мапе Эпика
                updateEpicStatus(subTask.getEpicId()); //пересчитываем статус эпика к которому принадлежит таска
            }
        }
    }
}
