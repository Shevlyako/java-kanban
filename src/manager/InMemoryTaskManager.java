package manager;

import exception.TimeConflictException;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;
import modeltask.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    HistoryManager historyManager = Managers.getDefaultHistory();
    private static int id = 1;

    //Метод для установки нового ID
    private int getNewId() { //Счетчик тасок
        return id++;
    }

    //Добавление новых задач
    @Override
    public Task addTask(Task newTask) {
        newTask.setId(getNewId());
        tasks.put(newTask.getId(), newTask);
        updatePrioritizedTasks();
        return newTask;
    }

    @Override
    public Subtask addSubTask(Subtask newSubtask) throws TimeConflictException {
        if (timeConflict(newSubtask)) {
            throw new TimeConflictException("Задачи пересекаются во времени");
        }
        newSubtask.setId(getNewId());
        subTasks.put(newSubtask.getId(), newSubtask);
        Epic epic = (Epic) epics.get(newSubtask.getEpicId());
        if (epic != null) {
            epic.addSubInEpic(newSubtask);
            updateEpic(epic);
            if (epic.getStartTime() == null || epic.getStartTime().isAfter(newSubtask.getStartTime())) {
                epic.setStartTime(newSubtask.getStartTime());
            }
            LocalDateTime endTimeSubtask = newSubtask.getEndTime();
            if (epic.getEndTime() == null || epic.getEndTime().isBefore(endTimeSubtask)) {
                epic.setEndTime(endTimeSubtask);
            }
            epic.setDuration(epic.getDuration().plus(newSubtask.getDuration()));
        }

        updatePrioritizedTasks();
        return newSubtask;
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        newEpic.setId(getNewId());
        epics.put(newEpic.getId(), newEpic);
        updatePrioritizedTasks();
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
        Set<Integer> taskIds = tasks.values().stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
        taskIds.forEach(historyManager::remove);
        tasks.clear();
        updatePrioritizedTasks();
    }


    @Override
    public void clearSubTasks() {
        Set<Integer> subtaskIdSet = subTasks.values().stream()
                .map(Subtask::getId)
                .collect(Collectors.toSet());
        subtaskIdSet.forEach(historyManager::remove);
        subTasks.clear();
        epics.values().forEach(Epic::clearSubInEpic);
        updatePrioritizedTasks();
    }


    @Override
    public void clearEpic() {
        Set<Integer> epicIdSet = epics.values().stream()
                .map(Epic::getId)
                .collect(Collectors.toSet());
        epicIdSet.forEach(historyManager::remove);
        epics.clear();
        clearSubTasks();
        updatePrioritizedTasks();
    }


    //Удаление задачи по id
    @Override
    public void removeTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        }
        updatePrioritizedTasks();
    }

    @Override
    public void removeSubtask(int id) {
        int epicId = subTasks.get(id).getEpicId();
        epics.get(epicId).removeSubInEpic(id);
        subTasks.remove(id);
        updateEpicStatus(epics.get(epicId));
        historyManager.remove(id);
        updateTimeForEpic(epicId);
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
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

    @Override
    public List<Subtask> getSubtasksEpic(Integer id) {
        return epics.get(id).getSubTasks().stream()
                .map(subTasks::get)
                .collect(Collectors.toList());
    }

    //Обновление задачи
    @Override
    public void updateTask(Task task) { //Обновляем таску
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
        updatePrioritizedTasks();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
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

    private void updateTimeForEpic(int idEpic) {
        Epic epic = epics.get(idEpic);

        if (epic != null) {
            List<Subtask> subtaskList = epic.getSubTasks().stream()
                    .map(subTasks::get)
                    .collect(Collectors.toList());
            ;
            LocalDateTime startTime = subtaskList.stream()
                    .map(Subtask::getStartTime)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);

            LocalDateTime endTime = subtaskList.stream()
                    .map(subtaskObj -> subtaskObj.getStartTime().plus(subtaskObj.getDuration()))
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            epic.setStartTime(startTime);
            epic.setEndTime(endTime);

            Duration duration = subtaskList.stream()
                    .map(Subtask::getDuration)
                    .filter(Objects::nonNull)
                    .reduce(Duration.ZERO, Duration::plus);

            epic.setDuration(duration);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private void updatePrioritizedTasks() {
        prioritizedTasks.clear();
        prioritizedTasks.addAll(tasks.values().stream()
                .filter(task -> task.getStartTime().toLocalDate() != null)
                .toList());

        prioritizedTasks.addAll(subTasks.values().stream()
                .filter(subtask -> subtask.getStartTime().toLocalDate() != null)
                .toList());
    }

    //метод для проверки пересечения задач
    public boolean timeConflict(Task newTask) {
        List<Task> sortTask = getPrioritizedTasks();
        return sortTask.stream()
                .anyMatch(existingTask -> !existingTask.getEndTime().isBefore(newTask.getStartTime()) &&
                        !newTask.getEndTime().isBefore(existingTask.getStartTime()));
    }

    //получаем историю
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}