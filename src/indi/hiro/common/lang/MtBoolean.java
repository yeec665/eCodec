package indi.hiro.common.lang;

import java.util.function.BooleanSupplier;

public final class MtBoolean implements BooleanSupplier, java.io.Serializable {

    private static final long serialVersionUID = 0x0319F6F514C45FECL;

    public boolean value;

    public MtBoolean() {

    }

    public MtBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public boolean getAsBoolean() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MtBoolean && ((MtBoolean) obj).value == value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
