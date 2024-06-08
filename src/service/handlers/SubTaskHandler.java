package service.handlers;

import model.SubTask;

import static service.HttpTaskServer.taskManager;

public class SubTaskHandler extends BaseHttpHandler<SubTask>  {
    public SubTaskHandler() {
        firstPartOfPath = "subtasks";
        add = taskManager::addSubTask;
        getById = taskManager::getSubTask;
        update = taskManager::updateSubTask;
        getList = taskManager::getSubTasksList;
        delete = taskManager::deleteTask;
        type = SubTask.class;
    }
}
