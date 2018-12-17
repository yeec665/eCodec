package indi.hiro.common.ds.generic;

import java.util.Objects;

public abstract class ArrayFinal<T> implements Iterable<T> {

    final Class<T> classT;

    final T[] array;

    ArrayFinal(Class<T> classT, T[] array) {
        Objects.requireNonNull(classT);
        Objects.requireNonNull(array);
        this.classT = classT;
        this.array = array;
        if (array.length < 1) {
            throw new IllegalArgumentException();
        }
    }


}
