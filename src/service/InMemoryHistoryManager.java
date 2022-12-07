package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements  HistoryManager  {

    private Node<Task> tail = null;
    private Node<Task> head = null;
    private final HashMap<Integer, Node<Task>> uniqueTasks = new HashMap<>();
    @Override
    public ArrayList<Task> getHistory() {
        return  getTasks();
    }

    @Override
    public void add(Task task) {
        if (uniqueTasks.containsKey(task.getId())) {
            removeNode(getNode(task));
        }
        linkLast(task);
    }

    @Override
    public void remove (int id) {
        removeNode(uniqueTasks.get(id));
    }

    private Node<Task> getNode(Task task) {
        for (Node<Task> current = head; current != null; current = current.next) {
            if (current.data == task) {
                return current;
            }
        }
        return null;
    }

    public void linkLast(Task task) {
        Node<Task> node;
        if (tail != null && head != null) {
            node = new Node<>(null, tail, task);
            tail.next = node;
        } else {
            node = new Node<>(null, null, task);
            head = node;
        }
        tail = node;
        uniqueTasks.put(node.data.getId(), node);
    }

    public void removeNode(Node<Task> node) {

        if (node == head) {
            head = head.next;
        } else if (node == tail) {
            tail = tail.prev;
        } else {
            node.prev.next = node.next;
        }
        node.data = null;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Node<Task> current = head; current != null; current = current.next) {
            allTasks.add(current.data);
        }
        return allTasks;
    }
 }

class Node <T> {
    Node next;
    Node prev;
    T data;

    public Node (Node next, Node prev, T data) {
        this.next = next;
        this.prev = prev;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}