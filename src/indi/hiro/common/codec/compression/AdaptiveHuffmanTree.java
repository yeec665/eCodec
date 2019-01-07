package indi.hiro.common.codec.compression;

import com.sun.istack.internal.NotNull;
import indi.hiro.common.ds.primitive.BooleanBuilder;

import java.util.function.Consumer;

/**
 * Created by Hiro on 2018/11/4.
 * Adaptive Huffman Coding: statistics are gathered and updated dynamically as the data stream arrives.
 */
public class AdaptiveHuffmanTree<D> {

    private static final boolean SHOW_PROCEDURE = false;

    final InitialCodeAssignment<D> initialCode;

    HuffmanTreeNode<D> root;

    /**
     * Temp node
     */
    HuffmanTreeNode<D> markNodeP, markNode;

    public AdaptiveHuffmanTree(InitialCodeAssignment<D> initialCode) {
        this.initialCode = initialCode;
        resetTree();
    }

    public void resetTree() {
        root = new HuffmanTreeNode<>(null, 0); // NEW node
    }

    public BooleanBuilder encode(Iterable<D> symbolSource) {
        BooleanBuilder out = BooleanBuilder.createEmpty();
        out.append(false);
        for (D d : symbolSource) {
            if (SHOW_PROCEDURE) {
                System.out.println("Incoming symbol : " + d);
            }
            BooleanBuilder codeToD = searchD(d, root, null);
            if (codeToD != null) {
                out.append(codeToD, true);
                markNode.frequency++;
                checkTree();
            } else {
                BooleanBuilder codeToNew = searchNew(root, null);
                if (codeToNew == null) {
                    throw new CodeCorruptedException();
                }
                out.append(codeToNew, true);
                initialCode.appendCode(d, out);
                if (markNodeP == null) {
                    root = new HuffmanTreeNode<>(root, new HuffmanTreeNode<>(d, 1));
                } else {
                    markNodeP.childL = new HuffmanTreeNode<>(markNode, new HuffmanTreeNode<>(d, 1));
                }
            }
            if (SHOW_PROCEDURE) {
                System.out.println("Current code : " + out);
                System.out.println("Tree root : " + root);
            }
        }
        return out;
    }

    public void decode(BooleanBuilder msg, Consumer<D> out) {
        try {
            HuffmanTreeNode<D> node = root, nodeP = null;
            int ptr = 1;
            while (true) {
                if (node.symbol != null) {
                    out.accept(node.symbol);
                    node.frequency++;
                    checkTree();
                    node = root;
                    nodeP = null;
                } else if (node.childL != null && node.childR != null) {
                    if (ptr < msg.bitLen()) {
                        nodeP = node;
                        if (msg.get(ptr++)) {
                            node = node.childR;
                        } else {
                            node = node.childL;
                        }
                    } else {
                        break;
                    }
                } else {
                    D newSymbol = initialCode.readCode(msg, ptr);
                    ptr += initialCode.getBitLen();
                    out.accept(newSymbol);
                    if (nodeP == null) {
                        root = new HuffmanTreeNode<>(root, new HuffmanTreeNode<>(newSymbol, 1));
                    } else {
                        nodeP.childL = new HuffmanTreeNode<>(node, new HuffmanTreeNode<>(newSymbol, 1));
                    }
                    node = root;
                    nodeP = null;
                }
                if (SHOW_PROCEDURE) {
                    System.out.println("Symbol string : " + out);
                    System.out.println("Tree root : " + root);
                }
            }
        } catch (NullPointerException e) {
            if (CodeCorruptedException.STRICT_MODE) {
                throw new CodeCorruptedException();
            }
        }
    }

    /**
     * t1 : parent of the result
     * t2 : the result
     * @param node start point
     * @param nodeP parent of start point
     * @return path
     */
    private BooleanBuilder searchNew(HuffmanTreeNode<D> node, HuffmanTreeNode<D> nodeP) {
        if (node.symbol == null) {
            if (node.childL != null && node.childR != null) { // branch node
                BooleanBuilder bbL = searchNew(node.childL, node); // recursive search
                if (bbL != null) {
                    return bbL.append(false);
                }
                BooleanBuilder bbR = searchNew(node.childR, node); // recursive search
                if (bbR != null) {
                    return bbR.append(true);
                }
            } else { // NEW node
                markNodeP = nodeP;
                markNode = node;
                return BooleanBuilder.createEmpty();
            }
        }
        return null;
    }

    /**
     * t1 : parent of the result
     * t2 : the result
     * @param node start point
     * @param nodeP parent of start point
     * @return path
     */
    private BooleanBuilder searchD(@NotNull D d, HuffmanTreeNode<D> node, HuffmanTreeNode<D> nodeP) {
        if (node.symbol == null) {
            if (node.childL != null && node.childR != null) { // branch node
                BooleanBuilder bbL = searchD(d, node.childL, node);
                if (bbL != null) {
                    return bbL.append(false);
                }
                BooleanBuilder bbR = searchD(d, node.childR, node);
                if (bbR != null) {
                    return bbR.append(true);
                }
            }
        } else if (node.symbol.equals(d)) { // D node
            markNodeP = nodeP;
            markNode = node;
            return BooleanBuilder.createEmpty();
        }
        return null;
    }

    private int maxDepth(HuffmanTreeNode<D> node) {
        if (node.childL != null && node.childR != null) {
            return Math.max(maxDepth(node.childL), maxDepth(node.childR)) + 1;
        } else {
            return 1;
        }
    }

    private void checkTree() {
        while (true) {
            markNode = null;
            markNodeP = null;
            try {
                int layer = maxDepth(root) - 1;
                do {
                    if (SHOW_PROCEDURE) {
                        System.out.println("Visiting layer : " + layer);
                    }
                    visitLayer(layer, root, null);
                } while (--layer > 0);
                break;
            } catch (TreeSwapException ignored) {

            }
            if (SHOW_PROCEDURE) {
                System.out.println("After swap : " + root);
            }
        }
    }

    /**
     *
     * @param layer layer index, >= 0
     * @param node start point
     * @param nodeP parent of start point
     */
    private void visitLayer(int layer, HuffmanTreeNode<D> node, HuffmanTreeNode<D> nodeP) throws TreeSwapException {
        if (node.childL != null && node.childR != null) {
            if (layer == 0) {
                node.frequency = node.childL.frequency + node.childR.frequency;
                visitNode(node, nodeP);
            } else {
                layer--;
                visitLayer(layer, node.childL, node);
                visitLayer(layer, node.childR, node);
            }
        } else if (layer == 0) {
            visitNode(node, nodeP);
        }
    }

    private void visitNode(HuffmanTreeNode<D> node, HuffmanTreeNode<D> nodeP) throws TreeSwapException {
        if (SHOW_PROCEDURE) {
            System.out.println("Comparing : " + markNode + " <-> " + node);
        }
        if (markNode == null) {
            markNode = node;
            markNodeP = nodeP;
            return;
        }
        int d = node.frequency - markNode.frequency;
        if (d > 0) {
            markNode = node;
            markNodeP = nodeP;
        } else if (d < 0) {
            if (markNodeP.childL == markNode) {
                markNodeP.childL = node;
                if (nodeP.childR == node) {
                    nodeP.childR = markNode;
                } else {
                    nodeP.childL = markNode;
                }
            } else {
                markNodeP.childR = node;
                if (nodeP.childL == node) {
                    nodeP.childL = markNode;
                } else {
                    nodeP.childR = markNode;
                }
            }
            if (SHOW_PROCEDURE) {
                System.out.println("Swap : " + markNode + " <-> " + node);
            }
            throw new TreeSwapException();
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }

    private static class TreeSwapException extends RuntimeException {

    }
}
