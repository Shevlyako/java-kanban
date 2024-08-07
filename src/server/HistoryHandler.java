package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equals("GET")) {
            try {
                String history = gson.toJson(taskManager.getHistory());
                sendText(exchange, history);
            } catch (Exception e) {
                sendInternalServerError(exchange);
            }
        } else {
            sendInternalServerError(exchange);
        }
    }
}