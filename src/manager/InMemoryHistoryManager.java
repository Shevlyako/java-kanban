package manager;

import modeltask.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    //Добавляем задачу в историю
    @Override
    public void addHistory(Task task) {
        if (task != null) {
            remove(task.getId()); //удаляем старую версию
            linkLast(task); //добавляем новую
        }
    }

    //Удаляем задачу из истории
    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    //Возвращаем историю
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    //Возвращаем список с историей просмотров
    private List<Task> getTasks() {
        List<Task> task = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            task.add(node.data);
            node = node.next;
        }
        return task;
    }

    // добавление нового элемента в конец списка.
    private void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<Task>(oldTail, element, null);
        tail = newNode;
        history.put(element.getId(), newNode);
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
    }

    //Удаление элемента связанного списка
    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;
            node.data = null;
            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && tail != node) {
                head = head.next;
                head.prev = null;
            } else if (head != node && tail == node) {
                tail = tail.prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    // отдельный класс Node для узла списка
    private class Node<Task> {

        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
