import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {
    private HashMap<Integer, SubTask> subTasksInEpic = new HashMap<>();

    public Epic(String name, String description, TaskStatus taskStatus) {
        super(name, description, taskStatus);
    }

    public Epic(String name, String description, TaskStatus taskStatus, int id, List<SubTask> subTasksInEpic) {
        super(name, description, taskStatus, id);
        this.subTasksInEpic = (HashMap<Integer, SubTask>) subTasksInEpic;
    }

    public void addSubInEpic(SubTask subTask){
        subTasksInEpic.put(subTask.getId(), subTask);
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasksInEpic;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", subTasks=" + getSubTasks() +
                ", status=" + getStatus() +
                '}';
    }
}
