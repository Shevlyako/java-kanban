package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class PriorityzedHandler extends BaseHttpHandler {
    public PriorityzedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            try {
                String prioritized = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(exchange, prioritized);
            } catch (Exception e) {
                sendInternalServerError(exchange);
            }
        } else {
            sendInternalServerError(exchange);
        }
    }
}