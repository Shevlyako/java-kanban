package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    private void sendResponse(HttpExchange h, int statusCode, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        sendResponse(h, 200, text);
    }

    protected void sendAdd(HttpExchange h, String text) throws IOException {
        sendResponse(h, 201, text);
    }

    public void sendNotFound(HttpExchange h, String text) throws IOException {
        sendResponse(h, 404, text);
    }

    public void sendHasInteractions(HttpExchange h, String text) throws IOException {
        sendResponse(h, 406, text);
    }

    public void sendInternalServerError(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(500, 0);
    }
}