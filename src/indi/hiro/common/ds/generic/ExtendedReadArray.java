package indi.hiro.common.ds.generic;

/**
 * Created by Hiro on 2018/11/1.
 */
public interface ExtendedReadArray<T> extends ReadArray<T> {

    boolean contains(T t);

    int firstIndexOf(T t, int start);

    int firstIndexOf(T t);

    int lastIndexOf(T t, int start);

    int lastIndexOf(T t);
}
