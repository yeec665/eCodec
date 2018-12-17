package indi.hiro.common.ds.primitive;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import indi.hiro.common.ds.util.DataContainer;
import indi.hiro.common.ds.util.EmptyStructureException;
import indi.hiro.common.ds.generic.ExtendedReadArray;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Hiro on 2018/11/3.
 */
public class FloatArray implements IndexedFloat, DataContainer.PrimitiveContainer, java.io.Serializable {

    private static final int DEFAULT_CAPACITY = 6;

    transient float[] ff;

    int size;

    public FloatArray(int initialCapacity) {
        ff = new float[initialCapacity];
    }

    public FloatArray() {
        ff = new float[DEFAULT_CAPACITY];
    }

    FloatArray(float[] ff) {
        Objects.requireNonNull(ff);
        this.ff = ff;
        this.size = ff.length;
    }

    FloatArray(FloatArray that) {
        this.ff = Arrays.copyOf(that.ff, that.size);
        this.size = that.size;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.defaultWriteObject();
        for (int i = 0; i < size; i++) {
            s.writeFloat(ff[i]);
        }
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        ff = new float[size];
        for (int i = 0; i < size; i++) {
            ff[i] = s.readFloat();
        }
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private void rangeCheck(int off, int len) {
        if (off < 0 || len < 0 || off + len > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private void slotRangeCheck(int index) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public float getFloat(int i) {
        rangeCheck(i);
        return ff[i];
    }

    public boolean notEmpty() {
        return size > 0;
    }

    public void trim() {
        if (size < ff.length) {
            ff = Arrays.copyOf(ff, size);
        }
    }

    public void ensureCapacity(int minCapacity) {
        int len = ff.length;
        if (len < minCapacity) {
            ff = Arrays.copyOf(ff, Math.max(minCapacity, len << 1));
        }
    }

    public boolean contains(float t) {
        for (int i = size - 1; i >= 0; i--) {
            if (ff[i] == t) {
                return true;
            }
        }
        return false;
    }

    public int firstIndexOf(float t, int start) {
        for (int i = start, size = this.size; i < size; i++) {
            if (ff[i] == t) {
                return i;
            }
        }
        return -1;
    }

    public int firstIndexOf(float t) {
        return firstIndexOf(t, 0);
    }

    public int lastIndexOf(float t, int start) {
        for (int i = start; i >= 0; i--) {
            if (ff[i] == t) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(float t) {
        return lastIndexOf(t, size - 1);
    }

    public void forEach(@NotNull FloatConsumer consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(ff[i]);
        }
    }

    public void forEach(int off, int len, @NotNull FloatConsumer consumer) {
        rangeCheck(off, len);
        for (int i = off, lim = off + len; i < lim; i++) {
            consumer.accept(ff[i]);
        }
    }

    public void copyInto(FloatArray fa) {
        fa.addLast(ff, 0, size);
    }

    public void copyInto(int off, int len, FloatArray fa) {
        fa.addLast(ff, off, len);
    }

    public float[] toFloatArray() {
        return Arrays.copyOf(ff, size);
    }

    public FloatArray copy() {
        return new FloatArray(this);
    }

    @Override
    public float setFloat(float t, int index) {
        rangeCheck(index);
        float oldValue = ff[index];
        ff[index] = t;
        return oldValue;
    }

    public void addFirst(float t) {
        ensureCapacity(size + 1);
        System.arraycopy(ff, 0, ff, 1, size++);
        ff[0] = t;
    }

    public void addLast(float t) {
        ensureCapacity(size + 1);
        ff[size++] = t;
    }

    public void insert(float t, int beforeIndex) {
        slotRangeCheck(beforeIndex);
        ensureCapacity(size + 1);
        System.arraycopy(ff, beforeIndex, ff, beforeIndex + 1, size - beforeIndex);
        ff[beforeIndex] = t;
        size++;
    }

    public boolean swap(int i1, int i2) {
        if (i1 == i2) {
            return false;
        }
        rangeCheck(i1);
        rangeCheck(i2);
        if (ff[i1] != ff[i2]) {
            float temp = ff[i1];
            ff[i1] = ff[i2];
            ff[i2] = temp;
            return true;
        } else {
            return false;
        }
    }

    public void addFirst(float[] tt, int off, int len) {
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(ff, 0, ff, len, size);
            size += len;
            System.arraycopy(tt, off, ff, 0, len);
        }
    }

    public void addLast(float[] tt, int off, int len) {
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(tt, off, ff, size, len);
            size += len;
        }
    }

    public void insert(float[] tt, int off, int len, int beforeIndex) {
        slotRangeCheck(beforeIndex);
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(ff, beforeIndex, ff, beforeIndex + len, size - beforeIndex);
            size += len;
            System.arraycopy(tt, off, ff, beforeIndex, len);
        }
    }

    public float removeFirst() {
        if (size <= 0) {
            throw new EmptyStructureException();
        }
        float removed = ff[0];
        size--;
        System.arraycopy(ff, 1, ff, 0, size);
        return removed;
    }

    public float removeLast() {
        if (size <= 0) {
            throw new EmptyStructureException();
        }
        return ff[--size];
    }

    public float remove(int index) {
        rangeCheck(index);
        float removed = ff[index];
        System.arraycopy(ff, index + 1, ff, index, --size - index);
        return removed;
    }

    public int remove(@NotNull FloatPredicate eliminator) {
        int ptr = 0, count = 0;
        for (int i = 0, size = this.size; i < size; i++) {
            if (eliminator.test(ff[i])) {
                count++;
            } else {
                ff[ptr++] = ff[i];
            }
        }
        size = ptr;
        return count;
    }

    public void removeInto(int off, int len, @Nullable FloatArray fa) {
        rangeCheck(off, len);
        if (len > 0) {
            if (fa != null) {
                fa.addLast(ff, off, len);
            }
            int end = off + len;
            System.arraycopy(ff, end, ff, off, size - end);
            size -= len;
        }
    }

    public void removeInto(@NotNull FloatPredicate eliminator, @Nullable FloatArray fa) {
        if (fa == null) {
            remove(eliminator);
        } else {
            int ptr = 0;
            for (int i = 0, size = this.size; i < size; i++) {
                float t = ff[i];
                if (eliminator.test(t)) {
                    fa.addLast(t);
                } else {
                    ff[ptr++] = t;
                }
            }
            size = ptr;
        }
    }

    public void clear() {
        size = 0;
    }

    public void sort() {
        Arrays.sort(ff, 0, size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FloatArray)) {
            return false;
        }
        FloatArray that = (FloatArray) obj;
        if (this.size != that.size) {
            return false;
        }
        for (int i = 0, size = this.size; i < size; i++) {
            if (this.ff[i] != that.ff[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(ff[i]);
        }
        return sb.append("]").toString();
    }

    public static class Sum extends FloatArray {

        private float y;

        public Sum() {
            super();
        }

        public Sum(int initialCapacity) {
            super(initialCapacity);
        }

        Sum(Sum that) {
            super(that);
            this.y = that.y;
        }

        private void updateSum() {
            int y = 0;
            for (int i = 0, size = this.size; i < size; i++) {
                y += ff[i];
            }
            this.y = y;
        }

        @Override
        public Sum copy() {
            return new Sum(this);
        }

        @Override
        public float setFloat(float t, int index) {
            float r = super.setFloat(t, index);
            y += t - r;
            return r;
        }

        @Override
        public void addFirst(float t) {
            super.addFirst(t);
            y += t;
        }

        @Override
        public void addLast(float t) {
            super.addLast(t);
            y += t;
        }

        @Override
        public void insert(float t, int beforeIndex) {
            super.insert(t, beforeIndex);
            y += t;
        }

        @Override
        public void addFirst(float[] tt, int off, int len) {
            super.addFirst(tt, off, len);
            if (len > 0) {
                updateSum();
            }
        }

        @Override
        public void addLast(float[] tt, int off, int len) {
            super.addLast(tt, off, len);
            if (len > 0) {
                updateSum();
            }
        }

        @Override
        public void insert(float[] tt, int off, int len, int beforeIndex) {
            super.insert(tt, off, len, beforeIndex);
            if (len > 0) {
                updateSum();
            }
        }

        @Override
        public float removeFirst() {
            float r = super.removeFirst();
            y -= r;
            return r;
        }

        @Override
        public float removeLast() {
            float r = super.removeLast();
            y -= r;
            return r;
        }

        @Override
        public float remove(int index) {
            float r = super.remove(index);
            y -= r;
            return r;
        }

        @Override
        public int remove(FloatPredicate eliminator) {
            int c = super.remove(eliminator);
            if (c > 0) {
                updateSum();
            }
            return c;
        }

        @Override
        public void removeInto(int off, int len, FloatArray fa) {
            super.removeInto(off, len, fa);
            if (len > 0) {
                updateSum();
            }
        }

        @Override
        public void removeInto(FloatPredicate eliminator, FloatArray ia) {
            super.removeInto(eliminator, ia);
            updateSum();
        }

        @Override
        public void clear() {
            super.clear();
            y = 0;
        }
    }
}
