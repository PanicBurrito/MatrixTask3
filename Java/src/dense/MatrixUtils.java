package dense;

import java.util.Random;

public class MatrixUtils {

    public static double[][] randomMatrix(int n, long seed) {
        double[][] m = new double[n][n];
        Random random = new Random(seed);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = random.nextDouble();
            }
        }
        return m;
    }

    public static double[][] zeroMatrix(int n) {
        return new double[n][n];
    }
}