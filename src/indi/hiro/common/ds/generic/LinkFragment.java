package indi.hiro.common.ds.generic;

import java.util.Iterator;

/**
 * Created by Hiro on 2018/12/1.
 */
public class LinkFragment<T> implements Iterable<T> {

    LinkFragment<T> prev;
    LinkFragment<T> next;

    public T item;

    public LinkFragment(LinkFragment<T> prev, LinkFragment<T> next, T item) {
        this.prev = prev;
        this.next = next;
        this.item = item;
    }

    public LinkFragment(T item) {
        this.prev = this;
        this.next = this;
        this.item = item;
    }

    public LinkFragment<T> prev() {
        return prev;
    }

    public LinkFragment<T> next() {
        return next;
    }

    public void insertAfter(T t) {
        LinkFragment<T> node = new LinkFragment<>(this, next, t);
        if (next != null) {
            next.prev = node;
        }
        next = node;
    }

    public void insertBefore(T t) {
        LinkFragment<T> node = new LinkFragment<>(prev, this, t);
        if (prev != null) {
            prev.next = node;
        }
        prev = node;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkFragmentIterator<>(this);
    }

    static class LinkFragmentIterator<T> implements Iterator<T> {

        LinkFragment<T> current;
        LinkFragment<T> stop;

        LinkFragmentIterator(LinkFragment<T> start) {
            current = start;
        }

        @Override
        public boolean hasNext() {
            return current != null && current != stop;
        }

        @Override
        public T next() {
            if (stop == null) {
                stop = current;
            }
            T t = current.item;
            current = current.next;
            return t;
        }
    }
}
