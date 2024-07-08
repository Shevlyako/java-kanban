package manager;

import exception.ManagerSaveException;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;
import modeltask.TaskStatus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    static String title = "id,type,name,status,description,epic, startTime, duration, endTime";

    public FileBackedTaskManager(File file) {
        this.file = file;
        save();
    }

    @Override
    public Task addTask(Task newTask) {
        if (newTask != null) {
            Task task1 = super.addTask(newTask);
            save();
            return task1;
        } else {
            return null;
        }
    }

    @Override
    public Epic addEpic(Epic newEpic) {
        if (newEpic != null) {
            Epic epic1 = super.addEpic(newEpic);
            save();
            return epic1;
        } else {
            return null;
        }
    }

    @Override
    public Subtask addSubTask(Subtask newSubtask) {
        if (newSubtask != null) {
            Subtask subtask1 = super.addSubTask(newSubtask);
            save();
            return subtask1;
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public ArrayList<Task> getTasks() {
        save();
        return super.getTasks();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        save();
        return super.getEpics();
    }

    @Override
    public ArrayList<Subtask> getSubTasks() {
        save();
        return super.getSubTasks();
    }

    @Override
    public List<Subtask> getSubtasksEpic(Integer id) {
        return super.getSubtasksEpic(id);
    }


    public void save() {
        String fileName = "fileTaskManager.csv";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.toURI()), StandardCharsets.UTF_8)) {
            writer.write(title);
            writer.write("\n");
            for (Task pair : tasks.values()) {
                writer.write(toString(pair));
                writer.write("\n");
            }
            for (Subtask pair : subTasks.values()) {
                writer.write(toString(pair));
                writer.write("\n");
            }
            for (Epic pair : epics.values()) {
                writer.write(toString(pair));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранит данные " + "\n ошибка", e.getCause());
        }
    }

    private TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;
    }

    private String toString(Task task) {
        String[] valueTask = new String[6];
        if (task instanceof Subtask) {
            valueTask = new String[]{Integer.toString(task.getId()), getType(task).toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(), Integer.toString(((Subtask) task).getEpicId()),
                    String.valueOf(task.getStartTime()), String.valueOf(task.getDuration()), String.valueOf(task.getEndTime())};
        } else {
            valueTask = new String[]{Integer.toString(task.getId()), getType(task).toString(), task.getName(),
                    task.getStatus().toString(), task.getDescription(), String.valueOf(task.getStartTime()),
                    String.valueOf(task.getDuration()), String.valueOf(task.getEndTime())};
        }
        return String.join(",", valueTask);
    }

    private static Task fromString(String value) {
        String[] splitValue = value.split(",");
        int id = Integer.parseInt(splitValue[0]);
        TaskType taskType = TaskType.valueOf(splitValue[1]);
        String name = splitValue[2];
        TaskStatus taskStatus = TaskStatus.valueOf(splitValue[3]);
        String description = splitValue[4];
        Task task = null;
        if (splitValue.length == 9) {
            int epicId = Integer.parseInt(splitValue[5]);
            LocalDateTime startTime = LocalDateTime.parse(splitValue[6]);
            Duration duration = Duration.parse(splitValue[7]);
            task = new Subtask(name, description, duration, startTime, epicId);
            task.setId(id);
            task.setStatus(taskStatus);
            return task;
        } else {
            if (taskType == TaskType.TASK) {
                LocalDateTime startTime = LocalDateTime.parse(splitValue[5]);
                Duration duration = Duration.parse(splitValue[6]);
                task = new Task(name, description, duration, startTime);
                task.setId(id);
                task.setStatus(taskStatus);
                return task;
            } else {
                LocalDateTime startTime = LocalDateTime.parse(splitValue[5]);
                Duration duration = Duration.parse(splitValue[6]);
                task = new Epic(name, description);
                task.setStartTime(startTime);
                task.setDuration(duration);
                task.setId(id);
                task.setStatus(taskStatus);
                return task;
            }
        }
    }

    private static TaskType toEnum(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            String[] lines = Files.readString(file.toPath()).split("\n");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
            for (String taskString : lines) {
                if (taskString.equals(title) || taskString.isBlank()) {
                    continue;
                }
                Task task = fromString(taskString);
                TaskType typeOfTask = toEnum(task);
                switch (typeOfTask) {
                    case TASK -> fileBackedTaskManager.addTask(task);
                    case EPIC -> fileBackedTaskManager.addEpic((Epic) task);
                    case SUBTASK -> fileBackedTaskManager.addSubTask((Subtask) task);
                    default -> throw new IllegalStateException("Неверное значение: " + typeOfTask);
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось востановить данные " + "\n ошибка", e.getCause());
        }
    }
}
