package service;

import model.Task;

public class Node {
    Task task;
    Node next;
    Node prev;

    public Node(Node prev, Task element, Node next) {
        this.task = element;
        this.next = next;
        this.prev = prev;
    }
}