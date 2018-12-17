package indi.hiro.common.math.sym.nBasic;

public class FinitePrimeFactor {

    public final FinitePrimeLink factor;
    private int appearance = 1;

    public FinitePrimeFactor(FinitePrimeLink factor) {
        this.factor = factor;
    }

    public FinitePrimeFactor(FinitePrimeLink factor, int n) {
        this.factor = factor;
        appearance = n;
    }

    public void inc() {
        appearance++;
    }

    public void inc(int n) {
        appearance += n;
    }

    public void dec() {
        appearance--;
    }

    public boolean positive() {
        return appearance >= 0;
    }

    public int count() {
        return appearance;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FinitePrimeFactor && equalMe((FinitePrimeFactor) obj);
    }

    public boolean equalMe(FinitePrimeFactor that) {
        return factor == that.factor && appearance == that.appearance;
    }

    @Override
    public String toString() {
        return "FinitePrimeFactor[" + factor + ", " + appearance + "]";
    }
}
