package indi.hiro.common.codec.huffman;

import com.sun.istack.internal.NotNull;
import indi.hiro.common.codec.CodeCorruptedException;
import indi.hiro.common.ds.primitive.BooleanBuilder;
import sun.reflect.generics.tree.Tree;

/**
 * Created by Hiro on 2018/11/4.
 * Adaptive Huffman Coding: statistics are gathered and updated dynamically as the data stream arrives.
 */
public class AdaptiveHuffmanTree<D> {

    final InitialCodeAssignment<D> initialCode;

    HuffmanTreeNode<D> root;

    /**
     * Temp node
     */
    HuffmanTreeNode<D> t1, t2, t3;
    int visitN;

    public AdaptiveHuffmanTree(InitialCodeAssignment<D> initialCode) {
        this.initialCode = initialCode;
    }

    public void resetTree() {
        root = new HuffmanTreeNode<>(null, 0);
    }

    public BooleanBuilder encode(Iterable<D> symbolSource) {
        BooleanBuilder out = BooleanBuilder.createEmpty();
        for (D d : symbolSource) {
            BooleanBuilder bb = searchD(d, root, null);
            if (bb != null) {
                t2.frequency++;
                checkTree();
            } else {
                BooleanBuilder nBb = searchNew(root, null);
                if (nBb == null) {
                    throw new CodeCorruptedException();
                }
                nBb.supplyTo(out);
                initialCode.appendCode(d, out);
                if (t1 == null) {
                    root = new HuffmanTreeNode<>(root, new HuffmanTreeNode<>(d, 1));
                } else {
                    t1.childL = new HuffmanTreeNode<>(t2, new HuffmanTreeNode<>(d, 1));
                }
            }
        }
        return out;
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
            if (node.childL != null && node.childR != null) {
                BooleanBuilder bbL = searchNew(node.childL, node);
                if (bbL != null) {
                    return bbL.append(false);
                }
                BooleanBuilder bbR = searchNew(node.childR, node);
                if (bbR != null) {
                    return bbR.append(true);
                }
            } else {
                t1 = nodeP;
                t2 = node;
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
            if (node.childL != null && node.childR != null) {
                BooleanBuilder bbL = searchD(d, node.childL, node);
                if (bbL != null) {
                    return bbL.append(false);
                }
                BooleanBuilder bbR = searchD(d, node.childR, node);
                if (bbR != null) {
                    return bbR.append(true);
                }
            }
        } else if (node.symbol.equals(d)) {
            t1 = nodeP;
            t2 = node;
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
            try {
                int layer = 0;
                do {
                    visitN = 0;
                    layer++;
                    visitLayer(layer, root, null);
                } while (visitN > 0);
                break;
            } catch (TreeSwapException ignored) {

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
        if (layer <= 0) {
            if (t3 != null) {
                int d = node.frequency - t2.frequency;
                if (d < 0) {
                    t2 = node;
                    t1 = nodeP;
                } else if (d > 0) {
                    HuffmanTreeNode<D> t4;
                    if (t1.childL == t2) {
                        t4 = t1.childL;
                        t1.childL = node;
                        if (nodeP.childL == node) {
                            nodeP.childL = t4;
                        } else {
                            nodeP.childR = t4;
                        }
                    } else if (t1.childR == t2) {
                        t4 = t1.childR;
                        t1.childR = node;
                        if (nodeP.childL == node) {
                            nodeP.childL = t4;
                        } else {
                            nodeP.childR = t4;
                        }
                    }
                    throw new TreeSwapException();
                }
            }
            t3 = node;
            visitN++;
        } else if (node.childL != null && node.childR != null) {
            layer--;
            visitLayer(layer, node.childR, node);
            visitLayer(layer, node.childL, node);
        }
    }

    private static class TreeSwapException extends RuntimeException {

    }
}
