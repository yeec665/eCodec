package indi.hiro.common.math.sym.fpBasic;

import java.io.Serializable;
import java.util.Arrays;

public final class PowerArrayF implements Serializable {

    public static void classTest() {
        PowerArrayF powerArrayF = new PowerArrayF(0.9f);
        System.out.println(powerArrayF);
        System.out.println(powerArrayF.power(10));
        System.out.println(powerArrayF.power(100));
        System.out.println(powerArrayF.power(1000));
    }

    private final float[] powerArray;
    private final boolean negativeInf;

    public PowerArrayF(float f) {
        float[] tArray = new float[32];
        int i = 0;
        for (; i < 32; i++) {
            tArray[i] = f;
            if (f == 0 || Float.isInfinite(f)) {
                i++;
                break;
            }
            f *= f;
        }
        if (i < 32) {
            tArray = Arrays.copyOf(tArray, i);
        }
        powerArray = tArray;
        negativeInf = f < -1.0f;
    }

    public float power(int n) {
        int i = 32 - Integer.numberOfLeadingZeros(n);
        if (i >= powerArray.length) {
            if (negativeInf && (n & 1) != 0) {
                return Float.NEGATIVE_INFINITY;
            } else {
                return powerArray[powerArray.length - 1];
            }
        }
        float f = 1.0f;
        for (int j = 0; j < i; j++) {
            if (((1 << j) & n) != 0) {
                f *= powerArray[j];
            }
        }
        return f;
    }

    @Override
    public String toString() {
        return "PowerArray[base = " + powerArray[0] + ", len = " + powerArray.length + "]";
    }
}
