package indi.hiro.common.ds.generic;

import java.util.function.Consumer;
import java.util.function.ToIntFunction;

/**
 * Created by Hiro on 2018/11/2.
 */
public interface GenericCounter<T> extends Consumer<T>, ToIntFunction<T> {

    int size();

    int iSum();

    @Override
    void accept(T x);

    void accept(T x, int n);

    void acceptAll(Iterable<T> xi);

    class OiPair {
        public Object k;
        public int v;

        public OiPair(Object k, int v) {
            this.k = k;
            this.v = v;
        }
    }
}
