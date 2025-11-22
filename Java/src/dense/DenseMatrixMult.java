package dense;

public class DenseMatrixMult {

    public static void multiplyNaive(double[][] a, double[][] b, double[][] c) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += a[i][k] * b[k][j];
                }
                c[i][j] = sum;
            }
        }
    }

    public static void multiplyImproved(double[][] a, double[][] b, double[][] c) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            double[] ci = c[i];
            for (int k = 0; k < n; k++) {
                double aik = a[i][k];
                double[] bk = b[k];
                for (int j = 0; j < n; j++) {
                    ci[j] += aik * bk[j];
                }
            }
        }
    }

    public static void multiplyBlocked(double[][] a, double[][] b, double[][] c, int blockSize) {
        int n = a.length;
        for (int ii = 0; ii < n; ii += blockSize) {
            for (int jj = 0; jj < n; jj += blockSize) {
                for (int kk = 0; kk < n; kk += blockSize) {

                    int iMax = Math.min(ii + blockSize, n);
                    int jMax = Math.min(jj + blockSize, n);
                    int kMax = Math.min(kk + blockSize, n);

                    for (int i = ii; i < iMax; i++) {
                        double[] ci = c[i];
                        for (int k = kk; k < kMax; k++) {
                            double aik = a[i][k];
                            double[] bk = b[k];
                            for (int j = jj; j < jMax; j++) {
                                ci[j] += aik * bk[j];
                            }
                        }
                    }
                }
            }
        }
    }

    public static void multiplyDenseSpMV(double[][] A, double[] x, double[] y) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            double[] rowA = A[i];
            for (int j = 0; j < n; j++) {
                sum += rowA[j] * x[j];
            }
            y[i] = sum;
        }
    }
}
