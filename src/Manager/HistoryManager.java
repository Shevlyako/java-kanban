package Manager;

import ModelTask.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addHistory(Task task);

    ArrayList<Task> getHistory();
}
