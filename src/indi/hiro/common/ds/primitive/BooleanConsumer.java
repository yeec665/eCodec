package indi.hiro.common.ds.primitive;

/**
 * Created by Hiro on 2018/10/30.
 */
@FunctionalInterface
public interface BooleanConsumer {

    void accept(boolean value);

    default void acceptBB(BooleanBuilder bb) {
        bb.supplyTo(this);
    }

    default void acceptBB(BooleanBuilder bb, int off, int len) {
        bb.supplyTo(this, off, len);
    }
}
