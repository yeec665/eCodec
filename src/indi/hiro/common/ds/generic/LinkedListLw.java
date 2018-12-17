package indi.hiro.common.ds.generic;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/11/10.
 *
 * A light weight linked list
 */
public class LinkedListLw<T> implements Iterable<T> {

    NodeLL<T> head = null;

    public LinkedListLw() {

    }

    public T itemFormFirst(int i) {
        NodeLL<T> node = head;
        while (i-- > 0) {
            node = node.next;
        }
        return node.item;
    }

    public void iterateItemFormFirst(Consumer<T> consumer) {
        NodeLL<T> node = head;
        while (node != null) {
            consumer.accept(node.item);
            node = node.next;
        }
    }

    public void addFirst(T t) {
        head = new NodeLL<>(head, t);
    }

    public T removeFirst() {
        T removed = head.item;
        head = head.next;
        return removed;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator<>(head);
    }

    static class NodeLL<T> {

        NodeLL<T> next;

        T item;

        NodeLL(NodeLL<T> next, T item) {
            this.next = next;
            this.item = item;
        }
    }

    static class LinkedListIterator<T> implements Iterator<T> {

        NodeLL<T> node;

        LinkedListIterator(NodeLL<T> head) {
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
}
