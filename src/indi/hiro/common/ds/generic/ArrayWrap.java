package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.DataContainer;
import indi.hiro.common.ds.util.DataStructureException;
import indi.hiro.common.ds.util.DsToStringUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public abstract class ArrayWrap<T> implements Iterable<T>, ExtendedReadArray<T>, DataContainer, java.io.Serializable {

    private static final long serialVersionUID = 0x1411012219FBF4B8L;

    static final int DEFAULT_CAPACITY = 4;

    public static<T> void arrayCopy(ArrayWrap<T> src, ArrayWrap<T> dst) {
        if (src.classT != dst.classT) {
            throw new DataStructureException();
        }
        dst.ensureCapacity(src.size);
        System.arraycopy(src.array, 0, dst.array, 0, src.size);
    }

    public static<T> void arrayCopy(ArrayWrap<T> src, ArrayWrap<T> dst, Replicator<T> replicator) {
        if (src.classT != dst.classT) {
            throw new DataStructureException();
        }
        int size = src.size;
        dst.ensureCapacity(size);
        for (int i = 0; i < size; i++) {
            dst.array[i] = replicator.copy(src.array[i]);
        }
        dst.size = size;
    }

    final Class<T> classT;

    int size = 0;

    boolean serializeContent = true;

    transient T[] array;

    transient ArrayWrapIterator arrayWrapIterator;

    ArrayWrap(Class<T> classT, int initialCapacity) {
        this.classT = classT;
        array = constructArray(initialCapacity);
    }

    ArrayWrap(Class<T> classT) {
        this(classT, DEFAULT_CAPACITY);
    }

    ArrayWrap(Class<T> classT, T[] array) {
        this.classT = classT;
        this.array = array;
    }

    public Class<T> contentClass() {
        return classT;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.defaultWriteObject();
        if (serializeContent) {
            for (int i = 0; i < size; i++) {
                s.writeObject(array[i]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (serializeContent) {
            array = constructArray(size);
            for (int i = 0; i < size; i++) {
                array[i] = (T) s.readObject();
            }
        } else {
            size = 0;
            array = constructArray(DEFAULT_CAPACITY);
        }
    }

    public void setSerializeContent(boolean serializeContent) {
        this.serializeContent = serializeContent;
    }

    protected void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    protected void slotRangeCheck(int index) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        rangeCheck(index);
        return array[index];
    }

    public boolean notEmpty() {
        return size > 0;
    }

    @SuppressWarnings("unchecked")
    public final T[] constructArray(int size) {
        return (T[]) java.lang.reflect.Array.newInstance(classT, size);
    }

    public void trim() {
        int size = this.size;
        if (size < array.length) {
            T[] newArray = constructArray(size);
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    @SuppressWarnings("unchecked")
    public void ensureCapacity(int minCapacity) {
        int len = array.length;
        if (len < minCapacity) {
            len = len + len / 2;
            T[] newArray = constructArray(Math.max(minCapacity, len));
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    @Override
    public boolean contains(T t) {
        for (int i = size - 1; i >= 0; i--) {
            if (array[i] == t) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int firstIndexOf(T t, int start) {
        for (int i = start, size = this.size; i < size; i++) {
            if (array[i] == t) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstIndexOf(T t) {
        return firstIndexOf(t, 0);
    }

    @Override
    public int lastIndexOf(T t, int start) {
        for (int i = start; i >= 0; i--) {
            if (array[i] == t) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(T t) {
        return lastIndexOf(t, size - 1);
    }

    public T[] toArray() {
        int size = this.size;
        T[] replicaArray = constructArray(size);
        System.arraycopy(array, 0, replicaArray, 0, size);
        return replicaArray;
    }

    public void quickClear() {
        size = 0;
    }

    public void deepClear() {
        size = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }

    public void sort(Comparator<T> comparator) {
        Arrays.sort(array, 0, size, comparator);
    }

    @Override
    public Iterator<T> iterator() {
        if (arrayWrapIterator == null || arrayWrapIterator.onGoing()) {
            return (arrayWrapIterator = new ArrayWrapIterator());
        } else {
            arrayWrapIterator.reset();
            return arrayWrapIterator;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(DsToStringUtil.nameFor(getClass()));
        sb.append(DsToStringUtil.GENERIC_LEFT_BRACKET);
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(DsToStringUtil.GENERIC_ELEMENT_SEP);
            }
            sb.append(array[i]);
        }
        return sb.append(DsToStringUtil.GENERIC_RIGHT_BRACKET).toString();
    }

    class ArrayWrapIterator implements Iterator<T> {

        int ptr = 0;

        boolean onGoing() {
            return ptr <= size;
        }

        void reset() {
            ptr = 0;
        }

        @Override
        public boolean hasNext() {
            if (ptr < size) {
                return true;
            } else {
                ptr++;
                return false;
            }
        }

        @Override
        public T next() {
            return array[ptr++];
        }
    }
}
