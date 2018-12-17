package indi.hiro.common.math.sym.nBasic;

import indi.hiro.common.math.sym.util.SymCorruptedException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class FiniteFactorSet implements Iterable<FinitePrimeFactor> {

    public static FiniteFactorSet tryFactorization(IntN x) {
        FiniteFactorSet set = new FiniteFactorSet();
        x = x.abs();
        if (x.isZero() || x.isOne()) {
            return set;
        }
        if (x instanceof BinInt) {
            return factorizationBin((BinInt) x, set);
        }
        if (x instanceof BcdInt) {
            return factorizationBcd((BcdInt) x, set);
        }
        return null;
    }

    private static void factorizationInt(FiniteFactorSet set, int v) {
        while (true) {
            FinitePrimeLink pl = FinitePrimeLink.smallestFactor(v);
            if (pl == null) {
                break;
            }
            while (v % pl.v == 0) {
                v /= pl.v;
                set.put(pl);
            }
        }
        if (v != 1) {
            throw new SymCorruptedException();
        }
    }

    private static BinInt preProcessBin(BinInt x, FiniteFactorSet set) {
        int nZero = 0;
        for (int i = 0; i < x.len; i++) {
            int bx = x.readMag(i);
            if (bx == 0) {
                nZero += 32;
            } else {
                nZero += Integer.numberOfTrailingZeros(bx);
                break;
            }
        }
        if (nZero > 0) {
            x = x.shift(-nZero);
            set.put(FinitePrimeLink.P_2, nZero);
        }
        return x;
    }

    private static FiniteFactorSet factorizationBin(BinInt x, FiniteFactorSet set) {
        x = preProcessBin(x, set);
        if (x.isOne()) {
            return set;
        }
        if (x.hasIntValue()) {
            factorizationInt(set, x.intValue());
            return set;
        } else {
            return null;
        }
    }

    private static BcdInt preProcessBcd(BcdInt x, FiniteFactorSet set) {
        while (x.readDigit(0) == 0) {
            x = x.shift(-1);
            set.put(FinitePrimeLink.P_2);
            set.put(FinitePrimeLink.P_5);
        }
        while (x.readDigit(0) % 2 == 0) {
            x = BcdInt.divideDigit(x, 2);
            set.put(FinitePrimeLink.P_2);
        }
        while (x.readDigit(0) == 5) {
            x = BcdInt.divideDigit(x, 5);
            set.put(FinitePrimeLink.P_5);
        }
        int digitSum = x.digitSum();
        while (digitSum % 9 == 0) {
            x = BcdInt.divideDigit(x, 9);
            digitSum = x.digitSum();
            set.put(FinitePrimeLink.P_3, 2);
        }
        if (x.digitSum() % 3 == 0) {
            x = BcdInt.divideDigit(x, 3);
            set.put(FinitePrimeLink.P_3, 3);
        }
        return x;
    }

    private static FiniteFactorSet factorizationBcd(BcdInt x, FiniteFactorSet set) {
        x = preProcessBcd(x, set);
        if (x.isOne()) {
            return set;
        }
        if (x.hasIntValue()) {
            factorizationInt(set, x.intValue());
            return set;
        } else {
            return null;
        }
    }

    private FinitePrimeFactor[] set = new FinitePrimeFactor[1];
    private int size = 0;
    private boolean sorted = true;

    public FiniteFactorSet() {

    }

    public void put(FinitePrimeLink pl) {
        for (int i = size - 1; i >= 0; i--) {
            if (set[i].factor == pl) {
                set[i].inc();
                return;
            }
        }
        if (size == set.length) {
            FinitePrimeFactor[] newSet = new FinitePrimeFactor[size * 2];
            System.arraycopy(set, 0, newSet, 0, size);
            set = newSet;
        }
        set[size++] = new FinitePrimeFactor(pl);
        sorted = false;
    }

    public void put(FinitePrimeLink pl, int n) {
        for (int i = size - 1; i >= 0; i--) {
            if (set[i].factor == pl) {
                set[i].inc(n);
                return;
            }
        }
        if (size == set.length) {
            FinitePrimeFactor[] newSet = new FinitePrimeFactor[size * 2];
            System.arraycopy(set, 0, newSet, 0, size);
            set = newSet;
        }
        set[size++] = new FinitePrimeFactor(pl, n);
        sorted = false;
    }

    public FinitePrimeFactor get(int index) {
        if (index >= 0 && index < size) {
            return set[index];
        }
        throw new IndexOutOfBoundsException();
    }

    public void sort() {
        if (!sorted) {
            sorted = true;
            Arrays.sort(set, 0, size, Comparator.comparingInt(a -> a.factor.v));
        }
    }

    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FiniteFactorSet && equalMe((FiniteFactorSet) obj);
    }

    public boolean equalMe(FiniteFactorSet that) {
        if (this == that) {
            return true;
        }
        sort();
        that.sort();
        if (size != that.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!set[i].equalMe(that.set[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        sort();
        StringBuilder sb = new StringBuilder("FiniteFactorSet[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(" * ");
            }
            sb.append(set[i].factor.v);
            sb.append('^');
            sb.append(set[i].count());
        }
        return sb.append(']').toString();
    }

    @Override
    public Iterator<FinitePrimeFactor> iterator() {
        return new FiniteFactorSetIterator();
    }

    class FiniteFactorSetIterator implements Iterator<FinitePrimeFactor> {

        private int ptr = 0;

        @Override
        public boolean hasNext() {
            return ptr < size;
        }

        @Override
        public FinitePrimeFactor next() {
            return set[ptr];
        }
    }
}
