package manager;

import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;

import java.util.ArrayList;

public interface TaskManager {
    Task addTask(Task newTask);

    Subtask addSubTask(Subtask newSubtask);

    Epic addEpic(Epic newEpic);

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubTasks();

    ArrayList<Epic> getEpics();

    void clearTasks();

    void clearSubTasks();

    void clearEpic();

    void removeTask(int id);

    void removeSubtask(int id);

    void removeEpic(int id);

    Task getTaskId(int id);

    Subtask getSubtaskId(int id);

    Epic getEpicId(int id);

    void updateTask(Task task);

    void updateEpicStatus(Epic epic);

    void updateEpic(Epic epic);

    void updateSubTask(Subtask subtask);
    ArrayList<Task> getHistory();
}
