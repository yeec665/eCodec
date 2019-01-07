package indi.hiro.common.ds.generic;

import indi.hiro.common.codec.compression.ArithmeticSequence;
import indi.hiro.common.codec.compression.HuffmanTreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Hiro on 2018/11/2.
 */
public class LinearCounter<T> implements GenericCounter<T> {

    private Object[] fo;
    private int[] fi;

    private int iNull;
    private int size;
    private int iSum;

    public LinearCounter(int initialCapacity) {
        initialCapacity = Math.max(4, initialCapacity);
        fo = new Object[initialCapacity];
        fi = new int[initialCapacity];
    }

    public LinearCounter() {
        this(4);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int iSum() {
        return iSum;
    }

    private int indexWithLinearSearch(Object x) {
        for (int i = size - 1; i >= 0; i--) {
            if (fo[i].equals(x)) {
                return i;
            }
        }
        return -1;
    }

    private void addP(T x, int n) {
        iSum += n;
        if (x == null) {
            iNull += n;
        } else {
            int p = indexWithLinearSearch(x);
            if (p >= 0) {
                fi[p] += n;
                return;
            }
            int foiLen = fo.length;
            if (size + 1 > foiLen) {
                foiLen <<= 1;
                Object[] nFo = new Object[foiLen];
                System.arraycopy(fo, 0, nFo, 0, size);
                fo = nFo;
                fi = Arrays.copyOf(fi, foiLen);
            }
            fo[size] = x;
            fi[size] = n;
            size++;
        }
    }

    private void minusP(T x, int n) {
        if (x == null) {
            if (n > iNull) {
                iSum -= iNull;
                iNull = 0;
            } else {
                iSum -= n;
                iNull -= n;
            }
        } else {
            //TODO
        }
    }

    @Override
    public void accept(T x) {
        accept(x, 1);
    }

    @Override
    public void accept(T x, int n) {
        if (n > 0) {
            addP(x, n);
        } else {
            minusP(x, n);
        }
    }

    @Override
    public void acceptAll(Iterable<T> xi) {
        for (T x : xi) {
            addP(x, 1);
        }
    }

    @Override
    public int applyAsInt(T x) {
        if (x == null) {
            return iNull;
        } else {
            int p = indexWithLinearSearch(x);
            if (p >= 0) {
                return fi[p];
            } else {
                return 0;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<HuffmanTreeNode<T>> toHuffmanSegments() {
        ArrayList<HuffmanTreeNode<T>> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new HuffmanTreeNode<>((T) fo[i], fi[i]));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public void addToArithmeticSequence(ArithmeticSequence<T> as, boolean byFreq, boolean desc) {
        ArrayList<OiPair> oiPairs = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            oiPairs.add(new OiPair(fo[i], fi[i]));
        }
        if (byFreq) {
            oiPairs.sort(Comparator.comparingInt(p -> p.v));
        }
        if (desc) {
            for (int i = oiPairs.size() - 1; i >= 0; i--) {
                OiPair p = oiPairs.get(i);
                as.add((T) p.k, p.v);
            }
        } else {
            for (OiPair p : oiPairs) {
                as.add((T) p.k, p.v);
            }
        }
    }
}
