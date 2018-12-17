package indi.hiro.common.codec.mm;

import indi.hiro.common.lang.DoubleArrays;

import java.util.Arrays;

/**
 * Created by Hiro on 2018/12/2.
 */
public class AcDct {

    public static final double[][] M = {
            { 0,  0,  0,  0, 16,  0,  0,  0},
            { 4,  0,  8, 16, 32, 16,  8,  0},
            { 4,  0, 16, 32, 64, 32, 16,  0},
            { 0,  0, 32, 64,128, 64, 32,  0},
            { 4,  0,  0, 32, 64, 32,  0,  0},
            { 0, 16,  0,  0, 32,  0,  0,  0},
            { 0,  0,  0,  0, 16,  0,  0,  0},
            { 0,  0,  0,  0,  0,  0,  0,  0}
    };

    public static void transform() {
        double[][] m1 = DoubleArrays.copy2(M);
        int width = m1[0].length;
        int height = m1.length;
        double[][] m2 = new double[height][width];
        for (int x = 0; x < width; x++) {
            int py = 0;
            for (int y = 0; y < width; y++) {
                if (m1[y][x] != 0) {
                    m1[py++][x] = m1[y][x];
                }
            }
            for (int v = 0; v < py; v++) {
                double s = 0;
                for (int y = 0; y < py; y++) {
                    s += Math.cos(((2 * y + 1) * v * Math.PI) / (2 * py)) * m1[y][x];
                }
                m2[v][x] = Math.sqrt(2.0 / py) * (v == 0 ? Math.sqrt(0.5) : 1.0) * s;
            }
            while (py < height) {
                m2[py++][x] = Double.NaN;
            }
        }
        double[][] m3 = new double[height][width];
        for (int y = 0; y < height; y++) {
            int px = 0;
            for (int x = 0; x < width; x++) {
                if (!Double.isNaN(m2[y][x])) {
                    m2[y][px++] = m2[y][x];
                }
            }
            for (int u = 0; u < px; u++) {
                double s = 0;
                for (int x = 0; x < px; x++) {
                    s += Math.cos(((2 * y + 1) * u * Math.PI) / (2 * px)) * m2[y][x];
                }
                m3[y][u] = Math.sqrt(2.0 / px) * (u == 0 ? Math.sqrt(0.5) : 1.0) * s;
            }
            while (px < height) {
                m3[y][px++] = Double.NaN;
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (Double.isNaN(m3[y][x])) {
                    System.out.printf("\t");
                } else {
                    System.out.printf("\t%.2f", m3[y][x]);
                }
            }
            System.out.println();
        }
    }
}
