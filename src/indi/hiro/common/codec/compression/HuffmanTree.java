package indi.hiro.common.codec.compression;

import indi.hiro.common.ds.primitive.BooleanBuilder;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/10/17.
 */
public class HuffmanTree<D> {

    HuffmanTreeNode<D> root;

    public HuffmanTree(ArrayList<HuffmanTreeNode<D>> segments) {
        while (segments.size() > 1) {
            segments.sort(HuffmanTreeNode.FREQUENCY_COMPARATOR);
            segments.add(new HuffmanTreeNode<>(segments.remove(0), segments.remove(0)));
        }
        root = segments.get(0);
    }

    public void decode(BooleanBuilder msg, Consumer<D> out) {
        HuffmanTreeNode<D> node = root;
        try {
            for (int i = 0, n = msg.bitLen(); i < n; i++) {
                if (msg.get(i)) {
                    node = node.childR;
                } else {
                    node = node.childL;
                }
                if (node.symbol != null) {
                    out.accept(node.symbol);
                    node = root;
                }
            }
        } catch (NullPointerException e) {
            if (CodeCorruptedException.STRICT_MODE) {
                throw new CodeCorruptedException();
            }
        }
        if (node != root && CodeCorruptedException.STRICT_MODE) {
            throw new CodeCorruptedException();
        }
    }
}
