package indi.hiro.common.lang;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public final class MtDouble implements DoubleSupplier, DoubleConsumer, java.io.Serializable {

    private static final long serialVersionUID = 0x01192250C6B29B2DL;

    public double value;

    public MtDouble() {

    }

    public MtDouble(double value) {
        this.value = value;
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public void accept(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MtDouble && ((MtDouble) obj).value == value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
