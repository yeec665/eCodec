package indi.hiro.common.lang;

import java.util.Comparator;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public final class MtInteger implements IntSupplier, IntConsumer, java.io.Serializable {

    private static final long serialVersionUID = 0x0B4185B5655B9F79L;

    public static final Comparator<MtInteger> COMPARATOR = Comparator.comparingInt(a -> a.value);
    public static final Comparator<MtInteger> INV_COMPARATOR = (a, b) -> Integer.compare(b.value, a.value);

    public int value;

    public MtInteger() {

    }

    public MtInteger(int value) {
        this.value = value;
    }

    public void pp() {
        value++;
    }

    public int ppL() {
        return ++value;
    }

    public int ppR() {
        return value++;
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public void accept(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MtInteger && ((MtInteger) obj).value == value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
