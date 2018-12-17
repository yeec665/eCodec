package indi.hiro.common.math.discrete;

import indi.hiro.common.lang.BooleanArrays;
import indi.hiro.common.math.basic.BoundI;
import indi.hiro.common.lang.IntegerArrays;

import java.util.Iterator;

public class FullArrangementIterator implements Iterator<int[]> {

    private final int n;
    private final int[] a;
    private final boolean[] b;

    private boolean hasNextFlag = true;

    public FullArrangementIterator(int n) {
        BoundI.checkPositive(n);
        this.n = n;
        this.a = IntegerArrays.arithmeticSequence(n, 0, 1);
        this.b = BooleanArrays.filledArray(n, false);
    }

    @Override
    public boolean hasNext() {
        return hasNextFlag;
    }

    @Override
    public int[] next() {
        int p = n - 1;
        while (p >= 0 && inc1(p)) p--;
        hasNextFlag = p >= 0;
        while (++p < n) inc2(p);
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

    public int disOrderCount() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (a[i] > a[j]) {
                    sum++;
                }
            }
        }
        return sum;
    }
}
