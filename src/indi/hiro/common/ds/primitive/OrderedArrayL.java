package indi.hiro.common.ds.primitive;

import java.util.Arrays;

public class OrderedArrayL {

    private long[] array;

    private int size;

    public OrderedArrayL(int initialCapacity) {
        initialCapacity = Math.max(4, initialCapacity);
        array = new long[initialCapacity];
    }

    public OrderedArrayL() {
        this(4);
    }

    public OrderedArrayL(long[] array, int len) {
        Arrays.sort(array, 0, len);
        this.array = array;
        this.size = len;
    }

    public int size() {
        return size;
    }

    public int firstIndexLargerThan(long v) {
        // TODO : use binary search
        for (int i = 0; i < size; i++) {
            if (array[i] > v) {
                return i;
            }
        }
        return size;
    }

    public int firstIndexLargerOrEqual(long v) {
        // TODO : use binary search
        for (int i = 0; i < size; i++) {
            if (array[i] >= v) {
                return i;
            }
        }
        return size;
    }

    public int lastIndexSmallerThan(long v) {
        // TODO : use binary search
        for (int i = size - 1; i >= 0; i--) {
            if (array[i] < v) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexSmallerOrEqual(long v) {
        // TODO : use binary search
        for (int i = size - 1; i >= 0; i--) {
            if (array[i] <= v) {
                return i;
            }
        }
        return -1;
    }

    public void insert(long v) {
        if (size + 1 >= array.length) {
            array = Arrays.copyOf(array, Math.max(size + 1, array.length * 2));
        }
        int ptr = firstIndexLargerOrEqual(v);
        System.arraycopy(array, ptr, array, ptr + 1, size - ptr);
        array[ptr] = v;
        size++;
    }

    public void clear() {
        size = 0;
    }

    public long get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }
}
