package indi.hiro.common.math.sym.nBasic;

public class MathExtension2 {

    public static int min(int a, int b, int c) {
        return a > b ? (b > c ? c : b) : (a > c ? c : a);
    }

    public static int max(int a, int b, int c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    public static int confine(int a, int b, int c) {
        return b < a ? a : (b > c ? c : b);
    }

    public static int ceilDivide(int a, int b) {
        return (a + b - 1) / b;
    }

    public static float min(float a, float b, float c) {
        return a > b ? (b > c ? c : b) : (a > c ? c : a);
    }

    public static float minAbs(float a, float b, float c) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        if (c < 0) c = -c;
        return a > b ? (b > c ? c : b) : (a > c ? c : a);
    }

    public static float max(float a, float b, float c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    public static float confine(float a, float b, float c) {
        return b < a ? a : (b > c ? c : b);
    }
}
