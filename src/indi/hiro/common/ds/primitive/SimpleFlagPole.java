package indi.hiro.common.ds.primitive;

import indi.hiro.common.ds.primitive.FlagPole;

public class SimpleFlagPole implements FlagPole, java.io.Serializable {

    private static final long serialVersionUID = 0xC0A765D4CBB10F94L;

    private int mf;

    @Override
    public int getFlagValue() {
        return mf;
    }

    @Override
    public final boolean hasFlag(int f) {
        return (mf & f) != 0;
    }

    @Override
    public final boolean hasFlags(int f) {
        return (mf | ~f) == 0xFFFFFFFF;
    }

    @Override
    public boolean hasFlagByIndex(int i) {
        return (mf & (1 << i)) != 0;
    }

    @Override
    public void setFlagValue(int f) {
        mf = f;
    }

    @Override
    public void addFlag(int f) {
        mf |= f;
    }

    @Override
    public void addFlagByIndex(int i) {
        mf |= (1 << i);
    }

    @Override
    public void removeFlag(int f) {
        mf &= ~f;
    }

    @Override
    public void removeFlagByIndex(int i) {
        mf &= ~(1 << i);
    }
}
