package manager;

import modeltask.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    @Override
    public void addHistory(Task task) { //По ТЗ указано что необходимо выводить все апросы в том числе повторяющиеся
        if (task != null) {
            history.add(new Task(task));
        }
        if (history.size() > 10) {
            history.remove(0);
        }
    }
    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
