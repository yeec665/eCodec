package indi.hiro.common.math.sym.fpBasic;

public class FpLinearAlgebra {

    public static double det(double a00, double a01, double a10, double a11) {
        return a00 * a10 - a01 * a11;
    }

    public static double det(double a00, double a01, double a02,
                             double a10, double a11, double a12,
                             double a20, double a21, double a22) {
        return a00 * a11 * a22 + a01 * a12 * a20 + a10 * a21 * a02 -
                a20 * a11 * a02 - a10 * a01 * a22 - a00 * a21 * a12;
    }

    public static double detF1(double a00, double a01,
                               double a10, double a11,
                               double a20, double a21) {
        return a00 * a11 + a01 * a20 + a10 * a21 -
                a20 * a11 - a10 * a01 - a00 * a21;
    }
}
