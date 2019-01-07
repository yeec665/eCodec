package indi.hiro.common.codec.compression;

import com.sun.istack.internal.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/11/3.
 */
public class ExtendedHuffmanDataConsumer<D> implements Consumer<ExtendedHuffmanData<D>> {

    private final Consumer<D> innerConsumer;

    public ExtendedHuffmanDataConsumer(@NotNull Consumer<D> innerConsumer) {
        Objects.requireNonNull(innerConsumer);
        this.innerConsumer = innerConsumer;
    }

    @Override
    public void accept(ExtendedHuffmanData<D> hd) {
        D d = hd.symbol;
        for (int i = hd.repeat - 1; i >= 0; i--) {
            innerConsumer.accept(d);
        }
    }
}
