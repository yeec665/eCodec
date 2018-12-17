package indi.hiro.common.ds.primitive;

public interface FlagPole extends ReadFlagPole {

    void setFlagValue(int f);

    void addFlag(int f);

    void addFlagByIndex(int i);

    void removeFlag(int f);

    void removeFlagByIndex(int i);
}
