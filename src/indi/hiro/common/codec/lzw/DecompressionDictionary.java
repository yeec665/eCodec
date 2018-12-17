package indi.hiro.common.codec.lzw;

import indi.hiro.common.codec.CodeCorruptedException;
import indi.hiro.common.ds.primitive.IntegerArray;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/11/3.
 */
public class DecompressionDictionary<D> {

    private final List<D> commonDictionary;

    private final ArrayList<IntegerArray> constructedDictionary = new ArrayList<>();

    private int offset = 1;

    public DecompressionDictionary(List<D> commonDictionary) {
        this.commonDictionary = commonDictionary;
        resetDictionary();
    }

    public void resetDictionary() {
        constructedDictionary.clear();
        for (int i = 0, size = commonDictionary.size(); i < size; i++) {
            IntegerArray ia = new IntegerArray(1);
            ia.addLast(i);
            constructedDictionary.add(ia);
        }
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void decode(Iterable<Integer> msg, Consumer<D> out) {
        IntegerArray entry;
        IntegerArray seq = new IntegerArray();
        for (int k : msg) {
            k -= offset;
            if (0 <= k || k < constructedDictionary.size()) {
                entry = constructedDictionary.get(k);
            } else {
                entry = seq.copy();
                entry.addLast(seq.getInt(0));
            }
            output(entry, out);
            if (seq.notEmpty()) {
                seq.addLast(entry.get(0));
                constructedDictionary.add(seq.copy());
            }
            seq.clear();
            entry.copyInto(seq);
        }
    }

    private void output(IntegerArray ia, Consumer<D> out) {
        int size = commonDictionary.size();
        for (int i = 0, n = ia.size(); i < n; i++) {
            int x = ia.getInt(i);
            if (x < 0 || x >= size) {
                if (CodeCorruptedException.STRICT_MODE) {
                    throw new CodeCorruptedException();
                }
                continue;
            }
            out.accept(commonDictionary.get(x));
        }
    }
}
