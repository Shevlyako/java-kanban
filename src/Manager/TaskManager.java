package Manager;

import java.util.ArrayList;
import java.util.HashMap;

import ModelTask.*;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int id = 0;

    private static int getId() { //Счетчик тасок
        return id = id + 1;
    }

    public void addTask(Task newTask) { //Добавляем таску
        newTask.setId(getId());
        tasks.put(newTask.getId(), newTask);
    }

    public void addSubTask(Subtask newSubtask) { //Добавляем субтаску
        newSubtask.setId(getId());
        subTasks.put(newSubtask.getId(), newSubtask);
        Epic epic = (Epic) epics.get(newSubtask.getEpicId());
        if (epic != null) {
            epic.addSubInEpic(newSubtask);
        }
    }

    public void addEpic(Epic newEpic) { //Добавляем эпик
        newEpic.setId(getId());
        epics.put(newEpic.getId(), newEpic);
    }

    public ArrayList<Task> getTasks() { //вовращаем все таски
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubTasks() { //возвращаем все субТаски
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpics() { //возвращаем все эпики
        return new ArrayList<>(epics.values());
    }

    public void clearTasks() { //удаляем все таски
        tasks.clear();
    }

    public void clearSubTasks() { //удаляем все субТаски
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubInEpic();
        }
    }

    public void clearEpic() { //Удаляем все эпики
        epics.clear();
        clearSubTasks();
    }

    public void removeTask(int id) { //удаляем таску по её id
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void removeSubtask(int id) {
        if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpicId()).removeSubInEpic(id); // Удаляем id subtask из листа эпика
            subTasks.remove(id);
        }
    }

    public void removeEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Integer> idSubtask = epics.get(id).getSubTasks();
            for (Subtask subtask : subTasks.values()) {
                if (idSubtask.contains(subtask.getId())) {
                    removeSubtask(subtask.getId());
                }
            }
            epics.remove(id);
        }
    }

    public Task getTaskId(int id) { //получаем таску по её id
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public Subtask getSubtaskId(int id) {
        if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        }
        return null;
    }

    public Epic getEpicId(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        }
        return null;
    }

    public void updateTask(Task task) { //Обновляем таску
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpicStatus(Epic epic) { //Метод для обновления статуса эпика при изменении статуса субтаски
        int nev = 0;
        int done = 0;
        for (Integer id : epic.getSubTasks()) { //Проходимся по таскам эпика и считаем их статусы
            Subtask subtask = subTasks.get(id);
            if (subtask.getStatus() == TaskStatus.NEW) {
                nev++;
            }
            if (subtask.getStatus() == TaskStatus.DONE) {
                done++;
            }
        }
        if (nev == epic.getSubTasks().size()) { //проверяем равны ли все таски новым
            epic.setStatus(TaskStatus.NEW);
        } else if (done == epic.getSubTasks().size()) { //проверяем равныли все таски выполненым
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void updateEpic(Epic epic) { //Обновление эпика
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void updateSubTask(Subtask subtask) { //Обновление субтаски
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            Epic epic = (Epic) epics.get(subtask.getEpicId());
            if (epic != null) { //Проверка эпика к которому пренадлежит таска на null
                updateEpicStatus(epic); //пересчитываем статус эпика к которому принадлежит таска
            }
        }
    }
}
