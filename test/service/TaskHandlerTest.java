package service;

import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandlerTest extends BaseHttpHandlerTest<Task> {

    public TaskHandlerTest() {
        firstPartOfPath = "tasks";
        add = manager::addTask;
        getById = manager::getTask;
        update = manager::updateTask;
        getList = manager::getTasksList;
        delete = manager::deleteTask;
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIGetTask(task, add, getList, false);
    }

    @Test
    public void testGetByIdTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIGetTask(task, add, getList, true);
    }

    @Test
    public void testNotFoundTask() throws IOException, InterruptedException {
        testAPINotFoundTask();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIAdd(task, add, getList);
    }

    @Test
    public void testAddIsIntersectTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Test 3", "Testing task 3",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIAddIsIntersect(task, task2, add);
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIUpdate(task, add, getList);
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIDelete(task, add, getList);
    }
}
