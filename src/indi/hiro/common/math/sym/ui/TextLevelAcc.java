package indi.hiro.common.math.sym.ui;

public class TextLevelAcc {

    private float[] moments = new float[4];
    private float min = Float.MAX_VALUE, max = 0;

    public TextLevelAcc() {

    }

    public void acc(float value, float times) {
        for (int i = 0; i < moments.length; i++) {
            moments[i] += times;
            times *= value;
        }
        min = Math.min(min, value);
        max = Math.max(max, value);
    }
}
