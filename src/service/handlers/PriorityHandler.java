package service.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import java.io.IOException;


import static service.HttpTaskServer.taskManager;
import static service.HttpTaskServer.gson;

public class PriorityHandler extends BaseHttpHandler<Task> implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (endpoint == Endpoint.GET_PRIORITY_LIST) {
            handleGetPriorityList(exchange);
        } else {
            writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    public PriorityHandler() {
        firstPartOfPath = "prioritized";
    }

    @Override
    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts[1].equals(firstPartOfPath)) {
            return Endpoint.GET_PRIORITY_LIST;
        }
        return Endpoint.UNKNOWN;
    }

    protected void handleGetPriorityList(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            writeResponse(exchange, response, 200);
        } catch (NumberFormatException e) {
            writeResponse(exchange, "Некорректный id", 400);
        }
    }
}
