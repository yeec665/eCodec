package indi.hiro.common.math.sym.real;

import indi.hiro.common.math.sym.fpBasic.DoubleWithE;
import indi.hiro.common.math.sym.ui.ShownOp;
import indi.hiro.common.math.sym.ui.ShownString;

/**
 * Created by Hiro on 2018/9/21.
 */
public class Euler implements Irrational {

    public static final DoubleWithE DWE = new DoubleWithE(Math.E);

    @Override
    public int productOrder() {
        return 5;
    }

    @Override
    public double dValue() {
        return Math.E;
    }

    @Override
    public DoubleWithE dwe() {
        return DWE;
    }

    @Override
    public int order(int i) {
        return 12;
    }

    @Override
    public int orderLength() {
        return 1;
    }

    @Override
    public void debugToString(StringBuilder sb) {
        sb.append('e');
    }

    @Override
    public ShownOp show() {
        return new ShownString("e");
    }
}
