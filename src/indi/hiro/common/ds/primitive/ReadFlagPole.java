package indi.hiro.common.ds.primitive;

/**
 * Created by Hiro on 2018/11/12.
 */
public interface ReadFlagPole {

    int getFlagValue();

    boolean hasFlag(int f);

    boolean hasFlags(int f);

    boolean hasFlagByIndex(int i);
}
