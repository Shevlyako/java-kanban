package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modeltask.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager;
    private static int id = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    //Метод для установки нового ID
    private int getNewId() { //Счетчик тасок
        return id++;
    }

    //Добавление новых задач
    @Override
    public Task addTask(Task newTask) {
        newTask.setId(getNewId());
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    @Override
    public Subtask addSubTask(Subtask newSubtask) {
        newSubtask.setId(getNewId());
        subTasks.put(newSubtask.getId(), newSubtask);
        Epic epic = (Epic) epics.get(newSubtask.getEpicId());
        if (epic != null) {
            epic.addSubInEpic(newSubtask);
            updateEpic(epic);
        }
        return newSubtask;
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        newEpic.setId(getNewId());
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    //Возвращение списка всех задач
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    //Удаляем все задачи
    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (Subtask subtask : subTasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubInEpic();
        }
    }

    @Override
    public void clearEpic() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
        }
        epics.clear();
        clearSubTasks();
    }

    //Удаление задачи по id
    @Override
    public void removeTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtask(int id) {
        if (subTasks.containsKey(id)) {
            historyManager.remove(id);
            epics.get(subTasks.get(id).getEpicId()).removeSubInEpic(id); // Удаляем id subtask из листа эпика
            subTasks.remove(id);
        }
    }

    @Override
    public void removeEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Integer> idSubtask = epics.get(id).getSubTasks();
            for (Integer subtask : idSubtask) {
                removeSubtask(subtask);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    //Получаем задачу по её id
    @Override
    public Task getTaskId(int id) {
        if (tasks.containsKey(id)) {
            historyManager.addHistory(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskId(int id) {
        if (subTasks.containsKey(id)) {
            historyManager.addHistory(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicId(int id) {
        if (epics.containsKey(id)) {
            historyManager.addHistory(epics.get(id));
            return epics.get(id);
        }
        return null;
    }

    //Обновление задачи
    @Override
    public void updateTask(Task task) { //Обновляем таску
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            Epic epic = (Epic) epics.get(subtask.getEpicId());
            if (epic != null) { //Проверка эпика к которому пренадлежит таска на null
                updateEpicStatus(epic); //пересчитываем статус эпика к которому принадлежит таска
            }
        }
    }

    //Обновление статуса эпика при обновлении статуса сабтаск
    @Override
    public void updateEpicStatus(Epic epic) {
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

    //получаем историю
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
