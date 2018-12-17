package indi.hiro.common.ds.primitive;

import indi.hiro.common.ds.util.DataContainer;
import indi.hiro.common.lang.IntegerArrays;

import java.util.Arrays;

/**
 * Created by Hiro on 2018/10/17.
 */
public class BooleanBuilder implements BooleanConsumer, DataContainer.PrimitiveContainer {
    
    public static BooleanBuilder createEmpty() {
        return new BooleanBuilder(IntegerArrays.ZL_ARRAY, 0);
    }

    public static BooleanBuilder createEmptyWithCapacity(int capacity) {
        return new BooleanBuilder(new int[capacity >> 5], 0);
    }

    public static BooleanBuilder createFalseWithCapacity(int capacity) {
        return new BooleanBuilder(new int[(capacity + 0x1F) >> 5], capacity);
    }
    
    public static BooleanBuilder createOneBit(boolean x) {
        return new BooleanBuilder(new int[]{x ? 0 : 1}, 1);
    }

    int[] content;

    int bitLen;
    
    public BooleanBuilder() {
        this(IntegerArrays.ZL_ARRAY, 0);
    }

    BooleanBuilder(int[] content, int bitLen) {
        this.content = content;
        this.bitLen = bitLen;
    }

    public boolean empty() {
        return bitLen > 0;
    }

    public int bitLen() {
        return bitLen;
    }
    
    boolean directGet(int i) {
        return (content[i >> 5] & (1 << (0x1F & i))) != 0;
    }

    public boolean get(int i) {
        if (i < 0 || i >= bitLen) {
            throw new IndexOutOfBoundsException();
        }
        return directGet(i);
    }

    public BooleanBuilder append(boolean x) {
        int len = content.length;
        if (bitLen + 1 >= len << 5) {
            content = Arrays.copyOf(content, Math.max(len + 1, 2 * len));
        }
        if (x) {
            content[bitLen >> 5] |= 1 << (0x1F & bitLen);
        } else {
            content[bitLen >> 5] &= ~(1 << (0x1F & bitLen));
        }
        bitLen++;
        return this;
    }
    
    void directSetT(int i) {
        content[i >> 5] |= 1 << (0x1F & i);
    }

    void directSetF(int i) {
        content[i >> 5] &= ~(1 << (0x1F & i));
    }

    void directFlip(int i) {
        content[i >> 5] ^= 1 << (0x1F & i);
    }
    
    public void set(boolean x, int i) {
        if (i < 0 || i >= bitLen) {
            throw new IndexOutOfBoundsException();
        }
        if (x) {
            directSetT(i);
        } else {
            directSetF(i);
        }
    }

    @Override
    public void accept(boolean value) {
        append(value);
    }

    public void supplyTo(BooleanConsumer bc) {
        for (int i = 0; i < bitLen; i++) {
            bc.accept((content[i >> 5] & (1 << (0x1F & i))) != 0);
        }
    }

    public void supplyTo(BooleanConsumer bc, int off, int len) {
        if (len <= 0) {
            return;
        }
        len += off;
        if (off < 0 || len > bitLen) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = off; i < len; i++) {
            bc.accept((content[i >> 5] & (1 << (0x1F & i))) != 0);
        }
    }

    public BooleanBuilder append(BooleanBuilder bb, boolean reverse) {
        int bbb = bb.bitLen;
        int len = content.length;
        if (bitLen + bbb >= len << 5) {
            content = Arrays.copyOf(content, Math.max((bitLen + bbb + 31) >> 5, 2 * len));
        }
        if (reverse) {
            for (int i = bbb - 1; i >= 0; i--) {
                if ((bb.content[i >> 5] & (1 << (0x1F & i))) != 0) {
                    content[bitLen >> 5] |= 1 << (0x1F & bitLen);
                } else {
                    content[bitLen >> 5] &= ~(1 << (0x1F & bitLen));
                }
                bitLen++;
            }
        } else {
            for (int i = 0; i < bbb; i++) {
                if ((bb.content[i >> 5] & (1 << (0x1F & i))) != 0) {
                    content[bitLen >> 5] |= 1 << (0x1F & bitLen);
                } else {
                    content[bitLen >> 5] &= ~(1 << (0x1F & bitLen));
                }
                bitLen++;
            }
        }
        return this;
    }

    /**
     * array[0], array[1], array[2], ... , array[array.length - 1]
     * start ................................................. end
     * 0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000 - start align
     * 0x00000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF - end align
     */
    public int[] toIntArray(boolean reverse, boolean startAlign) {
        if (reverse) {
            BooleanBuilder bb = createEmpty();
            bb.content = new int[bitLen >> 5];
            bb.bitLen = startAlign ? 0 : 0x20 - (0x1F & bitLen);
            bb.append(this, true);
            return bb.content;
        } else {
            if (startAlign || (bitLen & 0x1F) == 0) {
                return Arrays.copyOf(content, bitLen >> 5);
            }
            BooleanBuilder bb = createEmpty();
            bb.content = new int[bitLen >> 5];
            bb.bitLen = 0x20 - (0x1F & bitLen);
            supplyTo(bb);
            return bb.content;
        }
    }

    public BooleanBuilder copy() {
        return new BooleanBuilder(Arrays.copyOf(content, content.length), bitLen);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(bitLen);
        for (int i = 0; i < bitLen; i++) {
            if ((content[i >> 5] & (1 << (0x1F & i))) != 0) {
                sb.append('1');
            } else {
                sb.append('0');
            }
        }
        return sb.toString();
    }
}
