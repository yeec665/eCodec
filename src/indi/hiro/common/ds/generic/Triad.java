package indi.hiro.common.ds.generic;

import indi.hiro.common.ds.util.DsToStringUtil;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Hiro on 2018/11/19.
 */
public class Triad<T> {

    public T a, b, c;

    public Triad() {

    }

    public Triad(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(DsToStringUtil.nameFor(getClass()));
        sb.append(DsToStringUtil.PAIR_LEFT_BRACKET);
        sb.append(a);
        sb.append(DsToStringUtil.PAIR_ELEMENT_SEP);
        sb.append(b);
        sb.append(DsToStringUtil.PAIR_ELEMENT_SEP);
        sb.append(c);
        return sb.append(DsToStringUtil.PAIR_RIGHT_BRACKET).toString();
    }

    public static class WormIterator<T> extends Triad<T> implements Iterable<Triad<T>>, Iterator<Triad<T>> {

        final Iterable<T> srcIterable;

        Iterator<T> srcIterator;

        public WormIterator(Iterable<T> srcIterable) {
            this.srcIterable = srcIterable;
        }

        @Override
        public Iterator<Triad<T>> iterator() {
            srcIterator = srcIterable.iterator();
            b = srcIterator.hasNext() ? srcIterator.next() : null;
            c = srcIterator.hasNext() ? srcIterator.next() : null;
            return this;
        }

        @Override
        public boolean hasNext() {
            return srcIterator.hasNext();
        }

        @Override
        public Triad<T> next() {
            a = b;
            b = c;
            c = srcIterator.next();
            return this;
        }

    }

    public static class SnakeIterator<T> extends Triad<T> implements Iterable<Triad<T>>, Iterator<Triad<T>> {

        final Iterable<T> srcIterable;

        Iterator<T> srcIterator;

        T t0, t1;

        public SnakeIterator(Iterable<T> srcIterable) {
            this.srcIterable = srcIterable;
        }

        @Override
        public Iterator<Triad<T>> iterator() {
            srcIterator = srcIterable.iterator();
            t0 = b = srcIterator.hasNext() ? srcIterator.next() : null;
            t1 = c = srcIterator.hasNext() ? srcIterator.next() : null;
            return this;
        }

        @Override
        public boolean hasNext() {
            return c != t1 || srcIterator.hasNext();
        }

        @Override
        public Triad<T> next() {
            a = b;
            b = c;
            if (srcIterator.hasNext()) {
                c = srcIterator.next();
            } else if (c != t0) {
                c = t0;
            } else if (c != t1) {
                c = t1;
            } else {
                throw new NoSuchElementException();
            }
            return this;
        }
    }

    public interface WormConsumer<T> {

        default void apply(Iterable<T> iterable) {
            Iterator<T> iterator = iterable.iterator();
            T t1, t2, t3;
            if (!iterator.hasNext()) {
                return;
            }
            t3 = iterator.next();
            consumeHead(t3);
            if (!iterator.hasNext()) {
                consumeTail(t3);
                return;
            }
            t2 = t3;
            t3 = iterator.next();
            consumeNeck(t2, t3);
            while (iterator.hasNext()) {
                t1 = t2;
                t2 = t3;
                t3 = iterator.next();
                consumeBody(t1, t2, t3);
            }
            consumeBottom(t2, t3);
            consumeTail(t3);
        }

        void consumeHead(T t);

        void consumeNeck(T t1, T t2);

        void consumeBody(T t1, T t2, T t3);

        void consumeBottom(T t1, T t2);

        void consumeTail(T t);
    }
}