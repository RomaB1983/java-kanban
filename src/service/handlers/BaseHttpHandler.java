package service.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;

import service.exceptions.ManagerException;
import service.exceptions.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static service.HttpTaskServer.gson;

public class BaseHttpHandler<T extends Task> implements HttpHandler {
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected String firstPartOfPath;
    protected Function<Integer, T> getById;
    protected Consumer<T> add;
    protected Consumer<T> update;
    protected Supplier<List<T>> getList;
    protected Consumer<Integer> delete;
    protected Class<T> type;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_ALL -> handleGetAll(exchange, getList);

            case GET_BY_ID -> handleGetById(exchange, getById);

            case POST_CREATE -> handlePostCreate(exchange, add);

            case POST_UPDATE -> handlePostUpdate(exchange, update);

            case DELETE -> handleDelete(exchange, delete);

            default -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }


    protected void handleGetById(HttpExchange exchange, Function<Integer, T> getTask) throws IOException {
        try {
            T task = getTask.apply(getId(exchange));
            String response = gson.toJson(task);
            writeResponse(exchange, response, 200);
        } catch (NotFoundException e) {
            writeResponse(exchange, e.getMessage(), 404);
        } catch (NumberFormatException e) {
            writeResponse(exchange, "Некорректный id", 400);
        }
    }

    protected void handlePostCreate(HttpExchange exchange, Consumer<T> addTask) throws IOException {
        try {
            T t = gson.fromJson(getStringFromBody(exchange), type);
            addTask.accept(t);
            writeResponse(exchange, "", 201);
        } catch (ManagerException e) {
            writeResponse(exchange, e.getMessage(), 406);
        }
    }

    protected void handlePostUpdate(HttpExchange exchange, Consumer<T> updateTask) throws IOException {
        try {
            getId(exchange);
            String body = getStringFromBody(exchange);
            T t = gson.fromJson(body, type);
            updateTask.accept(t);
            writeResponse(exchange, "", 201);
        } catch (ManagerException e) {
            writeResponse(exchange, e.getMessage(), 406);
        } catch (NumberFormatException e) {
            writeResponse(exchange, "Некорректный id", 400);
        }
    }

    protected void handleGetAll(HttpExchange exchange, Supplier<List<T>> getAllTasks) throws IOException {
        String response = gson.toJson(getAllTasks.get());
        writeResponse(exchange, response, 200);
    }

    protected void handleDelete(HttpExchange exchange, Consumer<Integer> deleteTask) throws IOException {
        try {
            deleteTask.accept(getId(exchange));
            writeResponse(exchange, "", 200);
        } catch (NumberFormatException e) {
            writeResponse(exchange, "Некорректный id", 400);
        }
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts[1].equals(firstPartOfPath)) {
            if (pathParts.length == 2) {
                switch (requestMethod) {
                    case "GET" -> {
                        return Endpoint.GET_ALL;
                    }
                    case "POST" -> {
                        return Endpoint.POST_CREATE;
                    }
                }
            }
            if (pathParts.length == 3) {
                switch (requestMethod) {
                    case "GET" -> {
                        return Endpoint.GET_BY_ID;
                    }
                    case "POST" -> {
                        return Endpoint.POST_UPDATE;
                    }
                    case "DELETE" -> {
                        return Endpoint.DELETE;
                    }
                }
            }
        }

        return Endpoint.UNKNOWN;
    }

    protected void writeResponse(HttpExchange exchange,
                                 String responseString,
                                 int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected Integer getId(HttpExchange exchange) throws NumberFormatException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        return Integer.parseInt(pathParts[2]);
    }

    protected String getStringFromBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        return new String(inputStream.readAllBytes());
    }
}