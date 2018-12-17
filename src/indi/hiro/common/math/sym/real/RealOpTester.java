package indi.hiro.common.math.sym.real;

import indi.hiro.common.math.sym.nBasic.BcdParser;
import indi.hiro.common.math.sym.nBasic.FiniteFactorSet;

public class RealOpTester {

    public static void test() {
        System.out.println(FiniteFactorSet.tryFactorization(BcdParser.parseBcd("12345643250000000000")));
    }
}
