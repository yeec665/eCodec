package indi.hiro.common.ds.generic;

import java.util.Iterator;

public interface WormConsumer2<T> {

    default void apply(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        T t1, t2;
        if (iterator.hasNext()) {
            t2 = iterator.next();
            consumeHead(t2);
            while (iterator.hasNext()) {
                t1 = t2;
                t2 = iterator.next();
                consumeBody(t1, t2);
            }
            consumeTail(t2);
        }
    }

    void consumeHead(T t);

    void consumeBody(T t1, T t2);

    void consumeTail(T t);
}
