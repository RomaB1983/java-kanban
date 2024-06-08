package service.handlers;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.exceptions.NotFoundException;

import java.io.IOException;


import static service.HttpTaskServer.gson;
import static service.HttpTaskServer.taskManager;

public class EpicHandler extends BaseHttpHandler<Epic> {

    public EpicHandler() {
        firstPartOfPath = "epics";
        add = taskManager::addEpic;
        getById = taskManager::getEpic;
        update = taskManager::updateEpic;
        getList = taskManager::getEpicsList;
        delete = taskManager::deleteEpic;
        type = Epic.class;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        if (endpoint == Endpoint.GET_LIST) {
            handleGetList(exchange);
        } else {
            super.handle(exchange);
        }
    }

    private void handleGetList(HttpExchange exchange) throws IOException {
        try {
            String response = gson.toJson(taskManager.getSubTasksByEpic(getId(exchange)));
            writeResponse(exchange, response, 200);
        } catch (NumberFormatException e) {
            writeResponse(exchange, "Некорректный id", 400);
        } catch (NotFoundException e) {
            writeResponse(exchange, e.getMessage(), 404);
        }
    }

    @Override
    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts[0].equals(firstPartOfPath) && pathParts.length == 4 && pathParts[3].equals("subtasks")
                && requestMethod.equals("GET")) {
            return Endpoint.GET_LIST;
        }
        return super.getEndpoint(requestPath, requestMethod);
    }
}
