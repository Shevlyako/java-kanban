package modeltask;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epicId = epic.getId();
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "ModelTask.Subtask{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", ModelTask.Epic= id " + epicId +
                ", status=" + getStatus() +
                '}';
    }
}
