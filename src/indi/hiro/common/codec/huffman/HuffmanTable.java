package indi.hiro.common.codec.huffman;

import indi.hiro.common.codec.SymbolNotFoundException;
import indi.hiro.common.ds.primitive.BooleanBuilder;

import java.util.HashMap;

/**
 * Created by Hiro on 2018/10/17.
 */
public class HuffmanTable<D> {

    private final HashMap<D, BooleanBuilder> table = new HashMap<>();

    public HuffmanTable(HuffmanTree<D> huffmanTree) {
        explore(huffmanTree.root, BooleanBuilder.createEmpty());
    }

    private void explore(HuffmanTreeNode<D> node, BooleanBuilder bb) {
        if (node.symbol != null) {
            table.put(node.symbol, bb);
        } else {
            explore(node.childL, bb.copy().append(false));
            explore(node.childR, bb.append(true));
        }
    }

    public BooleanBuilder encode(Iterable<D> symbolSource) {
        BooleanBuilder out = BooleanBuilder.createEmpty();
        for (D d : symbolSource) {
            BooleanBuilder bb = table.get(d);
            if (bb != null) {
                bb.supplyTo(out);
            } else if (SymbolNotFoundException.STRICT_MODE) {
                throw new SymbolNotFoundException();
            }
        }
        return out;
    }

    @Override
    public String toString() {
        return table.toString();
    }
}
