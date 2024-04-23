public class SubTask extends Task {
    private int epicId;
    public SubTask(String name, String description,TaskStatus taskStatus, int epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", description=" + getDescription() +
                ", Epic= id " + epicId +
                ", status=" + getStatus() +
                '}';
    }

    public int getEpicId() {
        return epicId;
    }
}
