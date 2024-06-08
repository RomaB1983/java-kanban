package service.handlers;

import model.Task;


import static service.HttpTaskServer.taskManager;

public class TaskHandler extends BaseHttpHandler<Task> {
    public TaskHandler() {
        firstPartOfPath = "tasks";
        add = taskManager::addTask;
        getById = taskManager::getTask;
        update = taskManager::updateTask;
        getList = taskManager::getTasksList;
        delete = taskManager::deleteTask;
        type = Task.class;
    }
}
