package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.EmptyStructureException;

import java.util.Iterator;

public class ArrayCyclic<T> extends ArrayFinal<T> {

    final int size;

    int start = 0, end = 0;

    ArrayCyclic(Class<T> classT, T[] array) {
        super(classT, array);
        this.size = array.length;
    }

    public T removeFirst() {
        if (start == end) {
            throw new EmptyStructureException();
        }
        T removed = array[start];
        array[start] = null;
        if (++start == size) {
            start = 0;
        }
        return removed;
    }

    public void addLast(T t) {
        array[end++] = t;
        if (end == size) {
            end = 0;
        }
        if (start == end && ++start == size) {
            start = 0;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayCyclicIterator();
    }

    class ArrayCyclicIterator implements Iterator<T> {

        final int end = ArrayCyclic.this.end;

        int ptr = ArrayCyclic.this.start;

        @Override
        public boolean hasNext() {
            return ptr != end;
        }

        @Override
        public T next() {
            T t = array[ptr++];
            if (ptr == size) {
                ptr = 0;
            }
            return t;
        }
    }
}
