package indi.hiro.common.math.sym.function;

import indi.hiro.common.math.basic.BoundI;

/**
 * Created by Hiro on 2018/9/21.
 */
public abstract class MvFunction implements Function {

    public final int nv;

    public MvFunction(int nv) {
        BoundI.checkPositive(nv);
        this.nv = nv;
    }

    public abstract MvFunction replicate(Expression[] x);

    @Override
    public int nVar() {
        return nv;
    }
}
