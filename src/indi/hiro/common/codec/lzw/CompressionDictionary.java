package indi.hiro.common.codec.lzw;

import indi.hiro.common.codec.SymbolNotFoundException;
import indi.hiro.common.ds.primitive.IntegerArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hiro on 2018/11/3.
 */
public class CompressionDictionary<D> {

    private final List<D> commonDictionary;

    private final HashMap<IntegerArray.Hash, Integer> constructedDictionary = new HashMap<>();

    private int offset = 1;

    public CompressionDictionary(List<D> commonDictionary) {
        this.commonDictionary = commonDictionary;
        resetDictionary();
    }

    public void resetDictionary() {
        constructedDictionary.clear();
        for (int i = 0, size = commonDictionary.size(); i < size; i++) {
            IntegerArray.Hash ia = new IntegerArray.Hash(1);
            ia.addLast(i);
            constructedDictionary.put(ia, i);
        }
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public ArrayList<Integer> encode(Iterable<D> symbolSource) {
        ArrayList<Integer> out = new ArrayList<>();
        IntegerArray.Hash seq = new IntegerArray.Hash();
        int seqCode = -1;
        Iterator<D> itr = symbolSource.iterator();
        while (itr.hasNext()) {
            int x = commonDictionary.indexOf(itr.next());
            if (x != -1) {
                seq.addLast(x);
                seqCode = x;
                break;
            }
            if (SymbolNotFoundException.STRICT_MODE) {
                throw new SymbolNotFoundException();
            }
        }
        while (itr.hasNext()) {
            int x = commonDictionary.indexOf(itr.next());
            if (x == -1) {
                if (SymbolNotFoundException.STRICT_MODE) {
                    throw new SymbolNotFoundException();
                }
                continue;
            }
            seq.addLast(x);
            Integer newCode = constructedDictionary.get(seq);
            if (newCode == null) {
                out.add(offset + seqCode);
                constructedDictionary.put(seq.copy(), constructedDictionary.size());
                seq.clear();
                seq.addLast(x);
                seqCode = x;
            } else {
                seqCode = newCode;
            }
        }
        if (seqCode != -1) {
            out.add(offset + seqCode);
        }
        return out;
    }
}
