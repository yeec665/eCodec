package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.EmptyStructureException;

import java.util.Arrays;

/**
 * Created by Hiro on 2018/10/31.
 */
public class BidirectionalArray<T> extends ArraySlide<T> {


    public BidirectionalArray(Class<T> classT, int initialCapacity) {
        super(classT, initialCapacity);
    }

    public BidirectionalArray(Class<T> classT) {
        super(classT);
    }

    private BidirectionalArray(Class<T> classT, T[] array) {
        super(classT);
        this.array = array;
        this.p1 = 0;
        this.p2 = array.length;
    }

    public BidirectionalArray<T> copy() {
        return new BidirectionalArray<T>(classT, Arrays.copyOfRange(array, p1, p2));
    }

    public BidirectionalArray<T> copy(Replicator<T> replicator) {
        T[] replicaArray = constructArray(p2 - p1);
        for (int i = p1, j = 0; i < p2; i++) {
            replicaArray[j++] = replicator.copy(array[i]);
        }
        return new BidirectionalArray<T>(classT, replicaArray);
    }

    private void enlargeL(int n) {
        int mov = array.length - p2 + p1 - n;
        if (mov > 0) {
            mov = array.length - (mov >>> 1) - p2;
            System.arraycopy(array, p1, array, mov, p2 - p1);
        } else {
            T[] newArray = constructArray(array.length * 2 + n);
            mov = array.length + n - ((p1 + p2) >>> 1);
            System.arraycopy(array, p1, newArray, mov, p2 - p1);
            array = newArray;
        }
        p1 += mov;
        p2 += mov;
    }

    public void addFirst(T t) {
        if (p1 <= 0) {
            int mov = array.length - p2;
            if (mov > 0) {
                mov = (mov + 1) >>> 1;
                System.arraycopy(array, p1, array, mov, p2 - p1);
            } else {
                T[] newArray = constructArray(array.length * 2 + 1);
                mov = (array.length + 2) >>> 1;
                System.arraycopy(array, p1, newArray, mov, p2 - p1);
                array = newArray;
            }
            p1 += mov;
            p2 += mov;
        }
        array[--p1] = t;
    }

    public void addLast(T t) {
        if (p2 >= array.length) {
            if (p1 > 0) {
                int mov = (p1 + 1) >>> 1;
                System.arraycopy(array, p1, array, p1 - mov, p2 - p1);
                p1 -= mov;
                p2 -= mov;
            } else {
                T[] newArray = constructArray(array.length * 2 + 1);
                int mov = (array.length) >>> 1;
                System.arraycopy(array, p1, newArray, mov, p2 - p1);
                array = newArray;
                p1 += mov;
                p2 += mov;
            }
        }
        array[p2++] = t;
    }

    public void addFirst(ReadArray<T> ra) {
        if (ra instanceof ArrayWrap) {
            addFirst((ArrayWrap<T>) ra);
        } else if (ra instanceof ArraySlide) {
            addFirst((ArraySlide<T>) ra);
        } else {
            int size = ra.size();
            if (size <= 0) {
                return;
            }
            if (p1 < size) {
                enlargeL(size);
            }
            for (int i = size - 1; i >= 0; i--) {
                array[--p1] = ra.get(i);
            }
        }
    }

    public void addFirst(ArrayWrap<T> aw) {
        int awSize = aw.size;
        if (awSize <= 0) {
            return;
        }
        if (p1 < awSize) {
            enlargeL(awSize);
        }
        p1 -= awSize;
        System.arraycopy(aw.array, 0, array, p1, awSize);
    }

    public void addFirst(ArraySlide<T> as) {
        int asSize = as.p2 - as.p1;
        if (asSize <= 0) {
            return;
        }
        if (p1 < asSize) {
            enlargeL(asSize);
        }
        p1 -= asSize;
        System.arraycopy(as.array, as.p1, array, p1, asSize);
    }

    public T removeFirst() {
        if (p1 >= p2) {
            throw new EmptyStructureException();
        }
        T removed = array[p1];
        array[p1++] = null;
        return removed;
    }

    public T removeLast() {
        if (p1 >= p2) {
            throw new EmptyStructureException();
        }
        T removed = array[--p2];
        array[p2] = null;
        return removed;
    }

    @Override
    public void quickClear() {
        int p = array.length / 2;
        p1 = p;
        p2 = p;
    }
}
