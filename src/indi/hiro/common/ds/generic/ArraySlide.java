package indi.hiro.common.ds.generic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Hiro on 2018/10/31.
 */
public abstract class ArraySlide<T> implements Iterable<T>, ReadArray<T>, java.io.Serializable {

    static final int DEFAULT_CAPACITY = 8;

    final Class<T> classT;

    transient int p1, p2;

    transient T[] array;

    transient ArraySlideIterator arraySlideIterator;

    boolean serializeContent = true;

    ArraySlide(Class<T> classT, int initialCapacity) {
        this.classT = classT;
        array = constructArray(initialCapacity);
    }

    ArraySlide(Class<T> classT) {
        this(classT, DEFAULT_CAPACITY);
    }

    public Class<T> contentClass() {
        return classT;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.defaultWriteObject();
        if (serializeContent) {
            s.writeInt(p2 - p1);
            for (int i = p1; i < p2; i++) {
                s.writeObject(array[i]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (serializeContent) {
            int size = Math.max(0, s.readInt());
            array = constructArray(size);
            for (int i = 0; i < size; i++) {
                array[i] = (T) s.readObject();
            }
            p1 = 0;
            p2 = size;
        } else {
            p1 = 0;
            p2 = 0;
            array = constructArray(DEFAULT_CAPACITY);
        }
    }

    public void setSerializeContent(boolean serializeContent) {
        this.serializeContent = serializeContent;
    }

    protected void rangeCheck(int index) {
        if (index < 0 || index >= p2 - p1) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    protected void slotRangeCheck(int index) {
        if (index < 0 || index > p2 - p1) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        return p2 - p1;
    }

    public boolean notEmpty() {
        return p2 > p1;
    }

    @SuppressWarnings("unchecked")
    public final T[] constructArray(int size) {
        return (T[]) java.lang.reflect.Array.newInstance(classT, size);
    }

    public int firstIndexOf(T t, int start) {
        rangeCheck(start);
        for (int i = p1 + start, lim = p2; i < lim; i++) {
            if (array[i] == t) {
                return i;
            }
        }
        return -1;
    }

    public int firstIndexOf(T t) {
        return firstIndexOf(t, 0);
    }

    public int lastIndexOf(T t, int start) {
        rangeCheck(start);
        for (int i = p1 + start, lim = p1; i >= lim; i--) {
            if (array[i] == t) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(T t) {
        return lastIndexOf(t, p1 + p2 - 1);
    }

    @Override
    public T get(int index) {
        rangeCheck(index);
        return array[index];
    }

    public T[] toArray() {
        int size = p2 - p1;
        if (size >= 0) {
            T[] replicaArray = constructArray(size);
            System.arraycopy(array, p1, replicaArray, 0, size);
            return replicaArray;
        } else {
            return null;
        }
    }

    public void quickClear() {
        p1 = 0;
        p2 = 0;
    }

    public void deepClear() {
        quickClear();
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }

    public void sort(Comparator<T> comparator) {
        int size = p2 - p1;
        if (size > 1) {
            Arrays.sort(array, p1, p2, comparator);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = p1; i < p2; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(array[i]);
        }
        return sb.append("]").toString();
    }

    @Override
    public Iterator<T> iterator() {
        if (arraySlideIterator == null || arraySlideIterator.hasNext()) {
            return (arraySlideIterator = new ArraySlideIterator());
        } else {
            arraySlideIterator.reset();
            return arraySlideIterator;
        }
    }

    class ArraySlideIterator implements Iterator<T> {

        int p3 = p1;

        boolean onGoing() {
            return p3 <= p2;
        }

        void reset() {
            p3 = p1;
        }

        @Override
        public boolean hasNext() {
            if (p3 < p2) {
                return true;
            } else {
                p3++;
                return false;
            }
        }

        @Override
        public T next() {
            return array[p3++];
        }
    }
}
