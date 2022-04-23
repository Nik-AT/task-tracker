package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    public Node head;
    public Node tail;
    public static Map<Integer, Node> nodeMap = new HashMap<>();

    public static class Node {

        private Task data;
        private Node next;
        private Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public void add(Task task) {
        final Node node = nodeMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        if (node != null) {
            removeNode(node);
            nodeMap.remove(id);
        }
    }

    public void linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> historyArray = new ArrayList<>();
        Node node = head;
        while (node != null) {
            historyArray.add(node.data);
            node = node.next;
        }
        return historyArray;
    }

    public void removeNode(Node node) {
        if (node.next == null && node.prev == null) {
            head = null;
            tail = null;
        }
        if (node == head) {
            head = node.next;
            node.next.prev = null;
        }
        if (node == tail) {
            tail = node.prev;
            node.prev.next = null;
        }
        if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }
}
