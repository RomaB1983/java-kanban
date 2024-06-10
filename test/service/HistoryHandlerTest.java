package service;

import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryHandlerTest extends BaseHttpHandlerTest<Task> {

    public HistoryHandlerTest() {
        firstPartOfPath = "history";
    }

    @Test
    public void testGetPiroritizied() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());
        Task task1 = new Task("Test 1", "Testing task 2",
                Duration.ofMinutes(15), LocalDateTime.parse("2024-10-08T11:50:55"));

        manager.addTask(task);
        manager.addTask(task1);
        manager.getTask(task.getId());
        manager.getTask(task1.getId());

        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history" );
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.getHistory();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(2, tasks.size(), "Некорректное количество задач");
    }
}
