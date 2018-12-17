package indi.hiro.common.math.discrete;

import indi.hiro.common.lang.BooleanArrays;
import indi.hiro.common.lang.IntegerArrays;

import java.util.Iterator;

public class ArrangementIterator implements Iterator<int[]> {

    private final int n, m;
    private final int[] a;
    private final boolean[] b;

    private boolean hasNextFlag = true;

    ArrangementIterator(int n, int m) {
        this.n = n;
        this.m = m;
        this.a = IntegerArrays.arithmeticSequence(m, 0, 1);
        this.b = BooleanArrays.filledArray(n, i -> i >= m);
    }

    @Override
    public boolean hasNext() {
        return hasNextFlag;
    }

    @Override
    public int[] next() {
        int p = m - 1;
        while (p >= 0 && inc1(p)) p--;
        hasNextFlag = p >= 0;
        while (++p < m) inc2(p);
        return a;
    }

    private boolean inc1(int i) {
        b[a[i]] = true;
        for (int j = a[i] + 1; j < n; j++) {
            if (b[j]) {
                b[j] = false;
                a[i] = j;
                return false;
            }
        }
        return true;
    }

    private void inc2(int i) {
        for (int j = 0; j < n; j++) {
            if (b[j]) {
                b[j] = false;
                a[i] = j;
                break;
            }
        }
    }
}
