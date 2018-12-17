package indi.hiro.common.ds.generic;

import com.sun.istack.internal.NotNull;

import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/11/10.
 */
public class ArgumentedLinkedList<T, A> {

    ArgumentGenerator<T, A> argG;

    NodeALL<T, A> head = null;

    NodeALL<T, A> tail = null;

    int size = 0;

    public ArgumentedLinkedList(@NotNull ArgumentGenerator<T, A> argumentGenerator) {
        this.argG = argumentGenerator;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public int size() {
        return size;
    }

    public T itemFromFirst(int i) {
        NodeALL<T, A> node = head;
        while (i-- > 0) {
            node = node.next;
        }
        return node.item;
    }

    public T itemFromLast(int i) {
        NodeALL<T, A> node = tail;
        while (i-- > 0) {
            node = node.prev;
        }
        return node.item;
    }

    public A argFromFirst(int i) {
        NodeALL<T, A> node = head;
        while (i-- > 0) {
            node = node.next;
        }
        if (node.arg == null) {
            node.arg = argG.between(node.item, node.next.item);
        }
        return node.arg;
    }

    public A argFromLast(int i) {
        NodeALL<T, A> node = tail;
        while (i-- >= 0) {
            node = node.prev;
        }
        if (node.arg == null) {
            node.arg = argG.between(node.item, node.next.item);
        }
        return node.arg;
    }

    public void iterateItemFormFirst(Consumer<T> consumer) {
        NodeALL<T, A> node = head;
        while (node != null) {
            consumer.accept(node.item);
            node = node.next;
        }
    }

    public void iterateItemFormLast(Consumer<T> consumer) {
        NodeALL<T, A> node = tail;
        while (node != null) {
            consumer.accept(node.item);
            node = node.prev;
        }
    }

    public void iterateArgFormFirst(Consumer<A> consumer) {
        NodeALL<T, A> node = head;
        if (node == null) {
            return;
        }
        while (node.next != null) {
            consumer.accept(node.arg);
            node = node.next;
        }
    }

    public void iterateArgFormLast(Consumer<A> consumer) {
        NodeALL<T, A> node = tail;
        if (node == null) {
            return;
        }
        while (node.prev != null) {
            node = node.prev;
            consumer.accept(node.arg);
        }
    }

    public void addFirst(T t) {
        if (head != null) {
            NodeALL<T, A> node = new NodeALL<>(null, head, t, argG.between(t, head.item));
            head.prev = node;
            head = node;
        } else {
            NodeALL<T, A> node = new NodeALL<>(null, null, t, null);
            head = node;
            tail = node;
        }
    }

    public void addLast(T t) {
        if (tail != null) {
            NodeALL<T, A> node = new NodeALL<>(tail, null, t, null);
            tail.next = node;
            tail.arg = argG.between(tail.item, t);
            tail = node;
        } else {
            NodeALL<T, A> node = new NodeALL<>(null, null, t, null);
            head = node;
            tail = node;
        }
    }

    public interface ArgumentGenerator<T, A> {

        A between(T t1, T t2);
    }

    static class NodeALL<T, A> {

        NodeALL<T, A> prev;

        NodeALL<T, A> next;

        T item; // content of this

        A arg; // arg of link bet this and next

        NodeALL(NodeALL<T, A> prev, NodeALL<T, A> next, T item, A arg) {
            this.prev = prev;
            this.next = next;
            this.item = item;
            this.arg = arg;
        }
    }
}
