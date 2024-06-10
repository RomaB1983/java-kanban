package service;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static service.HttpTaskServer.gson;

public class BaseHttpHandlerTest<T extends Task> {

    TaskManager manager = new InMemoryTaskManager();

    HttpTaskServer taskServer = new HttpTaskServer(manager);

    protected String firstPartOfPath;
    protected Function<Integer, T> getById;
    protected Consumer<T> add;
    protected Consumer<T> update;
    protected Supplier<List<T>> getList;
    protected Consumer<Integer> delete;

    public BaseHttpHandlerTest() {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        HttpTaskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    public void testAPIGetTask(T task, Consumer<T> addTask, Supplier<List<T>> getAllTasks, Boolean isGetTaskById) throws IOException, InterruptedException {
        addTask.accept(task);
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + firstPartOfPath + ((isGetTaskById) ? "/1" : "/"));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<T> tasksFromManager = getAllTasks.get();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    public void testAPINotFoundTask() throws IOException, InterruptedException {
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + firstPartOfPath + "/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    public void testAPIAdd(T task, Consumer<T> addTask, Supplier<List<T>> getAllTasks) throws IOException, InterruptedException {
        addTask.accept(task);
        String taskJson = gson.toJson(task);
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + firstPartOfPath);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<T> tasksFromManager = getAllTasks.get();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    public void testAPIUpdate(T task, Consumer<T> addTask, Supplier<List<T>> getAllTasks) throws IOException, InterruptedException {
        addTask.accept(task);
        task.setName("Test_3");
        String taskJson = gson.toJson(task);

        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + firstPartOfPath + "/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<T> tasksFromManager = getAllTasks.get();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test_3", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    public void testAPIAddIsIntersect(T task, T task2, Consumer<T> addTask) throws IOException, InterruptedException {
        addTask.accept(task);
        String taskJson = gson.toJson(task2);

        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + firstPartOfPath);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    public void testAPIDelete(T task, Consumer<T> addTask, Supplier<List<T>> getAllTasks) throws IOException, InterruptedException {

        addTask.accept(task);
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + firstPartOfPath + "/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<T> tasksFromManager = getAllTasks.get();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }
}