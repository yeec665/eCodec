package indi.hiro.common.codec.compression;

import indi.hiro.common.ds.primitive.BooleanBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hiro on 2018/11/4.
 */
public class InitialCodeAssignment<D> {

    public static ArrayList<Character> dictionary(String msg) {
        ArrayList<Character> out = new ArrayList<>();
        for (Character c : msg.toCharArray()) {
            if (!out.contains(c)) {
                out.add(c);
            }
        }
        return out;
    }

    private final List<D> symbolList;

    private final int bitLen;

    private final boolean lsbFirst;

    public InitialCodeAssignment(List<D> symbolList, int bitLen, boolean lsbFirst) {
        int size = symbolList.size();
        if (size < 2) {
            throw new IllegalArgumentException();
        }
        this.symbolList = symbolList;
        this.bitLen = Math.max(bitLen, Integer.SIZE - Integer.numberOfLeadingZeros(size - 1));
        this.lsbFirst = lsbFirst;
    }

    public int getBitLen() {
        return bitLen;
    }

    public void appendCode(D d, BooleanBuilder bb) {
        int index = symbolList.indexOf(d);
        if (index < 0) {
            if (CodeCorruptedException.STRICT_MODE) {
                throw new CodeCorruptedException();
            }
        } else {
            appendShortInt(index, bb);
        }
    }

    private void appendShortInt(int x, BooleanBuilder bb) {
        if (lsbFirst) {
            for (int i = 0; i < bitLen; i++) {
                bb.append((x & (1 << i)) != 0);
            }
        } else {
            for (int i = bitLen - 1; i >= 0; i--) {
                bb.append((x & (1 << i)) != 0);
            }
        }
    }

    public D readCode(BooleanBuilder bb, int off) {
        int index = readShortInt(bb, off);
        if (index >= 0 && index < symbolList.size()) {
            return symbolList.get(index);
        } else {
            throw new CodeCorruptedException();
        }
    }

    private int readShortInt(BooleanBuilder bb, int off) {
        int x = 0;
        if (lsbFirst) {
            for (int i = 0; i < bitLen; i++) {
                if (bb.get(off + i)) {
                    x |= 1 << i;
                }
            }
        } else {
            off += bitLen - 1;
            for (int i = 0; i < bitLen; i++) {
                if (bb.get(off - i)) {
                    x |= 1 << i;
                }
            }
        }
        return x;
    }
}
