package indi.hiro.common.ds.generic;

/**
 * Created by Hiro on 2018/10/31.
 */
public interface ReadArray<T> {

    int size();

    T get(int index);
}
