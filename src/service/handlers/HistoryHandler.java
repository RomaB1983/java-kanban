package service.handlers;

import com.sun.net.httpserver.HttpExchange;
import model.Task;

import java.io.IOException;


import static service.HttpTaskServer.gson;
import static service.HttpTaskServer.taskManager;

public class HistoryHandler extends BaseHttpHandler<Task> {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (endpoint == Endpoint.GET_HISTORY) {
            handleGetHistory(exchange);
        } else {
            writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    public HistoryHandler() {
        firstPartOfPath = "history";
    }

    @Override
    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts[1].equals(firstPartOfPath)) {
            return Endpoint.GET_HISTORY;
        }
        return Endpoint.UNKNOWN;
    }

    protected void handleGetHistory(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(taskManager.getHistory());
            writeResponse(exchange, response, 200);
        } catch (NumberFormatException e) {
            writeResponse(exchange, "Некорректный id", 400);
        }
    }
}
