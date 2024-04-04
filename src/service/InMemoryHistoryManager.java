package service;

import model.Task;
import service.interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int SIZE_HISTORY_LIST = 10;
    private final List<Task> history;

    public InMemoryHistoryManager() {
        history = new ArrayList<>(SIZE_HISTORY_LIST);
    }

    @Override
    public void add(Task task) {
        if (history.size() == SIZE_HISTORY_LIST) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    /* Поясни пожалуйста, хотел спросить еще на прошлом спринте.. Почему нужно возврщать копию, а не сам массив
     Я понимаю, что это связано с тем, что принимающая сторона получит на прямую доступ к массиву, если вовзращать не
     копию. И может его, например удалить, что не желательно).
     Но, с другой стороны, отправля копию, внутри массива все равно будут реальные ссылки на Epic,Tastk, SubTask.
     и их можно будет поменять при желании.. Или я ошибаюсь?
     */
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
