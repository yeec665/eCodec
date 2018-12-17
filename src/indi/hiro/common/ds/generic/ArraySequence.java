package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.EmptyStructureException;

import java.util.Arrays;
import java.util.function.Predicate;

public class ArraySequence<T> extends ArrayWrap<T> {

    private static final long serialVersionUID = 0x74BD2D8A448DB9DBL;

    public ArraySequence(Class<T> classT, int initialCapacity) {
        super(classT, initialCapacity);
    }

    public ArraySequence(Class<T> classT) {
        super(classT);
    }

    private ArraySequence(Class<T> classT, T[] array) {
        super(classT);
        this.array = array;
        this.size = array.length;
    }

    public ArraySequence<T> copy() {
        return new ArraySequence<>(classT, Arrays.copyOf(array, size));
    }

    public ArraySequence<T> copy(Replicator<T> replicator) {
        int size = this.size;
        T[] replicaArray = constructArray(size);
        for (int i = 0; i < size; i++) {
            replicaArray[i] = replicator.copy(array[i]);
        }
        return new ArraySequence<>(classT, replicaArray);
    }

    public void setObject(Object o, int index) {
        rangeCheck(index);
        array[index] = classT.cast(o);
    }

    public T set(T t, int index) {
        rangeCheck(index);
        T oldValue = array[index];
        array[index] = t;
        return oldValue;
    }

    public void addFirst(T t) {
        ensureCapacity(size + 1);
        System.arraycopy(array, 0, array, 1, size);
        array[0] = t;
        size++;
    }

    public void addLast(T t) {
        ensureCapacity(size + 1);
        array[size++] = t;
    }

    public void insert(T t, int beforeIndex) {
        slotRangeCheck(beforeIndex);
        ensureCapacity(size + 1);
        System.arraycopy(array, beforeIndex, array, beforeIndex + 1, size - beforeIndex);
        array[beforeIndex] = t;
        size++;
    }

    public T removeFirst() {
        if (size <= 0) {
            throw new EmptyStructureException();
        }
        T removed = array[0];
        size--;
        System.arraycopy(array, 1, array, 0, size);
        array[size] = null;
        return removed;
    }

    public T removeLast() {
        if (size <= 0) {
            throw new EmptyStructureException();
        }
        T removed = array[--size];
        array[size] = null;
        return removed;
    }

    public T remove(int index) {
        rangeCheck(index);
        T removed = array[index];
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        array[--size] = null;
        return removed;
    }

    public int remove(Predicate<T> eliminator) {
        int ptr = 0, count = 0;
        for (int i = 0, size = this.size; i < size; i++) {
            if (eliminator.test(array[i])) {
                count++;
            } else {
                array[ptr++] = array[i];
            }
        }
        size = ptr;
        return count;
    }

    public void clear() {
        for (int i = 0, size = this.size; i < size; i++) {
            array[i] = null;
        }
        this.size = 0;
    }

    public void swap(int i1, int i2) {
        rangeCheck(i1);
        rangeCheck(i2);
        T temp = array[i1];
        array[i1] = array[i2];
        array[i2] = temp;
    }
}
