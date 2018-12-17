package indi.hiro.common.ds.primitive;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import indi.hiro.common.ds.util.DataContainer;
import indi.hiro.common.ds.util.DsToStringUtil;
import indi.hiro.common.ds.util.EmptyStructureException;
import indi.hiro.common.ds.generic.ExtendedReadArray;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Created by Hiro on 2018/11/3.
 */
public class IntegerArray implements ExtendedReadArray<Integer>, DataContainer.PrimitiveContainer, java.io.Serializable {

    private static final int DEFAULT_CAPACITY = 10;

    transient int[] ii;

    int size;

    public IntegerArray(int initialCapacity) {
        ii = new int[initialCapacity];
    }

    public IntegerArray() {
        ii = new int[DEFAULT_CAPACITY];
    }

    IntegerArray(int[] ii) {
        Objects.requireNonNull(ii);
        this.ii = ii;
        this.size = ii.length;
    }

    IntegerArray(IntegerArray that) {
        this.ii = Arrays.copyOf(that.ii, that.size);
        this.size = that.size;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.defaultWriteObject();
        for (int i = 0; i < size; i++) {
            s.writeInt(ii[i]);
        }
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        ii = new int[size];
        for (int i = 0; i < size; i++) {
            ii[i] = s.readInt();
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
    public Integer get(int index) {
        rangeCheck(index);
        return ii[index];
    }

    public int getInt(int index) {
        rangeCheck(index);
        return ii[index];
    }

    public boolean notEmpty() {
        return size > 0;
    }

    public void trim() {
        if (size < ii.length) {
            ii = Arrays.copyOf(ii, size);
        }
    }

    public void ensureCapacity(int minCapacity) {
        int len = ii.length;
        if (len < minCapacity) {
            ii = Arrays.copyOf(ii, Math.max(minCapacity, len << 1));
        }
    }

    @Override
    public boolean contains(Integer t) {
        int it = t;
        for (int i = size - 1; i >= 0; i--) {
            if (ii[i] == it) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int firstIndexOf(Integer t, int start) {
        for (int i = start, size = this.size; i < size; i++) {
            if (ii[i] == t) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstIndexOf(Integer t) {
        return firstIndexOf(t, 0);
    }

    @Override
    public int lastIndexOf(Integer t, int start) {
        for (int i = start; i >= 0; i--) {
            if (ii[i] == t) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Integer t) {
        return lastIndexOf(t, size - 1);
    }

    public void forEach(@NotNull IntConsumer consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(ii[i]);
        }
    }

    public void forEach(int off, int len, @NotNull IntConsumer consumer) {
        rangeCheck(off, len);
        for (int i = off, lim = off + len; i < lim; i++) {
            consumer.accept(ii[i]);
        }
    }

    public void copyInto(IntegerArray ia) {
        ia.addLast(ii, 0, size);
    }

    public void copyInto(int off, int len, IntegerArray ia) {
        ia.addLast(ii, off, len);
    }

    public int[] toIntArray() {
        return Arrays.copyOf(ii, size);
    }

    public IntegerArray copy() {
        return new IntegerArray(this);
    }

    public int setInt(int t, int index) {
        rangeCheck(index);
        int oldValue = ii[index];
        ii[index] = t;
        return oldValue;
    }

    public void addFirst(int t) {
        ensureCapacity(size + 1);
        System.arraycopy(ii, 0, ii, 1, size++);
        ii[0] = t;
    }

    public void addLast(int t) {
        ensureCapacity(size + 1);
        ii[size++] = t;
    }

    public void insert(int t, int beforeIndex) {
        slotRangeCheck(beforeIndex);
        ensureCapacity(size + 1);
        System.arraycopy(ii, beforeIndex, ii, beforeIndex + 1, size - beforeIndex);
        ii[beforeIndex] = t;
        size++;
    }

    public boolean swap(int i1, int i2) {
        if (i1 == i2) {
            return false;
        }
        rangeCheck(i1);
        rangeCheck(i2);
        if (ii[i1] != ii[i2]) {
            int temp = ii[i1];
            ii[i1] = ii[i2];
            ii[i2] = temp;
            return true;
        } else {
            return false;
        }
    }

    public void addFirst(int[] tt, int off, int len) {
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(ii, 0, ii, len, size);
            size += len;
            System.arraycopy(tt, off, ii, 0, len);
        }
    }

    public void addLast(int[] tt, int off, int len) {
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(tt, off, ii, size, len);
            size += len;
        }
    }

    public void insert(int[] tt, int off, int len, int beforeIndex) {
        slotRangeCheck(beforeIndex);
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(ii, beforeIndex, ii, beforeIndex + len, size - beforeIndex);
            size += len;
            System.arraycopy(tt, off, ii, beforeIndex, len);
        }
    }

    public int removeFirst() {
        if (size <= 0) {
            throw new EmptyStructureException();
        }
        int removed = ii[0];
        size--;
        System.arraycopy(ii, 1, ii, 0, size);
        return removed;
    }

    public int removeLast() {
        if (size <= 0) {
            throw new EmptyStructureException();
        }
        return ii[--size];
    }

    public int remove(int index) {
        rangeCheck(index);
        int removed = ii[index];
        System.arraycopy(ii, index + 1, ii, index, --size - index);
        return removed;
    }

    public int remove(@NotNull IntPredicate eliminator) {
        int ptr = 0, count = 0;
        for (int i = 0, size = this.size; i < size; i++) {
            if (eliminator.test(ii[i])) {
                count++;
            } else {
                ii[ptr++] = ii[i];
            }
        }
        size = ptr;
        return count;
    }

    public void removeInto(int off, int len, @Nullable IntegerArray ia) {
        rangeCheck(off, len);
        if (len > 0) {
            if (ia != null) {
                ia.addLast(ii, off, len);
            }
            int end = off + len;
            System.arraycopy(ii, end, ii, off, size - end);
            size -= len;
        }
    }

    public void removeInto(@NotNull IntPredicate eliminator, @Nullable IntegerArray ia) {
        if (ia == null) {
            remove(eliminator);
        } else {
            int ptr = 0;
            for (int i = 0, size = this.size; i < size; i++) {
                int t = ii[i];
                if (eliminator.test(t)) {
                    ia.addLast(t);
                } else {
                    ii[ptr++] = t;
                }
            }
            size = ptr;
        }
    }

    public void clear() {
        size = 0;
    }

    public void sort() {
        Arrays.sort(ii, 0, size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntegerArray)) {
            return false;
        }
        IntegerArray that = (IntegerArray) obj;
        if (this.size != that.size) {
            return false;
        }
        for (int i = 0, size = this.size; i < size; i++) {
            if (this.ii[i] != that.ii[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return DsToStringUtil.toString(DsToStringUtil.nameFor(getClass()), ii, size);
    }

    public static class Hash extends IntegerArray {

        private static final int K_A = 0x41101F2D;
        private static final int K_B = 0x00007BB1;

        private int y = K_A;

        public Hash() {
            super();
        }

        public Hash(int initialCapacity) {
            super(initialCapacity);
        }

        Hash(Hash that) {
            super(that);
            this.y = that.y;
        }

        private void updateHash() {
            int y = K_A;
            for (int i = 0, size = this.size; i < size; i++) {
                y = y * K_B + ii[i];
            }
            this.y = y;
        }

        @Override
        public Hash copy() {
            return new Hash(this);
        }

        @Override
        public int setInt(int t, int index) {
            int r = super.setInt(t, index);
            if (r != t) {
                updateHash();
            }
            return r;
        }

        @Override
        public void addFirst(int t) {
            super.addFirst(t);
            updateHash();
        }

        @Override
        public void addLast(int t) {
            super.addLast(t);
            y = y * K_B + t;
        }

        @Override
        public void insert(int t, int beforeIndex) {
            super.insert(t, beforeIndex);
            updateHash();
        }

        @Override
        public boolean swap(int i1, int i2) {
            if (super.swap(i1, i2)) {
                updateHash();
                return true;
            }
            return false;
        }

        @Override
        public void addFirst(int[] tt, int off, int len) {
            super.addFirst(tt, off, len);
            if (len > 0) {
                updateHash();
            }
        }

        @Override
        public void addLast(int[] tt, int off, int len) {
            super.addLast(tt, off, len);
            int y = this.y;
            for (len += off; off < len; off++) {
                y = y * K_B + tt[off];
            }
            this.y = y;
        }

        @Override
        public void insert(int[] tt, int off, int len, int beforeIndex) {
            super.insert(tt, off, len, beforeIndex);
            if (len > 0) {
                updateHash();
            }
        }

        @Override
        public int removeFirst() {
            int r = super.removeFirst();
            updateHash();
            return r;
        }

        @Override
        public int removeLast() {
            int r = super.removeLast();
            updateHash();
            return r;
        }

        @Override
        public int remove(int index) {
            int r = super.remove(index);
            updateHash();
            return r;
        }

        @Override
        public int remove(IntPredicate eliminator) {
            int c = super.remove(eliminator);
            if (c > 0) {
                updateHash();
            }
            return c;
        }

        @Override
        public void removeInto(int off, int len, IntegerArray ia) {
            super.removeInto(off, len, ia);
            if (len > 0) {
                updateHash();
            }
        }

        @Override
        public void removeInto(IntPredicate eliminator, IntegerArray ia) {
            super.removeInto(eliminator, ia);
            updateHash();
        }

        @Override
        public void clear() {
            super.clear();
            y = K_A;
        }

        @Override
        public void sort() {
            super.sort();
            updateHash();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Hash) {
                Hash that = (Hash) obj;
                if (this.y != that.y || this.size != that.size) {
                    return false;
                }
                for (int i = 0, size = this.size; i < size; i++) {
                    if (this.ii[i] != that.ii[i]) {
                        return false;
                    }
                }
                return true;
            } else {
                return super.equals(obj);
            }
        }

        @Override
        public int hashCode() {
            return y;
        }
    }
}
