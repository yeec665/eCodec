package indi.hiro.common.lang;

public class IntegerExponentSeriesF {

    public static float pow(float a, int x) {
        if (x <= 0) {
            if (x == 0) {
                return 1.0f;
            }
            throw new ArithmeticException();
        }
        int p = Integer.SIZE - Integer.numberOfLeadingZeros(x);
        float y = 1.0f;
        for (int i = 0; i < p; i++) {
            if ((x & (1 << i)) != 0) {
                y *= a;
            }
            a *= a;
        }
        return y;
    }

    private final float[] values = new float[Integer.SIZE];

    /**
     * y = Math.pow(a, x);
     */
    public IntegerExponentSeriesF(float a) {
        for (int i = 0; i < values.length; i++) {
            values[i] = a;
            a *= a;
        }
    }

    public float f(int x) {
        if (x <= 0) {
            if (x == 0) {
                return 1.0f;
            }
            throw new ArithmeticException();
        }
        int p = Integer.SIZE - 1 - Integer.numberOfLeadingZeros(x);
        float y = values[p];
        for (p--; p >= 0; p--) {
            if ((x & (1 << p)) != 0) {
                y *= values[p];
            }
        }
        return y;
    }

    public static void classTest() {
        final float FE = (float) Math.E;
        IntegerExponentSeriesF ies = (new IntegerExponentSeriesF(FE));
        for (int i = 0; i < 100; i++) {
            System.out.print((float) Math.exp(i));
            System.out.print(" = ");
            System.out.print(ies.f(i));
            System.out.print(" = ");
            System.out.println(pow(FE, i));
        }
    }
}
