package indi.hiro.common.ds.generic;

import java.util.function.Supplier;

public class CyclicPool<T> implements Supplier<T> {

    private final T[] array;

    private final Supplier<T> constructor;

    private final int bitSize, bitMask;

    private volatile int ptr;

    public CyclicPool(T[] array, Supplier<T> constructor) {
        this.array = array;
        this.constructor = constructor;
        this.bitSize = Integer.SIZE - 1 - Integer.numberOfLeadingZeros(array.length);
        this.bitMask = (1 << bitSize) - 1;
    }

    @Override
    public synchronized T get() {
        if (ptr <= bitMask) {
            return array[ptr++] = constructor.get();
        } else {
            int im = ptr++ & bitMask;
            if (im == 0) {
                array[(ptr >> bitSize) & bitMask] = constructor.get();
            }
            return array[im];
        }
    }

    public int getPtr() {
        return ptr;
    }
}
