package Manager;

import ModelTask.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            history.add(new Task(task));
        }
        if (history.size() > 10) {
            history.remove(0);
        }
    }
    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}
