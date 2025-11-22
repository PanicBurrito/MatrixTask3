package benchmark;

import dense.MatrixUtils;
import dense.DenseMatrixMult;

public class BenchmarkDense {

    public static double[] runOnce(int n) {
        double[][] a = MatrixUtils.randomMatrix(n, 42);
        double[][] b = MatrixUtils.randomMatrix(n, 43);

        // naive
        double[][] cNaive = MatrixUtils.zeroMatrix(n);
        long t1Start = System.nanoTime();
        DenseMatrixMult.multiplyNaive(a, b, cNaive);
        long t1End = System.nanoTime();
        double timeNaive = (t1End - t1Start) / 1e9;

        // improved
        double[][] cImproved = MatrixUtils.zeroMatrix(n);
        long t2Start = System.nanoTime();
        DenseMatrixMult.multiplyImproved(a, b, cImproved);
        long t2End = System.nanoTime();
        double timeImproved = (t2End - t2Start) / 1e9;

        // blocked
        double[][] cBlocked = MatrixUtils.zeroMatrix(n);
        long t3Start = System.nanoTime();
        DenseMatrixMult.multiplyBlocked(a, b, cBlocked, 32);
        long t3End = System.nanoTime();
        double timeBlocked = (t3End - t3Start) / 1e9;

        return new double[]{timeNaive, timeImproved, timeBlocked};
    }

    public static void main(String[] args) {
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 1024;

        double[][] a = MatrixUtils.randomMatrix(n, 42);
        double[][] b = MatrixUtils.randomMatrix(n, 43);

        // 1) Naive
        double[][] cNaive = MatrixUtils.zeroMatrix(n);
        long t1Start = System.nanoTime();
        DenseMatrixMult.multiplyNaive(a, b, cNaive);
        long t1End = System.nanoTime();
        double timeNaive = (t1End - t1Start) / 1e9;

        // 2) Improved
        double[][] cImproved = MatrixUtils.zeroMatrix(n);
        long t2Start = System.nanoTime();
        DenseMatrixMult.multiplyImproved(a, b, cImproved);
        long t2End = System.nanoTime();
        double timeImproved = (t2End - t2Start) / 1e9;

        double[][] cBlocked = MatrixUtils.zeroMatrix(n);
        long t3Start = System.nanoTime();
        DenseMatrixMult.multiplyBlocked(a, b, cBlocked, 32); // z.B. blockSize = 32
        long t3End = System.nanoTime();
        double timeBlocked = (t3End - t3Start) / 1e9;

        System.out.printf("n = %d%n", n);
        System.out.printf("Naive:    %.6f s%n", timeNaive);
        System.out.printf("Improved: %.6f s%n", timeImproved);
        System.out.printf("Blocked:  %.6f s%n", timeBlocked);
    }
}
