package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.DataStructureException;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/11/10.
 */
public class BidirectionalLinkedList<T> implements Iterable<T> {

    NodeBLL<T> head = null;

    NodeBLL<T> tail = null;

    int size = 0;

    boolean reverse = false;

    public BidirectionalLinkedList() {

    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public boolean isReverse() {
        return reverse;
    }

    public int size() {
        return size;
    }

    public T itemFromFirst(int i) {
        NodeBLL<T> node = head;
        while (i-- > 0) {
            node = node.next;
        }
        return node.item;
    }

    public T itemFromLast(int i) {
        NodeBLL<T> node = tail;
        while (i-- > 0) {
            node = node.prev;
        }
        return node.item;
    }

    /**
     * From default direction indicated by "reverse"
     */
    public T item(int i) {
        if (reverse) {
            return itemFromLast(i);
        } else {
            return itemFromFirst(i);
        }
    }

    public void iterateItemFormFirst(Consumer<T> consumer) {
        NodeBLL<T> node = head;
        while (node != null) {
            consumer.accept(node.item);
            node = node.next;
        }
    }

    public void iterateItemFormLast(Consumer<T> consumer) {
        NodeBLL<T> node = tail;
        while (node != null) {
            consumer.accept(node.item);
            node = node.prev;
        }
    }

    /**
     * From default direction indicated by "reverse"
     */
    public void iterate(Consumer<T> consumer) {
        if (reverse) {
            iterateItemFormLast(consumer);
        } else {
            iterateItemFormFirst(consumer);
        }
    }

    public Iterator<T> iterator(boolean iDir, int i, boolean tDir) {
        NodeBLL<T> start;
        if (iDir) {
            start = tail;
            while (i-- > 0) start = start.prev;
        } else {
            start = head;
            while (i-- > 0) start = start.next;
        }
        if (tDir) {
            return new LinkedListTailIterator<>(start);
        } else {
            return new LinkedListHeadIterator<>(start);
        }
    }

    /**
     * From default direction indicated by "reverse"
     */
    @Override
    public Iterator<T> iterator() {
        if (reverse) {
            return new LinkedListTailIterator<>(tail);
        } else {
            return new LinkedListHeadIterator<>(head);
        }
    }

    public void addFirst(T t) {
        NodeBLL<T> head = this.head;
        NodeBLL<T> n = new NodeBLL<>(null, head, t);
        if (head != null) {
            head.prev = n;
        } else if (tail == null) {
            tail = n;
        } else {
            throw new DataStructureException();
        }
        this.head = n;
        size++;
    }

    public void addLast(T t) {
        NodeBLL<T> tail = this.tail;
        NodeBLL<T> n = new NodeBLL<>(tail, null, t);
        if (tail != null) {
            tail.next = n;
        } else if (head == null) {
            head = n;
        } else {
            throw new DataStructureException();
        }
        this.tail = n;
        size++;
    }

    private void unlink(NodeBLL<T> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else if (head == node) {
            head = node.next;
        } else {
            throw new DataStructureException();
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else if (tail == node) {
            tail = node.prev;
        } else {
            throw new DataStructureException();
        }
    }

    public T removeFromLast(int i) {
        NodeBLL<T> node = tail;
        while (i-- > 0) {
            node = node.prev;
        }
        unlink(node);
        size--;
        return node.item;
    }

    static class NodeBLL<T> {

        NodeBLL<T> prev;

        NodeBLL<T> next;

        T item;

        NodeBLL(NodeBLL<T> prev, NodeBLL<T> next, T item) {
            this.prev = prev;
            this.next = next;
            this.item = item;
        }
    }

    static class LinkedListHeadIterator<T> implements Iterator<T> {

        NodeBLL<T> node;

        LinkedListHeadIterator(NodeBLL<T> head) {
            this.node = head;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public T next() {
            T t = node.item;
            node = node.next;
            return t;
        }
    }

    static class LinkedListTailIterator<T> implements Iterator<T> {

        NodeBLL<T> node;

        LinkedListTailIterator(NodeBLL<T> tail) {
            this.node = tail;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public T next() {
            T t = node.item;
            node = node.prev;
            return t;
        }
    }
}
