package indi.hiro.common.codec.mm;

/**
 * Created by Hiro on 2018/11/4.
 */
public class DctQuiz {

    public static void solve() {
        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {
                double f = 0;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        f += Math.max(0, Math.cos((2 * i + 1) * u * Math.PI / 16) * Math.cos((2 * j + 1) * v * Math.PI / 16));
                    }
                }
                f *= (u == 0 ? Math.sqrt(0.5) : 1) * (v == 0 ? Math.sqrt(0.5) : 1) * 255 / 4;
                System.out.printf("%.2f\t", f);
            }
            System.out.println();
        }
    }
}
