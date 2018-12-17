package indi.hiro.common.codec.huffman;

import indi.hiro.common.codec.CodeCorruptedException;
import indi.hiro.common.ds.primitive.BooleanBuilder;

import java.util.List;

/**
 * Created by Hiro on 2018/11/4.
 */
public class InitialCodeAssignment<D> {

    private final List<D> symbolList;

    private final int bitLen;

    private final boolean lsbFirst;

    public InitialCodeAssignment(List<D> symbolList, int bitLen, boolean lsbFirst) {
        int size = symbolList.size();
        if (size < 2) {
            throw new IllegalArgumentException();
        }
        this.symbolList = symbolList;
        this.bitLen = Math.max(bitLen, Integer.SIZE - Integer.numberOfLeadingZeros(size));
        this.lsbFirst = lsbFirst;
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
}
