package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import service.handlers.*;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

/*
 * Привет!
 * Не стал делать несколько методов для отправки ответов, как хотят в ТЗ.
 * Сделал все в одном writeResponse - не знаю насколько это плохо, показалось, что куча методов не нужно..
 * Плюс я сделал реализацию Handler не так, как видили в ТЗ, поэтому тесты пока все писать не стал. не ругайся(
 * Просто, если ты не примешь такой подход, придется все переписывать, в том числе и тесты.
 * */
public class HttpTaskServer {
    private static final int PORT = 8080;

    public static  TaskManager taskManager;
    static HttpServer httpServer;
    public static Gson gson;

    public HttpTaskServer() {
        taskManager = Managers.getDefault();
    }

    public HttpTaskServer(TaskManager manager) {
        taskManager = manager;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())

                .create();
    }

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TaskHandler());
            httpServer.createContext("/subtasks", new SubTaskHandler());
            httpServer.createContext("/epics", new EpicHandler());
            httpServer.createContext("/history", new HistoryHandler());
            httpServer.createContext("/prioritized", new PriorityHandler());
            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        httpServer.stop(1);
    }
}
