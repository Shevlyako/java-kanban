package modeltask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Duration.ofMinutes(0), null);
    }

    public void addSubInEpic(Subtask subtask) {
        subTasksId.add(subtask.getId());
    }

    public void deleteSubInEpic(int id) {
        subTasksId.remove(id);
    }

    public ArrayList<Integer> getSubTasks() {
        return new ArrayList<>(subTasksId);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void clearSubInEpic() {
        subTasksId.clear();
        setStartTime(null);
        setEndTime(null);
        setDuration(Duration.ofMinutes(0));
    }

    public void removeSubInEpic(Integer id) {
        subTasksId.remove(id);
    }

    @Override
    public String toString() {
        return "ModelTask.Epic{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", subTasks=" + getSubTasks() +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksId, epic.subTasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksId);
    }


}
