package indi.hiro.common.codec.compression;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

/**
 * Created by Hiro on 2018/11/2.
 */
public class ExtendedHuffmanData<D> {

    public static<S> ArrayList<ExtendedHuffmanData<S>> encode(Iterable<S> symbolSource) {
        ArrayList<ExtendedHuffmanData<S>> unitedList = new ArrayList<>();
        S lastS = null;
        int repeat = 0;
        for (S s : symbolSource) {
            if (lastS == null) {
                lastS = s;
                repeat = 1;
            } else {
                if (lastS.equals(s)) {
                    repeat++;
                } else {
                    unitedList.add(new ExtendedHuffmanData<>(lastS, repeat));
                    lastS = s;
                    repeat = 1;
                }
            }
        }
        if (lastS != null) {
            unitedList.add(new ExtendedHuffmanData<>(lastS, repeat));
        }
        return unitedList;
    }

    public final D symbol;

    public final int repeat;

    public ExtendedHuffmanData(@NotNull D symbol, int repeat) {
        this.symbol = symbol;
        this.repeat = repeat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == symbol) {
            return repeat == 1;
        }
        if (obj instanceof ExtendedHuffmanData) {
            ExtendedHuffmanData that = (ExtendedHuffmanData) obj;
            return symbol.equals(that.symbol) && repeat == that.repeat;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return symbol.hashCode() + 0x00F33A51 * repeat;
    }

    @Override
    public String toString() {
        return symbol + "*" + repeat;
    }
}
