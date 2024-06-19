package service;

import model.Epic;
import model.SubTask;
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

public class EpicHandlerTest extends BaseHttpHandlerTest<Epic> {

    public EpicHandlerTest() {
        firstPartOfPath = "epics";
        add = manager::addEpic;
        getById = manager::getEpic;
        update = manager::updateEpic;
        getList = manager::getEpicsList;
        delete = manager::deleteEpic;
    }

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        Epic task = new Epic("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIGetTask(task, add, getList, false);
    }

    @Test
    public void testGetByIdEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIGetTask(task, add, getList, true);
    }

    @Test
    public void testNotFoundEpic() throws IOException, InterruptedException {
        testAPINotFoundTask();
    }

     @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIAdd(task, add, getList);
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIUpdate(task, add, getList);
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic task = new Epic("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIDelete(task, add, getList);
    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());
        SubTask subTask1 = new SubTask("Упаковать вещи", "Упаоквать вещи в коробку и перемотать скотчем",Duration.ofMinutes(10), LocalDateTime.parse("2024-09-06T11:50:55"));
        manager.addEpic(epic);
        subTask1.setEpicId(epic.getId());
        manager.addSubTask(subTask1);
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks" );
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<SubTask> subtasksList = manager.getSubTasksByEpic(epic.getId());

        assertNotNull(subtasksList, "Задачи не возвращаются");
        assertEquals(1, subtasksList.size(), "Некорректное количество задач");
    }

}
