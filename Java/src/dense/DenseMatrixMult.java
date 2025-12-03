package dense;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

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

    /**
     * Parallel naive matrix multiplication using an ExecutorService.
     * Each worker computes a contiguous block of rows of the result matrix c.
     */
    public static void multiplyNaiveParallelExecutor(double[][] a, double[][] b, double[][] c) {
        int n = a.length;
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(cores);

        int blockSize = (n + cores - 1) / cores; // roughly n / cores
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int t = 0; t < cores; t++) {
            final int startRow = t * blockSize;
            final int endRow = Math.min(startRow + blockSize, n);
            if (startRow >= endRow) {
                break;
            }

            tasks.add(() -> {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = 0; j < n; j++) {
                        double sum = 0.0;
                        for (int k = 0; k < n; k++) {
                            sum += a[i][k] * b[k][j];
                        }
                        c[i][j] = sum;
                    }
                }
                return null;
            });
        }

        try {
            pool.invokeAll(tasks); // waits for all tasks to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            pool.shutdown();
        }
    }

    /**
     * Parallel naive matrix multiplication using Java parallel streams.
     * Each row i of the result matrix c is computed in parallel.
     */
    public static void multiplyNaiveParallelStream(double[][] a, double[][] b, double[][] c) {
        int n = a.length;
        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += a[i][k] * b[k][j];
                }
                c[i][j] = sum;
            }
        });
    }
}
