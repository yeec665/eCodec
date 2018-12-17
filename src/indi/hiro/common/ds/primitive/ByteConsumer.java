package indi.hiro.common.ds.primitive;

@FunctionalInterface
public interface ByteConsumer {

    void accept(byte value);

    default void acceptArray(byte[] value) {
        for (byte b : value) {
            accept(b);
        }
    }

    default void acceptArray(byte[] value, int off, int len) {
        for (len += off; off < len; off++) {
            accept(value[off]);
        }
    }
}
