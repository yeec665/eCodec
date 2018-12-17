package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.sym.unknown.Unknown;

import java.util.Arrays;

/**
 * Created by Hiro on 2018/9/21.
 */
public abstract class CommutableMvFunction extends MvFunction {

    final Expression[] x;

    private boolean sorted = false;

    public CommutableMvFunction(Expression[] x) {
        super(x.length);
        this.x = x;
    }

    public CommutableMvFunction(Expression[] x, int len) {
        super(len);
        if (len > x.length) {
            throw new IllegalArgumentException();
        }
        this.x = x;
    }

    @Override
    public Expression var(int i) {
        return x[i];
    }

    public Expression[] copyArray() {
        return Arrays.copyOf(x, nv);
    }

    @Override
    public boolean containsUnknown(Unknown u) {
        for (int i = 0; i < nv; i++) {
            if (x[i].containsUnknown(u)) {
                return true;
            }
        }
        return false;
    }

    public void sort() {
        if (!sorted) {
            sorted = true;
            Arrays.sort(x, 0, nv, ExpressionOp::compareOrder);
        }
    }

    @Override
    public int order(int i) {
        if (i == 1) {
            return nv;
        } else {
            i -= 2;
            sort();
            for (int j = 0; j < nv; j++) {
                int k = x[j].orderLength();
                if (i < k) {
                    return x[j].order(i);
                }
                i -= k;
            }
            return -1;
        }
    }

    @Override
    public int orderLength() {
        int len = 2;
        for (int j = 0; j < nv; j++) {
            len += x[j].orderLength();
        }
        return len;
    }
}
