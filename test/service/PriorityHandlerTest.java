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

public class PriorityHandlerTest extends BaseHttpHandlerTest<Task> {

    public PriorityHandlerTest() {
        firstPartOfPath = "prioritized";
    }

    @Test
    public void testGetPiroritizied() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());
        Task task1 = new Task("Test 1", "Testing task 2",
                Duration.ofMinutes(15), LocalDateTime.parse("2024-10-08T11:50:55"));

        manager.addTask(task);
        manager.addTask(task1);

        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized" );
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.getPrioritizedTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.get(0).getId(), "Некорректное количество задач");
        assertEquals(2, tasks.get(1).getId(), "Некорректное количество задач");

    }
}
