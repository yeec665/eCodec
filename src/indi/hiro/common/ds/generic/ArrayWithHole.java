package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.DataStructureException;
import indi.hiro.common.lang.ReferenceArray;

import java.util.Arrays;

public class ArrayWithHole<T> extends ArrayWrap<T> {

    private static final int DEFAULT_CAPACITY = 20;

    protected int nnSize = 0;

    public ArrayWithHole(Class<T> classT, int initialCapacity) {
        super(classT);
        array = constructArray(Math.max(1, initialCapacity));
    }

    public ArrayWithHole(Class<T> classT) {
        this(classT, DEFAULT_CAPACITY);
    }

    private ArrayWithHole(Class<T> classT, T[] array) {
        super(classT);
        this.array = array;
        this.size = array.length;
        nnSize = ReferenceArray.countNonNull(array);
    }

    public ArrayWithHole<T> copy() {
        return new ArrayWithHole<>(classT, Arrays.copyOf(array, size));
    }

    public ArrayWithHole<T> copy(Replicator<T> replicator) {
        int size = this.size;
        T[] replicaArray = constructArray(size);
        for (int i = 0; i < size; i++) {
            if (array[i] == null) {
                continue;
            }
            replicaArray[i] = replicator.copy(array[i]);
        }
        return new ArrayWithHole<>(classT, replicaArray);
    }

    public T set(T t, int index) {
        rangeCheck(index);
        T oldValue = array[index];
        if (oldValue != null) {
            nnSize--;
        }
        array[index] = t;
        if (t != null) {
            nnSize++;
        }
        return oldValue;
    }

    public void add(T t) {
        ensureCapacity(size + 1);
        if (t != null) {
            nnSize++;
        }
        array[size++] = t;
    }

    public T remove(int index) {
        rangeCheck(index);
        T removed = array[index];
        if (removed != null) {
            nnSize--;
        }
        array[index] = null;
        return removed;
    }

    public int nonNullSize() {
        return nnSize;
    }

    public int holeCount() {
        return size - nnSize;
    }

    public void compact() {
        if (size > nnSize) {
            int ptr = 0;
            for (int i = 0; i < size; i++) {
                if (array[i] != null) {
                    array[ptr++] = array[i];
                }
            }
            size = ptr;
            if (nnSize != ptr) {
                throw new DataStructureException();
            }
        } else if (size < nnSize) {
            throw new DataStructureException();
        }
    }

    public void swap(int i1, int i2) {
        rangeCheck(i1);
        rangeCheck(i2);
        T temp = array[i1];
        array[i1] = array[i2];
        array[i2] = temp;
    }

    @Override
    public void quickClear() {
        super.quickClear();
        nnSize = 0;
    }

    @Override
    public void deepClear() {
        super.deepClear();
        nnSize = 0;
    }
}
