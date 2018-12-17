package indi.hiro.common.ds.primitive;

import indi.hiro.common.math.basic.IndexBound;

/**
 * Created by Hiro on 2018/12/2.
 */
public interface IndexedDouble {

    int size();

    double getDouble(int i);

    double setDouble(double d, int i);

    static IndexedDouble decimate(double[] array, int off, int dec, int lim) {
        int len = array.length;
        int end = off + dec * lim;
        if (!(IndexBound.in(off, len) && IndexBound.in(end, len))) {
            throw new IllegalStateException();
        }
        if (off == 0 && dec == 1 && lim == len) {
            return new WrapOfArray(array);
        }
        if (!IndexBound.in(off - dec, len) && IndexBound.in(end + dec, len)) {
            return new DecimationOfArrayNoCheck(array, off, dec, lim);
        }
        if (off == 0) {
            return new DecimationOfArrayFormZero(array, dec, lim);
        }
        return new DecimationOfArray(array, off, dec, lim);
    }

    static IndexedDouble decimateToMaximum(double[] array, int off, int dec) {
        int len = array.length;
        if (!IndexBound.in(off, len)) {
            throw new IllegalStateException();
        }
        if (off == 0 && dec == 1) {
            return new WrapOfArray(array);
        }
        int lim;
        if (dec > 0) {
            lim = (len - off + dec - 1) / dec;
        } else if (dec < 0) {
            lim = (off - dec - 1) / -dec;
        } else {
            lim = 1;
        }
        if (!IndexBound.in(off - dec, len)) {
            return new DecimationOfArrayNoCheck(array, off, dec, lim);
        }
        if (off == 0) {
            return new DecimationOfArrayFormZero(array, dec, lim);
        }
        return new DecimationOfArray(array, off, dec, lim);
    }

    class WrapOfArray implements IndexedDouble {

        final double[] array;

        WrapOfArray(double[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public double getDouble(int i) {
            return array[i];
        }

        @Override
        public double setDouble(double d, int i) {
            double oldValue = array[i];
            array[i] = d;
            return oldValue;
        }
    }

    class DecimationOfArray implements IndexedDouble {

        final double[] array;

        final int off, dec, lim;

        DecimationOfArray(double[] array, int off, int dec, int lim) {
            this.array = array;
            this.off = off;
            this.dec = dec;
            this.lim = lim;
        }

        @Override
        public int size() {
            return lim;
        }

        @Override
        public double getDouble(int i) {
            if (i < 0 || i >= lim) {
                throw new IndexOutOfBoundsException();
            }
            return array[off + dec * i];
        }

        @Override
        public double setDouble(double d, int i) {
            if (i < 0 || i >= lim) {
                throw new IndexOutOfBoundsException();
            }
            i = off + dec * i;
            double oldValue = array[i];
            array[i] = d;
            return oldValue;
        }
    }

    class DecimationOfArrayNoCheck implements IndexedDouble {

        final double[] array;

        final int off, dec, lim;

        DecimationOfArrayNoCheck(double[] array, int off, int dec, int lim) {
            this.array = array;
            this.off = off;
            this.dec = dec;
            this.lim = lim;
        }

        @Override
        public int size() {
            return lim;
        }

        @Override
        public double getDouble(int i) {
            return array[off + dec * i];
        }

        @Override
        public double setDouble(double d, int i) {
            i = off + dec * i;
            double oldValue = array[i];
            array[i] = d;
            return oldValue;
        }
    }

    class DecimationOfArrayFormZero implements IndexedDouble {

        final double[] array;

        final int dec, lim;

        DecimationOfArrayFormZero(double[] array, int dec, int lim) {
            this.array = array;
            this.dec = dec;
            this.lim = lim;
        }

        @Override
        public int size() {
            return lim;
        }

        @Override
        public double getDouble(int i) {
            if (i >= lim) {
                throw new IndexOutOfBoundsException();
            }
            return array[dec * i];
        }

        @Override
        public double setDouble(double d, int i) {
            if (i >= lim) {
                throw new IndexOutOfBoundsException();
            }
            i *= dec;
            double oldValue = array[i];
            array[i] = d;
            return oldValue;
        }
    }
}
