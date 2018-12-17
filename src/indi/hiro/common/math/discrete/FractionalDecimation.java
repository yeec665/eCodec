package indi.hiro.common.math.discrete;

import java.util.function.BooleanSupplier;

/**
 * Created by Hiro on 2018/12/3.
 */
public class FractionalDecimation implements BooleanSupplier {

    final int numerator, denominator;

    int i1, i2;

    public FractionalDecimation(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public void reset() {
        i1 = 0;
        i2 = 0;
    }

    @Override
    public boolean getAsBoolean() {
        i1 += numerator;
        if (i1 > i2) {
            i2 += denominator;
            if (i1 == i2) {
                reset();
            }
            return true;
        }
        return false;
    }
}
