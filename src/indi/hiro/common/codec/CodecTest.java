package indi.hiro.common.codec;

import indi.hiro.common.codec.arithmetic.ArithmeticSequence;
import indi.hiro.common.codec.huffman.*;
import indi.hiro.common.codec.lzw.CompressionDictionary;
import indi.hiro.common.codec.lzw.DecompressionDictionary;
import indi.hiro.common.ds.generic.LinearCounter;
import indi.hiro.common.ds.primitive.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hiro on 2018/10/18.
 *
 * Main class
 */
public class CodecTest {

    public static void main(String[] args) {
        testAll();
    }

    private static void testAll() {
        testHuffman();
        testExtendedHuffman();
        testLzw();
        testArithmetic();
    }

    private static void testHuffman() {
        String message = "That's one small step for a man, a giant leap for mankind.";
        try {
            HuffmanTree<Character> huffmanTree = new HuffmanTree<>(HuffmanTreeNode.buildFromCC(new CharCounter(message)));
            BooleanBuilder code = (new HuffmanTable<>(huffmanTree)).encode(new StringAsCharacterIterable(message));
            System.out.println(code);
            StringBuilder result = new StringBuilder();
            huffmanTree.decode(code, new StringBuilderAsCharacterConsumer(result));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testExtendedHuffman() {
        String message = "ABCDC ABBBC BBBAC BCBBB ACDBB CCABD BBBDD BBBAB DBBAC";
        try {
            ArrayList<ExtendedHuffmanData<Character>> huffmanData = ExtendedHuffmanData.encode(new StringAsCharacterIterable(message));
            LinearCounter<ExtendedHuffmanData<Character>> linearCounter = new LinearCounter<>();
            linearCounter.acceptAll(huffmanData);
            HuffmanTree<ExtendedHuffmanData<Character>> huffmanTree = new HuffmanTree<>(linearCounter.toHuffmanSegments());
            BooleanBuilder code = (new HuffmanTable<>(huffmanTree)).encode(huffmanData);
            System.out.println(code);
            StringBuilder result = new StringBuilder();
            huffmanTree.decode(code, new ExtendedHuffmanDataConsumer<>(new StringBuilderAsCharacterConsumer(result)));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testLzw() {
        List<Character> commonDictionary = new ArrayList<>();
        commonDictionary.add('A');
        commonDictionary.add('B');
        commonDictionary.add('C');
        String message = "ABABBABCABABBA";
        try {
            ArrayList<Integer> code = (new CompressionDictionary<>(commonDictionary)).encode(new StringAsCharacterIterable(message));
            System.out.println(code);
            StringBuilder result = new StringBuilder();
            (new DecompressionDictionary<>(commonDictionary)).decode(code, new StringBuilderAsCharacterConsumer(result));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testArithmetic() {
        String message = "a new birth of freedom - and that government of the people, by the people, for the people, shall not perish from earth.";
        try {
            ArithmeticSequence<Character> arithmeticSequence = ArithmeticSequence.buildFromCC(new CharCounter(message), true, true);
            BooleanBuilder code = arithmeticSequence.encode(new StringAsCharacterIterable(message));
            System.out.println(code);
            StringBuilder result = new StringBuilder();
            arithmeticSequence.decode(code, new StringBuilderAsCharacterConsumer(result));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
