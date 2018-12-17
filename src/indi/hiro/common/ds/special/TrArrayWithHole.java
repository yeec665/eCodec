package indi.hiro.common.ds.special;

import indi.hiro.common.ds.util.DataStructureException;

import java.util.List;

public class TrArrayWithHole implements Runnable, java.io.Serializable {

    private static final int DEFAULT_CAPACITY = 18;

    private transient TracedRunnable[] array;
    private transient int size;
    private int nnSize;

    public TrArrayWithHole() {
        array = new TracedRunnable[DEFAULT_CAPACITY];
    }

    private void ensureCapacity(int minCapacity) {
        int len = array.length;
        if (len < minCapacity) {
            len = len + len / 2;
            TracedRunnable[] newArray = new TracedRunnable[Math.max(minCapacity, len)];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.defaultWriteObject();
        int nn = 0;
        for (int i = 0; i < size; i++) {
            if (array[i] != null) {
                s.writeObject(array[i]);
                nn++;
            }
        }
        if (nn != nnSize) {
            throw new DataStructureException();
        }
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        size = nnSize;
        array = new TracedRunnable[Math.max(nnSize, DEFAULT_CAPACITY)];
        for (int i = 0; i < nnSize; i++) {
            array[i] = (TracedRunnable) s.readObject();
        }
    }

    public void add(TracedRunnable tr) {
        ensureCapacity(size + 1);
        if (tr != null) {
            nnSize++;
        }
        array[size++] = tr;
    }

    public boolean remove(TracedRunnable tr) {
        if (tr == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (array[i] == tr) {
                array[i] = null;
                nnSize--;
                return true;
            }
        }
        return false;
    }

    public void addAll(List<TracedRunnable> trList) {
        ensureCapacity(trList.size());
        for (TracedRunnable tr : trList) {
            if (tr != null) {
                nnSize++;
            }
            array[size++] = tr;
        }
    }

    public void removeAll(List<TracedRunnable> trList) {
        for (TracedRunnable tr : trList) {
            if (tr == null) {
                continue;
            }
            for (int i = 0; i < size; i++) {
                if (array[i] == tr) {
                    array[i] = null;
                    nnSize--;
                    break;
                }
            }
        }
    }

    public void clear() {
        size = 0;
        nnSize = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }

    public void maintain() {
        if (nnSize < size) {
            int ptr = 0;
            for (int i = 0; i < size; i++) {
                if (array[i] != null) {
                    array[ptr++] = array[i];
                }
            }
            for (int i = ptr; i < size; i++) {
                array[i] = null;
            }
            size = ptr;
            if (nnSize != size) {
                throw new DataStructureException();
            }
        }
    }

    @Override
    public void run() {
        int index = 0;
        while (true) {
            try {
                while (index < size) {
                    TracedRunnable tr = array[index++];
                    if (tr != null) {
                        tr.run();
                    }
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
