package indi.hiro.common.ds.primitive;

public class MaxHolderCyclicFloat extends CyclicFloat {
    
    private float defaultMax = 0;
    private float max;
    private int maxI;
    private boolean maxHeld = false;
    
    public MaxHolderCyclicFloat(int n) {
        super(n);
    }

    public void setDefaultMax(float defaultMax) {
        this.defaultMax = defaultMax;
    }

    public float max() {
        if (!maxHeld) {
            sweep();
        }
        return max;
    }

    private void sweep() {
        max = defaultMax;
        if (start <= end) {
            for (int i = start; i < end; i++) {
                meetValue(array[i]);
            }
        } else {
            for (int i = start; i < length; i++) {
                meetValue(array[i]);
            }
            for (int i = 0; i < end; i++) {
                meetValue(array[i]);
            }
        }
        maxHeld = true;
    }
    
    private void meetValue(float f) {
        if (f > max) {
            max = f;
        }
    }

    @Override
    public void onFloatAdded(int i) {
        float f = array[i];
        if (maxHeld && f >= max) {
            maxI = i;
            max = f;
        }
    }

    @Override
    public void onFloatRemoved(int i) {
        if (maxHeld && i == maxI) {
            maxHeld = false;
        }
    }
}
