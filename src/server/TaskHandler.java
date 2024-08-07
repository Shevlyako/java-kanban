package server;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.TimeConflictException;
import manager.TaskManager;
import modeltask.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    handleGet(exchange);
                    break;
                }
                case "POST": {
                    handlePost(exchange);
                    break;
                }
                case "DELETE": {
                    handleDelete(exchange);
                    break;
                }
                default: {
                    sendNotFound(exchange, "Несуществующий ресурс");
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private int parsePathID(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (Pattern.matches("^/tasks$", path)) {
            try {
                String response = gson.toJson(taskManager.getTasks());
                sendText(exchange, response);
            } catch (Exception e) {
                sendInternalServerError(exchange);
            }
        }
        if (Pattern.matches("^/tasks/\\d+$", path)) {
            try {
                String pathId = path.replaceFirst("/tasks/", "");
                int id = parsePathID(pathId);
                if (id != -1) {
                    String response = gson.toJson(taskManager.getTaskId(id));
                    sendText(exchange, response);
                }
            } catch (NotFoundException e) {
                sendNotFound(exchange, "Задача не найдена");
            } catch (Exception e) {
                sendInternalServerError(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (Pattern.matches("^/tasks$", path)) {
            try {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task task1 = gson.fromJson(body, Task.class);
                taskManager.addTask(task1);
                sendAdd(exchange, "Задача создана");
            } catch (TimeConflictException exception) {
                sendHasInteractions(exchange, "Данное время занято");
            } catch (Exception e) {
                sendInternalServerError(exchange);
            }
        }
        if (Pattern.matches("^/tasks/\\d+$", path)) {
            try {
                String pathId = path.replaceFirst("/tasks/", "");
                int id = parsePathID(pathId);
                if (id != -1) {
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    taskManager.updateTask(task);
                    sendAdd(exchange, "Задача обновлена");
                }
            } catch (TimeConflictException e) {
                sendHasInteractions(exchange, "Данное время занято");
            } catch (Exception e) {
                sendInternalServerError(exchange);
            }
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        boolean isDeleteAll = Pattern.matches("^/tasks$", path);

        try {
            if (isDeleteAll) {
                taskManager.clearTasks();
                System.out.println("Удалены все задачи");
            } else if (Pattern.matches("^/tasks/\\d+$", path)) {
                String pathId = path.replaceFirst("/tasks/", "");
                int id = parsePathID(pathId);
                if (id != -1) {
                    taskManager.removeTask(id);
                    System.out.println("Удаленa задача под индексом " + id);
                }
            }
            exchange.sendResponseHeaders(200, 0);
        } catch (NotFoundException exception) {
            sendNotFound(exchange, "Задача не найдена");
        } catch (Exception exception) {
            sendInternalServerError(exchange);
        }
    }
}