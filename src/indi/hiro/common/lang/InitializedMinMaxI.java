package indi.hiro.common.lang;

import java.util.function.IntConsumer;

public class InitializedMinMaxI implements IntConsumer {

    private int startMin, startMax;
    private int min, max;

    public InitializedMinMaxI(int startMin, int startMax) {
        this.startMin = startMin;
        this.startMax = startMax;
        reset();
    }

    public void reset() {
        min = startMin;
        max = startMax;
    }

    @Override
    public void accept(int value) {
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getRange() {
        return max - min;
    }
}
