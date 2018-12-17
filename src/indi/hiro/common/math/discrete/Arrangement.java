package indi.hiro.common.math.discrete;

import indi.hiro.common.math.basic.BoundI;
import indi.hiro.common.lang.IntegerArrays;

import java.util.Iterator;

public class Arrangement implements Iterable<int[]> {

    private int n, m;

    public Arrangement(int n) {
        if (!(n > 0)) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.m = n;
    }

    public Arrangement(int n, int m) {
        if (!(n > 0 && m > 0 && n >= m)) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.m = m;
    }

    public int count() {
        int sum = n;
        for (int i = n - m + 1; i < n; i++) {
            sum *= i;
        }
        return sum;
    }

    public void print() {
        for (int[] a : this) {
            System.out.println(IntegerArrays.toString(a));
        }
    }

    @Override
    public Iterator<int[]> iterator() {
        if (n == m) {
            return new FullArrangementIterator(n);
        } else {
            return new ArrangementIterator(n, m);
        }
    }
}
