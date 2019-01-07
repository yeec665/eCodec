package indi.hiro.common.codec.compression;

import indi.hiro.common.ds.generic.LinearCounter;
import indi.hiro.common.ds.primitive.BooleanBuilder;
import indi.hiro.common.ds.primitive.CharCounter;
import indi.hiro.common.ds.primitive.StringAsCharacterIterable;
import indi.hiro.common.ds.theoretical.InformationEntropy;
import indi.hiro.common.lang.FpToStringConverter;

import java.util.ArrayList;

/**
 * Created by Hiro on 2019/1/7.
 */
public class EntropyAnalyzer {

    public static void testAll() {
        testCodec5("ABCDC ABBBC BBBAC BCBBB ACDBB CCABD BBBDD BBBAB DBBAC");
        testForeseen3();
        testUnforeseen2();
    }

    private static void testForeseen3() {
        testForeseen3("That's one small step for a man, a giant leap for mankind.");
        testForeseen3("https://www.bilibili.com/index.html");
        testForeseen3("The Python programming language has won the title \"programming language of the year\"! " +
                "Python has received this title because it has gained most ranking points in 2018 if compared to all other languages.");
        testForeseen3("若想解决纷争，必先陷入纷争！");
        testForeseen3("一儿曰：“日初出大如车盖，及日中则如盘盂，此不为远者小而近者大乎？”" +
                "一儿曰：“日初出沧沧凉凉，及其日中如探汤，此不为近者热而远者凉乎？”");
        testForeseen3("卷积神经网络（Convolutional Neural Networks，CNN）是一种深度前馈人工神经网络，当今在图像识别中属于比较新的且效果较好的机器学习算法。");
    }

    private static void testUnforeseen2() {
        testUnforeseen2("000000010020002400204000505003040203233121130422050000000400004");
        testUnforeseen2("It was the best of times, it was the worst of times, it was the age of wisdom, it was the age of foolishness, it was the epoch of belief, it was the epoch of incredulity, it was the season of Light, " +
                "it was the season of Darkness, it was the spring of hope, it was the winter of despair, we had everything before us, we had nothing before us, we were all going direct to Heaven, we were all going direct the other way--in short, the period was so.");
        testUnforeseen2("昔人已乘黄鹤去，此地空余黄鹤楼。黄鹤一去不复返，白云千载空悠悠。");
        testUnforeseen2("浙江大学控制科学与工程学院（简称控制学院）始建于1956年，现有“自动化”本科专业和“控制科学与工程”、“网络空间安全”2个一级学科。其中，“控制科学与工程”学科是国家一级重点学科，" +
                "覆盖了控制理论与控制工程、模式识别与智能系统、系统工程、检测技术与自动化装置、导航制导与控制等5个二级学科,拥有“控制科学与工程”一级学科的博士和硕士学位授予权、控制工程专业硕士学位授予权。");
    }

    public static void testCodec5(String msg) {
        System.out.println();
        System.out.println("Analyzing entropy all 5 codecs on symbol sequence : ");
        System.out.println(msg);
        analyze(msg);
        System.out.println();
        testHuffman(msg);
        System.out.println();
        testExtendedHuffman(msg);
        System.out.println();
        testAdaptiveHuffman(msg);
        System.out.println();
        testLzw(msg);
        System.out.println();
        testArithmetic(msg);
        System.out.println();
    }

    public static void testForeseen3(String msg) {
        System.out.println(EntropyAnalyzer.class.getSimpleName() + " testing foreseen 3 on symbol sequence : ");
        System.out.println(msg);
        analyze(msg);
        System.out.println();
        testHuffman(msg);
        System.out.println();
        testExtendedHuffman(msg);
        System.out.println();
        testArithmetic(msg);
        System.out.println();
    }

    public static void testUnforeseen2(String msg) {
        System.out.println(EntropyAnalyzer.class.getSimpleName() + " testing unforeseen 2 on symbol sequence : ");
        System.out.println(msg);
        analyze(msg);
        System.out.println();
        testAdaptiveHuffman(msg);
        System.out.println();
        testLzw(msg);
        System.out.println();
    }

    private static void analyze(String msg) {
        System.out.print(Byte.SIZE * msg.getBytes().length);
        System.out.print('\t');
        System.out.print(FpToStringConverter.toDecString(msg.length() * InformationEntropy.entropy(msg), 2));
    }

    private static void analyze(BooleanBuilder bb) {
        System.out.print(bb.bitLen());
        System.out.print('\t');
        System.out.print(FpToStringConverter.toDecString(bb.bitLen() * InformationEntropy.entropy(bb), 2));
    }

    private static void analyze(ArrayList<Integer> integerArrayList) {
        int maxValue = 0;
        for (int i : integerArrayList) {
            if (i > maxValue) {
                maxValue = i;
            }
        }
        System.out.print((Integer.SIZE - Integer.numberOfLeadingZeros(maxValue)) * integerArrayList.size());
        System.out.print('\t');
        System.out.print(FpToStringConverter.toDecString(integerArrayList.size() * InformationEntropy.entropy(integerArrayList), 2));
    }

    private static void testHuffman(String msg) {
        try {
            BooleanBuilder code = new HuffmanTable<>(new HuffmanTree<>(
                    HuffmanTreeNode.buildFromCC(new CharCounter(msg))
            )).encode(new StringAsCharacterIterable(msg));
            analyze(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testExtendedHuffman(String msg) {
        try {
            ArrayList<ExtendedHuffmanData<Character>> huffmanData = ExtendedHuffmanData.encode(new StringAsCharacterIterable(msg));
            LinearCounter<ExtendedHuffmanData<Character>> linearCounter = new LinearCounter<>();
            linearCounter.acceptAll(huffmanData);
            BooleanBuilder code = new HuffmanTable<>(new HuffmanTree<>(
                    linearCounter.toHuffmanSegments()
            )).encode(huffmanData);
            analyze(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testAdaptiveHuffman(String msg) {
        try {
            InitialCodeAssignment<Character> initialCodeAssignment = new InitialCodeAssignment<>(InitialCodeAssignment.dictionary(msg), 0, false);
            AdaptiveHuffmanTree<Character> adaptiveHuffmanTree = new AdaptiveHuffmanTree<>(initialCodeAssignment);
            BooleanBuilder code = adaptiveHuffmanTree.encode(new StringAsCharacterIterable(msg));
            analyze(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testLzw(String msg) {
        try {
            CompressionDictionary<Character> compressionDictionary = new CompressionDictionary<>(InitialCodeAssignment.dictionary(msg));
            ArrayList<Integer> code = compressionDictionary.encode(new StringAsCharacterIterable(msg));
            analyze(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testArithmetic(String msg) {
        try {
            ArithmeticSequence<Character> arithmeticSequence = ArithmeticSequence.buildFromCC(new CharCounter(msg), true, true);
            BooleanBuilder code = arithmeticSequence.encode(new StringAsCharacterIterable(msg));
            analyze(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
