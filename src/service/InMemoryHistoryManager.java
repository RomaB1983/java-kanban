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
        System.out.println("history.task " + history.get(id));
        removeNode(history.get(id));
        history.remove(id);
        System.out.println("history.task del " + history.get(id));
    }

    @Override
    public void remove(Collection<Integer> ids) {
        for(int id :ids){
            remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private Node linkLast(Task task) {
        final Node newNode = new Node(tail, task, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail=newNode;
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

    private List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }
    private static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Node prev, Task element, Node next) {
            this.task = element;
            this.next = next;
            this.prev = prev;
        }
    }

}
