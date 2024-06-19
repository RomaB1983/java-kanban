package service;

import model.SubTask;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskHandlerTest extends BaseHttpHandlerTest<SubTask> {

    public SubTaskHandlerTest() {
        firstPartOfPath = "subtasks";
        add = manager::addSubTask;
        getById = manager::getSubTask;
        update = manager::updateSubTask;
        getList = manager::getSubTasksList;
        delete = manager::deleteSubTask;
    }

    @Test
    public void testGetAllSubTasks() throws IOException, InterruptedException {
        SubTask task = new SubTask("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIGetTask(task, add, getList, false);
    }

    @Test
    public void testGetByIdSubTask() throws IOException, InterruptedException {
        SubTask task = new SubTask("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIGetTask(task, add, getList, true);
    }

    @Test
    public void testNotFoundSubTask() throws IOException, InterruptedException {
        testAPINotFoundTask();
    }

    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        SubTask task = new SubTask("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIAdd(task, add, getList);
    }

    @Test
    public void testAddIsIntersectSubTask() throws IOException, InterruptedException {
        SubTask task = new SubTask("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());
        SubTask task2 = new SubTask("Test 3", "Testing task 3",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIAddIsIntersect(task, task2, add);
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        SubTask task = new SubTask("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());
        testAPIUpdate(task, add, getList);
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        SubTask task = new SubTask("Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        testAPIDelete(task, add, getList);
    }
}
