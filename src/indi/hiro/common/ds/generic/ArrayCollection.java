package indi.hiro.common.ds.generic;

import java.util.Random;
import java.util.function.Predicate;

public class ArrayCollection<T> extends ArrayWrap<T> {

    public ArrayCollection(Class<T> classT, int initialCapacity) {
        super(classT, initialCapacity);
    }

    public ArrayCollection(Class<T> classT) {
        super(classT);
    }

    public void add(T t) {
        ensureCapacity(size + 1);
        array[size++] = t;
    }

    public boolean remove(Object object) {
        for (int i = 0; i < size; i++) {
            if (array[i] == object) {
                size--;
                array[i] = array[size];
                array[size] = null;
                return true;
            }
        }
        return false;
    }

    public void remove(int index) {
        rangeCheck(index);
        size--;
        array[index] = array[size];
        array[size] = null;
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

    private void innerSwap(int i1, int i2) {
        T temp = array[i1];
        array[i1] = array[i2];
        array[i2] = temp;
    }

    public void shuffle(Random random) {
        int size = this.size;
        if (size <= 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            innerSwap(i, random.nextInt(size));
        }
    }
}
