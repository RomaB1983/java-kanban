package service;

import model.Task;
import service.interfaces.HistoryManager;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;

    private Node tail;

    private final HashMap<Integer, Node> history;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            removeNode(history.get(task.getId()));
            history.put(task.getId(), linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private Node linkLast(Task task) {
        final Node last = tail;
        final Node newNode = new Node(last, task, null);
        tail = newNode;
        if (last == null) {
            head = newNode;
        } else {
            last.next = newNode;
        }
        return newNode;
    }

    private void removeNode(Node node) {
        if (node != null) {
            final Node next = node.next;
            final Node prev = node.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
        }
    }

    /**
     * В ТЗ написано, что надо из двусвязногосписка формировать список таск.
     * Можешь пояснить зачем? Почему нельзя собрать их из history через values(), и также в цикле собрать List?
     * На мой взгляд выглядит логичнее. Так мы точно соберем то, что в истории..
     * Вроде и не дольше должна работать такая реализация..
    */
    private List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }
}
