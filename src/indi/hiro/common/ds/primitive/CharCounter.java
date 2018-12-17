package indi.hiro.common.ds.primitive;

import com.sun.istack.internal.NotNull;
import indi.hiro.common.ds.util.DataStructureException;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Created by Hiro on 2018/10/18.
 */
public class CharCounter {

    private char[] fc;
    /**
     * All counts are positive
     */
    private int[] fi;

    private int size;
    private int iSum;

    public CharCounter(int initialCapacity) {
        initialCapacity = Math.max(4, initialCapacity);
        fc = new char[initialCapacity];
        fi = new int[initialCapacity];
    }

    public CharCounter() {
        this(4);
    }

    public CharCounter(String s) {
        this();
        add(s);
    }

    public int size() {
        return size;
    }

    public void iterateI(IntConsumer ic) {
        for (int i = 0, size = this.size; i < size; i++) {
            ic.accept(fi[i]);
        }
    }

    public void iterateII(CharIntConsumer cic) {
        for (int i = 0, size = this.size; i < size; i++) {
            cic.accept(fc[i], fi[i]);
        }
    }

    public void iterateIP(Consumer<CharIntPair> ipc) {
        for (int i = 0, size = this.size; i < size; i++) {
            ipc.accept(new CharIntPair(fc[i], fi[i]));
        }
    }

    @Deprecated
    private int indexWithLinearSearch(char x) {
        int p = 0;
        while (p < size) {
            if (fc[p] < x) {
                p++;
            } else {
                break;
            }
        }
        return p;
    }

    private int indexWithBinarySearch(char x) {
        int pL = 0, pR = size;
        while (pL < pR) {
            int pM = (pL + pR) >>> 1;
            char mx = fc[pM];
            if (mx < x) {
                pL = pM + 1;
            } else if (mx > x) {
                pR = pM;
            } else {
                return pM;
            }
        }
        return pL;
    }

    public int count(char x) {
        int p = indexWithBinarySearch(x);
        if (p < size && fc[p] == x) {
            return fi[p];
        }
        return 0;
    }



    public int countAll() {
        return iSum;
    }

    private void addSum(int n) {
        int nSum = iSum + n;
        if (nSum < 0) {
            throw new DataStructureException();
        }
        iSum = nSum;
    }

    private int addP(char x, int n) {
        addSum(n);
        int p = indexWithBinarySearch(x);
        if (p < size && fc[p] == x) {
            return fi[p] += n;
        }
        int fciLen = fc.length;
        if (size + 1 > fciLen) {
            fciLen <<= 1;
            fc = Arrays.copyOf(fc, fciLen);
            fi = Arrays.copyOf(fi, fciLen);
        }
        System.arraycopy(fc, p, fc, p + 1, size - p);
        fc[p] = x;
        System.arraycopy(fi, p, fi, p + 1, size - p);
        fi[p] = n;
        size++;
        return n;
    }

    private int minusP(char x, int n) {

        // TODO

        return 0;
    }

    public void addOne(char x) {
        addP(x, 1);
    }

    public void add(char[] xx) {
        for (char x : xx) {
            addP(x, 1);
        }
    }

    public void add(char[] xx, int off, int len) {
        for (int lim = off + len; off < lim; off++) {
            addP(xx[off], 1);
        }
    }

    /**
     * @param cs typically a String
     */
    public void add(@NotNull CharSequence cs) {
        for (int i = 0, n = cs.length(); i < n; i++) {
            addP(cs.charAt(i), 1);
        }
    }

    public void add(char x, int n) {
        if (n > 0) {
            addP(x, n);
        } else {
            minusP(x, -n);
        }
    }

    public int addAndCount(char x, int n) {
        if (n > 0) {
            return addP(x, n);
        } else if (n < 0) {
            return minusP(x, -n);
        } else {
            return count(x);
        }
    }

    public void add(CharCounter cc) {
        cc.iterateII(this::addP);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0, size = this.size; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(fc[i]).append('=').append(fi[i]);
        }
        return sb.append("}").toString();
    }

    public static class CharIntPair {
        public char a;
        public int b;

        public CharIntPair(char a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    @FunctionalInterface
    public interface CharIntConsumer {

        void accept(char a, int b);
    }

    @FunctionalInterface
    public interface CharIntPredicate {

        boolean test(char a, int b);
    }
}
