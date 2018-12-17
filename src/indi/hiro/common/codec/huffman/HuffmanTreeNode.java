package indi.hiro.common.codec.huffman;

import indi.hiro.common.ds.primitive.CharCounter;
import indi.hiro.common.ds.primitive.IntCounter;

import java.util.ArrayList;
import java.util.Comparator;

public class HuffmanTreeNode<D> {

    public static ArrayList<HuffmanTreeNode<Integer>> buildFromIC(IntCounter ic) {
        ArrayList<HuffmanTreeNode<Integer>> list = new ArrayList<>();
        ic.iterateII((a, b) -> list.add(new HuffmanTreeNode<>(a, b)));
        return list;
    }

    public static ArrayList<HuffmanTreeNode<Character>> buildFromCC(CharCounter cc) {
        ArrayList<HuffmanTreeNode<Character>> list = new ArrayList<>();
        cc.iterateII((a, b) -> list.add(new HuffmanTreeNode<>(a, b)));
        return list;
    }

    public static final Comparator<HuffmanTreeNode> FREQUENCY_COMPARATOR = Comparator.comparingInt(x -> x.frequency);

    D symbol;

    int frequency;

    HuffmanTreeNode<D> childL, childR;

    public HuffmanTreeNode(D symbol, int frequency) {
        this.symbol = symbol;
        this.frequency = frequency;
    }

    public HuffmanTreeNode(HuffmanTreeNode<D> childL, HuffmanTreeNode<D> childR) {
        this.frequency = childL.frequency + childR.frequency;
        this.childL = childL;
        this.childR = childR;
    }

    public int getFrequency() {
        return frequency;
    }
}
