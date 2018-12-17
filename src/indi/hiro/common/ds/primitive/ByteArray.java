package indi.hiro.common.ds.primitive;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import indi.hiro.common.ds.util.DataContainer;
import indi.hiro.common.ds.util.DsToStringUtil;
import indi.hiro.common.ds.util.EmptyStructureException;
import indi.hiro.common.ds.generic.ExtendedReadArray;
import indi.hiro.common.lang.ByteArrays;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Hiro on 2018/11/4.
 */
public class ByteArray implements ExtendedReadArray<Byte>, DataContainer.PrimitiveContainer, java.io.Serializable {

    private static final int DEFAULT_CAPACITY = 32;

    transient byte[] bb;

    int size;

    public ByteArray(int initialCapacity) {
        bb = new byte[initialCapacity];
    }

    public ByteArray() {
        bb = new byte[DEFAULT_CAPACITY];
    }

    ByteArray(byte[] bb, int size) {
        if (bb.length < size) {
            throw new IllegalArgumentException();
        }
        this.bb = bb;
        this.size = size;
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.defaultWriteObject();
        for (int i = 0; i < size; i++) {
            s.writeByte(bb[i]);
        }
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        bb = new byte[size];
        for (int i = 0; i < size; i++) {
            bb[i] = s.readByte();
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
    public Byte get(int index) {
        rangeCheck(index);
        return bb[index];
    }

    public byte getByte(int index) {
        rangeCheck(index);
        return bb[index];
    }

    public boolean notEmpty() {
        return size > 0;
    }
    
    public void trim() {
        if (size < bb.length) {
            bb = Arrays.copyOf(bb, size);
        }
    }

    public void ensureCapacity(int minCapacity) {
        int len = bb.length;
        if (len < minCapacity) {
            bb = Arrays.copyOf(bb, Math.max(minCapacity, len << 1));
        }
    }

    @Override
    public boolean contains(Byte t) {
        byte bt = t;
        for (int i = size - 1; i >= 0; i--) {
            if (bb[i] == bt) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int firstIndexOf(Byte t, int start) {
        for (int i = start, size = this.size; i < size; i++) {
            if (bb[i] == t) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstIndexOf(Byte t) {
        return firstIndexOf(t, 0);
    }

    @Override
    public int lastIndexOf(Byte t, int start) {
        for (int i = start; i >= 0; i--) {
            if (bb[i] == t) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Byte t) {
        return lastIndexOf(t, size - 1);
    }
    
    public void forEach(@NotNull ByteConsumer consumer) {
        for (int i = 0; i < size; i++) {
            consumer.accept(bb[i]);
        }
    }

    public void forEach(int off, int len, @NotNull ByteConsumer consumer) {
        rangeCheck(off, len);
        for (int i = off, lim = off + len; i < lim; i++) {
            consumer.accept(bb[i]);
        }
    }
    
    public void copyInto(ByteArray ba) {
        
    }

    public void copyInto(int off, int len, ByteArray ba) {

    }

    public byte[] toByteArray() {
        return Arrays.copyOf(bb, size);
    }

    public byte[] toByteArray(int off, int len) {
        rangeCheck(off, len);
        return Arrays.copyOfRange(bb, off, off + len);
    }
    
    public ByteArray copy() {
        return new ByteArray(bb, size);
    }

    public void setByte(byte t, int index) {
        rangeCheck(index);
        bb[index] = t;
    }
    
    public void addFirst(byte t) {
        ensureCapacity(size + 1);
        System.arraycopy(bb, 0, bb, 1, size++);
        bb[0] = t;
    }
    
    public void addLast(byte t) {
        ensureCapacity(size + 1);
        bb[size++] = t;
    }

    public void insert(byte t, int beforeIndex) {
        slotRangeCheck(beforeIndex);
        ensureCapacity(size + 1);
        System.arraycopy(bb, beforeIndex, bb, beforeIndex + 1, size - beforeIndex);
        bb[beforeIndex] = t;
        size++;
    }
    
    public boolean swap(int i1, int i2) {
        if (i1 == i2) {
            return false;
        }
        rangeCheck(i1);
        rangeCheck(i2);
        if (bb[i1] != bb[i2]) {
            byte temp = bb[i1];
            bb[i1] = bb[i2];
            bb[i2] = temp;
            return true;
        } else {
            return false;
        }
    }

    public void addFirst(byte[] tt, int off, int len) {
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(bb, 0, bb, len, size);
            size += len;
            System.arraycopy(tt, off, bb, 0, len);
        }
    }

    public void addLast(byte[] tt, int off, int len) {
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(tt, off, bb, size, len);
            size += len;
        }
    }

    public void insert(byte[] tt, int off, int len, int beforeIndex) {
        slotRangeCheck(beforeIndex);
        if (off < 0 || len < 0 || off + len > tt.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(bb, beforeIndex, bb, beforeIndex + len, size - beforeIndex);
            size += len;
            System.arraycopy(tt, off, bb, beforeIndex, len);
        }
    }

    public int removeFirst() {
        if (size <= 0) {
            throw new EmptyStructureException();
        }
        int removed = bb[0];
        size--;
        System.arraycopy(bb, 1, bb, 0, size);
        return removed;
    }

    public int removeLast() {
        if (size <= 0) {
            throw new EmptyStructureException();
        }
        return bb[--size];
    }

    public byte remove(int index) {
        rangeCheck(index);
        byte removed = bb[index];
        System.arraycopy(bb, index + 1, bb, index, --size - index);
        return removed;
    }

    public int remove(@NotNull BytePredicate eliminator) {
        int ptr = 0, count = 0;
        for (int i = 0, size = this.size; i < size; i++) {
            if (eliminator.test(bb[i])) {
                count++;
            } else {
                bb[ptr++] = bb[i];
            }
        }
        size = ptr;
        return count;
    }

    public void removeInto(int off, int len, @Nullable ByteArray ba) {
        rangeCheck(off, len);
        if (len > 0) {
            if (ba != null) {
                ba.addLast(bb, off, len);
            }
            int end = off + len;
            System.arraycopy(bb, end, bb, off, size - end);
            size -= len;
        }
    }

    public void removeInto(@NotNull BytePredicate eliminator, @Nullable ByteArray ba) {
        if (ba == null) {
            remove(eliminator);
        } else {
            int ptr = 0;
            for (int i = 0, size = this.size; i < size; i++) {
                byte t = bb[i];
                if (eliminator.test(t)) {
                    ba.addLast(t);
                } else {
                    bb[ptr++] = t;
                }
            }
            size = ptr;
        }
    }

    public void clear() {
        size = 0;
    }

    public ByteArrayAsInputStream clearToStream() {
        byte[] bb = this.bb;
        int size = this.size;
        this.bb = ByteArrays.ZL_ARRAY;
        this.size = 0;
        return new ByteArrayAsInputStream(bb, size);
    }

    public void sort() {
        Arrays.sort(bb, 0, size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ByteArray)) {
            return false;
        }
        ByteArray that = (ByteArray) obj;
        if (this.size != that.size) {
            return false;
        }
        for (int i = 0, size = this.size; i < size; i++) {
            if (this.bb[i] != that.bb[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(DsToStringUtil.nameFor(getClass()));
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(DsToStringUtil.PRIMITIVE_ELEMENT_SEP);
            }
            sb.append(bb[i]);
        }
        return sb.append(DsToStringUtil.PRIMITIVE_RIGHT_BRACKET).toString();
    }

    public static class ByteArrayAsInputStream extends InputStream {

        private final byte[] bb;

        private final int size;

        private int pos, mark;

        ByteArrayAsInputStream(byte[] bb, int size) {
            this.bb = bb;
            this.size = size;
        }

        /**
         * Reads the next byte of data from this input stream. The value
         * byte is returned as an <code>int</code> in the range
         * <code>0</code> to <code>255</code>. If no byte is available
         * because the end of the stream has been reached, the value
         * <code>-1</code> is returned.
         * <p>
         * This <code>read</code> method
         * cannot block.
         *
         * @return  the next byte of data, or <code>-1</code> if the end of the
         *          stream has been reached.
         */
        @Override
        public int read() throws IOException {
            if (pos < size) {
                return bb[pos++];
            } else {
                return -1;
            }
        }

        /**
         * Reads up to <code>len</code> bytes of data into an array of bytes
         * from this input stream.
         * If <code>pos</code> equals <code>count</code>,
         * then <code>-1</code> is returned to indicate
         * end of file. Otherwise, the  number <code>k</code>
         * of bytes read is equal to the smaller of
         * <code>len</code> and <code>count-pos</code>.
         * If <code>k</code> is positive, then bytes
         * <code>buf[pos]</code> through <code>buf[pos+k-1]</code>
         * are copied into <code>b[off]</code>  through
         * <code>b[off+k-1]</code> in the manner performed
         * by <code>System.arraycopy</code>. The
         * value <code>k</code> is added into <code>pos</code>
         * and <code>k</code> is returned.
         * <p>
         * This <code>read</code> method cannot block.
         *
         * @param   b     the buffer into which the data is read.
         * @param   off   the start offset in the destination array <code>b</code>
         * @param   len   the maximum number of bytes read.
         * @return  the total number of bytes read into the buffer, or
         *          <code>-1</code> if there is no more data because the end of
         *          the stream has been reached.
         * @exception  NullPointerException If <code>b</code> is <code>null</code>.
         * @exception  IndexOutOfBoundsException If <code>off</code> is negative,
         * <code>len</code> is negative, or <code>len</code> is greater than
         * <code>b.length - off</code>
         */
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (off < 0 || len < 0 || off + len > b.length) {
                throw new IndexOutOfBoundsException();
            }
            if (pos > size) {
                return -1;
            }
            int mov = size - pos;
            if (len > mov) {
                len = mov;
            }
            if (len == 0) {
                return 0;
            }
            System.arraycopy(bb, pos, b, off, len);
            pos += len;
            return len;
        }

        /**
         * Skips <code>n</code> bytes of input from this input stream. Fewer
         * bytes might be skipped if the end of the input stream is reached.
         * The actual number <code>k</code>
         * of bytes to be skipped is equal to the smaller
         * of <code>n</code> and  <code>count-pos</code>.
         * The value <code>k</code> is added into <code>pos</code>
         * and <code>k</code> is returned.
         *
         * @param   n   the number of bytes to be skipped.
         * @return  the actual number of bytes skipped.
         */
        @Override
        public long skip(long n) throws IOException {
            long k = size - pos;
            if (n < k) {
                k = Math.max(0, n);
            }
            pos += k;
            return k;
        }

        /**
         * Returns the number of remaining bytes that can be read (or skipped over)
         * from this input stream.
         * <p>
         * The value returned is <code>count&nbsp;- pos</code>,
         * which is the number of bytes remaining to be read from the input buffer.
         *
         * @return  the number of remaining bytes that can be read (or skipped
         *          over) from this input stream without blocking.
         */
        public synchronized int available() {
            return size - pos;
        }

        /**
         * Tests if this <code>InputStream</code> supports mark/reset. The
         * <code>markSupported</code> method of <code>ByteArrayAsInputStream</code>
         * always returns <code>true</code>.
         */
        @Override
        public boolean markSupported() {
            return true;
        }

        /**
         * Set the current marked position in the stream.
         * ByteArrayInputStream objects are marked at position zero by
         * default when constructed.  They may be marked at another
         * position within the buffer by this method.
         * <p>
         * If no mark has been set, then the value of the mark is the
         * offset passed to the constructor (or 0 if the offset was not
         * supplied).
         *
         * <p> Note: The <code>readLimit</code> for this class
         *  has no meaning.
         */
        @Override
        public void mark(int readLimit) {
            mark = pos;
        }

        /**
         * Resets the buffer to the marked position.  The marked position
         * is 0 unless another position was marked or an offset was specified
         * in the constructor.
         */
        public void reset() {
            pos = mark;
        }
    }

    public static class ByteArrayAsOutputStream extends OutputStream {

        private byte[] bb;

        private int size;

        public ByteArrayAsOutputStream() {
            bb = new byte[DEFAULT_CAPACITY];
        }

        public ByteArray closeToByteArray() {
            byte[] bb = this.bb;
            if (bb == null) {
                throw new IllegalStateException();
            }
            this.bb = null;
            return new ByteArray(bb, size);
        }

        private void ensureCapacity(int minCapacity) {
            int len = bb.length;
            if (len < minCapacity) {
                bb = Arrays.copyOf(bb, Math.max(minCapacity, len << 1));
            }
        }

        /**
         * Writes the specified byte to this byte array output stream.
         *
         * @param   b   the byte to be written.
         */
        @Override
        public void write(int b) throws IOException {
            byte[] bb = this.bb;
            if (bb == null) {
                throw new IllegalStateException();
            }
            ensureCapacity(size + 1);
            bb[size++] = (byte) b;
        }

        public synchronized void write(byte b[], int off, int len) {
            if (off < 0 || len < 0 || off + len > b.length) {
                throw new IndexOutOfBoundsException();
            }
            ensureCapacity(size + len);
            System.arraycopy(b, off, bb, size, len);
            size += len;
        }

        @Override
        public String toString() {
            return new String(bb, 0, size);
        }
    }
}
