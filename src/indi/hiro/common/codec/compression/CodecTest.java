package indi.hiro.common.codec.compression;

import indi.hiro.common.ds.generic.LinearCounter;
import indi.hiro.common.ds.primitive.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hiro on 2018/10/18.
 */
public class CodecTest {

    public static void main(String[] args) {
        testAll();
        EntropyAnalyzer.testAll();
    }

    public static void testAll() {
        testHuffman("HELLO");
        testAdaptiveHuffman("AADCCDD");
        testLzw("ABABBABCABABBA");
        testArithmetic("CAEE");
        testCodec5("ABCDC ABBBC BBBAC BCBBB ACDBB CCABD BBBDD BBBAB DBBAC");
    }

    private static void testCodec5(String msg) {
        System.out.println();
        System.out.println("Encoding/decoding test using all 5 codecs on symbol sequence : ");
        System.out.println(msg);
        System.out.println("1.Huffman :");
        testHuffman(msg);
        System.out.println("2.Extended Huffman :");
        testExtendedHuffman(msg);
        System.out.println("3.Adaptive Huffman :");
        testAdaptiveHuffman(msg);
        System.out.println("4.LZW (dictionary) :");
        testLzw(msg);
        System.out.println("5.Arithmetic :");
        testArithmetic(msg);
    }

    private static void testHuffman(String msg) {
        HuffmanTree<Character> huffmanTree = new HuffmanTree<>(HuffmanTreeNode.buildFromCC(new CharCounter(msg)));
        HuffmanTable<Character> huffmanTable = new HuffmanTable<>(huffmanTree);
        System.out.println(huffmanTable);
        BooleanBuilder code = huffmanTable.encode(new StringAsCharacterIterable(msg));
        System.out.print("Encoded : ");
        System.out.println(code);
        StringBuilder result = new StringBuilder();
        huffmanTree.decode(code, new StringBuilderAsCharacterConsumer(result));
        System.out.print("Decoded : ");
        System.out.println(result);
    }

    private static void testExtendedHuffman(String msg) {
        ArrayList<ExtendedHuffmanData<Character>> huffmanData = ExtendedHuffmanData.encode(new StringAsCharacterIterable(msg));
        LinearCounter<ExtendedHuffmanData<Character>> linearCounter = new LinearCounter<>();
        linearCounter.acceptAll(huffmanData);
        HuffmanTree<ExtendedHuffmanData<Character>> huffmanTree = new HuffmanTree<>(linearCounter.toHuffmanSegments());
        HuffmanTable<ExtendedHuffmanData<Character>> huffmanTable = new HuffmanTable<>(huffmanTree);
        System.out.println(huffmanTable);
        BooleanBuilder code = (new HuffmanTable<>(huffmanTree)).encode(huffmanData);
        System.out.print("Encoded : ");
        System.out.println(code);
        StringBuilder result = new StringBuilder();
        huffmanTree.decode(code, new ExtendedHuffmanDataConsumer<>(new StringBuilderAsCharacterConsumer(result)));
        System.out.print("Decoded : ");
        System.out.println(result);
    }

    private static void testAdaptiveHuffman(String msg) {
        InitialCodeAssignment<Character> initialCodeAssignment = new InitialCodeAssignment<>(InitialCodeAssignment.dictionary(msg), 5, false);
        AdaptiveHuffmanTree<Character> adaptiveHuffmanTree = new AdaptiveHuffmanTree<>(initialCodeAssignment);
        BooleanBuilder code = adaptiveHuffmanTree.encode(new StringAsCharacterIterable(msg));
        System.out.print("Encoded : ");
        System.out.println(code);
        System.out.println(adaptiveHuffmanTree);
        StringBuilder result = new StringBuilder();
        adaptiveHuffmanTree.resetTree();
        adaptiveHuffmanTree.decode(code, new StringBuilderAsCharacterConsumer(result));
        System.out.print("Decoded : ");
        System.out.println(result);
        System.out.println(adaptiveHuffmanTree);
    }

    private static void testLzw(String msg) {
        List<Character> commonDictionary = InitialCodeAssignment.dictionary(msg);
        CompressionDictionary<Character> compressionDictionary = new CompressionDictionary<>(commonDictionary);
        ArrayList<Integer> code = compressionDictionary.encode(new StringAsCharacterIterable(msg));
        System.out.print("Encoded : ");
        System.out.println(code);
        System.out.println(compressionDictionary.getConstructedDictionary());
        StringBuilder result = new StringBuilder();
        DecompressionDictionary<Character> decompressionDictionary = new DecompressionDictionary<>(commonDictionary);
        decompressionDictionary.decode(code, new StringBuilderAsCharacterConsumer(result));
        System.out.print("Decoded : ");
        System.out.println(result);
        System.out.println(decompressionDictionary.getConstructedDictionary());
    }

    private static void testArithmetic(String msg) {
        ArithmeticSequence<Character> arithmeticSequence = ArithmeticSequence.buildFromCC(new CharCounter(msg), true, true);
        System.out.println(arithmeticSequence);
        BooleanBuilder code = arithmeticSequence.encode(new StringAsCharacterIterable(msg));
        System.out.print("Encoded : ");
        System.out.println(code);
        StringBuilder result = new StringBuilder();
        arithmeticSequence.decode(code, new StringBuilderAsCharacterConsumer(result));
        System.out.print("Decoded : ");
        System.out.println(result);
    }
}
