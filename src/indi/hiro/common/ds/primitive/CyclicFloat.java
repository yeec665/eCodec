package indi.hiro.common.ds.primitive;

import indi.hiro.common.ds.util.EmptyStructureException;
import indi.hiro.common.math.basic.BoundI;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class CyclicFloat {

    final float[] array;

    final int length;

    int start = 0, end = 0, ctr = 0;

    public CyclicFloat(int n) {
        BoundI.checkPositive(n);
        array = new float[n];
        length = n;
    }

    public int size() {
        int size = end - start;
        if (size < 0) {
            size += length;
        }
        return size;
    }

    public int ctrStart() {
        return ctr - size();
    }

    public int ctrEnd() {
        return ctr;
    }

    public float removeFirst() {
        if (start == end) {
            throw new EmptyStructureException();
        }
        float removed = array[start];
        onFloatRemoved(start++);
        if (start == length) {
            start = 0;
        }
        return removed;
    }

    public void addLast(float f) {
        array[end] = f;
        onFloatAdded(end++);
        ctr++;
        if (end == length) {
            end = 0;
        }
        if (start == end) {
            onFloatRemoved(start++);
            if (start == length) {
                start = 0;
            }
        }
    }

    public void onFloatAdded(int i) {

    }

    public void onFloatRemoved(int i) {

    }

    public void iterate(FloatConsumer consumer) {
        if (start <= end) {
            for (int i = start; i < end; i++) {
                consumer.accept(array[i]);
            }
        } else {
            for (int i = start; i < length; i++) {
                consumer.accept(array[i]);
            }
            for (int i = 0; i < end; i++) {
                consumer.accept(array[i]);
            }
        }
    }

    public Path2D.Float toPath(Rectangle2D.Float rect, float srcMin, float srcMax) {
        int size = size();
        if (size < 2) {
            return new Path2D.Float();
        }
        Path2D.Float path = new Path2D.Float(Path2D.WIND_NON_ZERO, size);
        float mx = rect.width / (size - 1);
        float my = rect.height / (srcMax - srcMin);
        path.moveTo(rect.x, rect.y + my * (array[start] - srcMin));
        if (start <= end) {
            for (int i = start + 1, j = 1; i < end; i++) {
                path.lineTo(rect.x + mx * j++, rect.y + my * (array[i] - srcMin));
            }
        } else {
            int j = 1;
            for (int i = start + 1; i < length; i++) {
                path.lineTo(rect.x + mx * j++, rect.y + my * (array[i] - srcMin));
            }
            for (int i = 0; i < end; i++) {
                path.lineTo(rect.x + mx * j++, rect.y + my * (array[i] - srcMin));
            }
        }
        return path;
    }
}
