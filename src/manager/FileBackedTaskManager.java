package manager;

import exception.ManagerSaveException;
import modeltask.Epic;
import modeltask.Subtask;
import modeltask.Task;
import modeltask.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    static String title = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) {
        this.file = file;
        save();
    }

    @Override
    public Task addTask(Task newTask) {
        Task task1 = super.addTask(newTask);
        save();
        return task1;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic epic1 = super.addEpic(epic);
        save();
        return epic1;
    }

    @Override
    public Subtask addSubTask(Subtask subtask) {
        Subtask subtask1 = super.addSubTask(subtask);
        save();
        return subtask1;
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


    public void save() {
        String fileName = "fileTaskManager.csv";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.toURI()), StandardCharsets.UTF_8)) {
            writer.write(title);
            writer.newLine();
            for (Task pair : tasks.values()) {
                writer.write(toString(pair));
                writer.newLine();
            }
            for (Subtask pair : subTasks.values()) {
                writer.write(toString(pair));
                writer.newLine();
            }
            for (Epic pair : epics.values()) {
                writer.write(toString(pair));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранит данные " + "\n ошибка", e.getCause());
        }
    }

    public void loadFromFile() {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String line = bufferedReader.readLine();
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                if (line.equals("")) {
                    break;
                }

                Task task = fromString(line);

                if (task instanceof Epic epic) {
                    addEpic(epic);
                } else if (task instanceof Subtask subtask) {
                    addSubTask(subtask);
                } else {
                    addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла!");
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
            valueTask = new String[]{Integer.toString(task.getId()), getType(task).toString(), task.getName(), task.getStatus().toString(),
                    task.getDescription(), Integer.toString(((Subtask) task).getEpicId())};
        } else {
            valueTask = new String[]{Integer.toString(task.getId()), getType(task).toString(), task.getName(), task.getStatus().toString(),
                    task.getDescription()};
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
        if (splitValue.length == 6) {
            int epicId = Integer.parseInt(splitValue[5]);
            task = new Subtask(name, description, epicId);
            task.setId(id);
            task.setStatus(taskStatus);
            return task;
        } else {
            if (taskType == TaskType.TASK) {
                task = new Task(name, description);
                task.setId(id);
                task.setStatus(taskStatus);
                return task;
            } else {
                task = new Epic(name, description);
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
            String lineSeparator = System.lineSeparator();
            String[] lines = Files.readString(file.toPath()).split(lineSeparator);
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
