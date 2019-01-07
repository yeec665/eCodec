package indi.hiro.common.codec.compression;

import indi.hiro.common.ds.primitive.BooleanBuilder;
import indi.hiro.common.ds.primitive.CharCounter;
import indi.hiro.common.ds.primitive.IntCounter;
import indi.hiro.common.math.sym.nBasic.BinInt;
import indi.hiro.common.math.sym.nBasic.IntOp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/10/18.
 */
public class ArithmeticSequence<D> {

    public static ArithmeticSequence<Integer> buildFromIC(IntCounter ic, boolean byFreq, boolean desc) {
        ArithmeticSequence<Integer> rv = new ArithmeticSequence<>();
        if (!byFreq && !desc) {
            ic.iterateII(rv::add);
            return rv;
        }
        ArrayList<IntCounter.IntPair> ip = new ArrayList<>(ic.size());
        ic.iterateIP(ip::add);
        if (byFreq) {
            // sort by frequency
            ip.sort(Comparator.comparingInt(p -> p.b));
        }
        if (desc) {
            for (int i = ip.size() - 1; i >= 0; i--) {
                IntCounter.IntPair p = ip.get(i);
                rv.add(p.a, p.b);
            }
        } else {
            for (IntCounter.IntPair p : ip) {
                rv.add(p.a, p.b);
            }
        }
        return rv;
    }

    public static ArithmeticSequence<Character> buildFromCC(CharCounter ic, boolean byFreq, boolean desc) {
        ArithmeticSequence<Character> rv = new ArithmeticSequence<>();
        if (!byFreq && !desc) {
            ic.iterateII(rv::add);
            return rv;
        }
        ArrayList<CharCounter.CharIntPair> ip = new ArrayList<>(ic.size());
        ic.iterateIP(ip::add);
        if (byFreq) {
            // sort by frequency
            ip.sort(Comparator.comparingInt(p -> p.b));
        }
        if (desc) {
            for (int i = ip.size() - 1; i >= 0; i--) {
                CharCounter.CharIntPair p = ip.get(i);
                rv.add(p.a, p.b);
            }
        } else {
            for (CharCounter.CharIntPair p : ip) {
                rv.add(p.a, p.b);
            }
        }
        return rv;
    }
    
    private static BinInt pPlus(BinInt a, BinInt b) {
        if (a.isZero()) {
            return b;
        }
        if (b.isZero()) {
            return a;
        }
        return BinInt.plus(a, b);
    }
    
    private static BinInt pMultiply(BinInt a, BinInt b) {
        if (a.isZero() || b.isOne()) {
            return a;
        }
        if (a.isOne() || b.isZero()) {
            return b;
        }
        return BinInt.multiply(a, b);
    }

    private final ArrayList<AsEntry<D>> list = new ArrayList<>();

    public ArithmeticSequence() {
        list.add(new AsEntry<>(BinInt.BIN_ZERO, BinInt.BIN_ONE));
    }

    public void add(D d, int frequency) {
        if (frequency <= 0) {
            throw new IllegalArgumentException();
        }
        AsEntry<D> last = list.get(list.size() - 1);
        last.symbol = d;
        last.len = BinInt.create(frequency);
        list.add(new AsEntry<>(pPlus(last.off, last.len), BinInt.BIN_ONE));
    }

    public int indexOf(D symbol) {
        for (int i = 0, n = list.size() - 1; i < n; i++) {
            if (list.get(i).symbol == symbol) {
                return i;
            }
        }
        return -1;
    }

    public AsEntry<D> search(D symbol) {
        for (int i = 0, n = list.size() - 1; i < n; i++) {
            AsEntry<D> entry = list.get(i);
            if (entry.symbol.equals(symbol)) {
                return entry;
            }
        }
        return null;
    }

    public BinInt frequencySum() {
        AsEntry<D> last = list.get(list.size() - 1);
        return pPlus(last.off, last.len);
    }

    public BooleanBuilder encode(Iterable<D> symbolSource) {
        BinInt off = BinInt.BIN_ZERO;
        BinInt len = BinInt.BIN_ONE;
        BinInt sum = frequencySum();
        if (!sum.positive()) {
            throw new BadDictionaryException();
        }
        int n = 0;
        for (D d : symbolSource) {
            AsEntry<D> entry = search(d);
            if (entry == null) {
                if (SymbolNotFoundException.STRICT_MODE) {
                    throw new SymbolNotFoundException();
                }
                continue;
            }
            off = pPlus(pMultiply(off, sum), pMultiply(len, entry.off));
            len = pMultiply(len, entry.len);
            n++;
        }
        AsEntry<D> entry = list.get(list.size() - 1);
        off = pPlus(pMultiply(off, sum), pMultiply(len, entry.off));
        len = pMultiply(len, entry.len);
        n++;
        return encode(off, len, sum, n);
    }

    private BooleanBuilder encode(BinInt off, BinInt len, BinInt sum, int n) {
        BinInt lim = pPlus(off, len);
        BinInt sumN = IntOp.powerBinI(sum, n);
        BinInt code = BinInt.BIN_ZERO;
        int k = 0;
        while (true) {
            BinInt c0 = code.shift(1);
            BinInt c1 = pPlus(c0, BinInt.BIN_ONE);
            k++;
            BinInt csn = pMultiply(c1, sumN);
            if (BinInt.compare(lim.shift(k), csn) <= 0) {
                code = c0;
                csn = pMultiply(c0, sumN);
            } else {
                code = c1;
            }
            if (BinInt.compare(off.shift(k), csn) <= 0) {
                break;
            }
        }
        BooleanBuilder bb = BooleanBuilder.createEmpty();
        code.append(bb, k, true);
        return bb;
    }

    public void decode(BooleanBuilder msg, Consumer<D> out) {
        int size = list.size();
        int k = msg.bitLen();
        BinInt sum = frequencySum();
        BinInt a = BinInt.create(msg.toIntArray(true, true), false);
        BinInt b = BinInt.BIN_ONE.shift(k);
        while (true) {
            a = pMultiply(a, sum);
            int i;
            BinInt p1 = BinInt.BIN_ZERO;
            for (i = 1; i < size; i++) {
                BinInt p2 = pMultiply(list.get(i).off, b);
                if (BinInt.compare(p2, a) > 0) {
                    AsEntry<D> entry = list.get(i - 1);
                    out.accept(entry.symbol);
                    a = BinInt.minus(a, p1);
                    b = pMultiply(b, entry.len);
                    break;
                }
                p1 = p2;
            }
            if (i == size) {
                break;
            }
        }
    }

    @Override
    public String toString() {
        return list.toString();
    }

    public static class AsEntry<D> {

        D symbol;
        BinInt off;
        BinInt len;

        public AsEntry() {
            off = BinInt.BIN_ZERO;
            len = BinInt.BIN_ZERO;
        }

        public AsEntry(BinInt off, BinInt len) {
            this.off = off;
            this.len = len;
        }

        public AsEntry(D symbol, BinInt off, BinInt len) {
            this.symbol = symbol;
            this.off = off;
            this.len = len;
        }

        @Override
        public String toString() {
            return symbol + "[" + off.intValue() + ", " + len.intValue() + "]";
        }
    }
}
