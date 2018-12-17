package indi.hiro.common.math.sym.nBasic;

public class IntDivResult<T extends IntN> {

    public final T quotient;
    public final T remainder;

    public IntDivResult(T quotient, T remainder) {
        this.quotient = quotient;
        this.remainder = remainder;
    }

    @Override
    public String toString() {
        return quotient + "..." + remainder;
    }
}
