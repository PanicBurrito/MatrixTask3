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
        // if arguments are given, use them as sizes; otherwise use a small default set
        int[] sizes;
        if (args.length > 0) {
            sizes = new int[args.length];
            for (int i = 0; i < args.length; i++) {
                sizes[i] = Integer.parseInt(args[i]);
            }
        } else {
            sizes = new int[]{256, 512, 1024, 1536};
        }
        // block sizes to experiment with for blocked multiplication
        int defaultBlockSize = 32;
        int[] blockSizes = new int[]{16, 32, 64, 128};

        for (int n : sizes) {
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

            // 3) Blocked (using default block size)
            double[][] cBlocked = MatrixUtils.zeroMatrix(n);
            long t3Start = System.nanoTime();
            DenseMatrixMult.multiplyBlocked(a, b, cBlocked, defaultBlockSize);
            long t3End = System.nanoTime();
            double timeBlocked = (t3End - t3Start) / 1e9;

            // 4) Naive parallel with ExecutorService
            double[][] cParallelExec = MatrixUtils.zeroMatrix(n);
            long t4Start = System.nanoTime();
            DenseMatrixMult.multiplyNaiveParallelExecutor(a, b, cParallelExec);
            long t4End = System.nanoTime();
            double timeParallelExec = (t4End - t4Start) / 1e9;

            // 5) Naive parallel with parallel streams
            double[][] cParallelStream = MatrixUtils.zeroMatrix(n);
            long t5Start = System.nanoTime();
            DenseMatrixMult.multiplyNaiveParallelStream(a, b, cParallelStream);
            long t5End = System.nanoTime();
            double timeParallelStream = (t5End - t5Start) / 1e9;

            System.out.printf("n = %d%n", n);
            System.out.printf("Naive:             %.6f s%n", timeNaive);
            System.out.printf("Improved:          %.6f s%n", timeImproved);
            System.out.printf("Blocked:           %.6f s%n", timeBlocked);
            System.out.printf("Parallel Executor: %.6f s (speedup vs naive: %.2fx)%n",
                    timeParallelExec, timeNaive / timeParallelExec);
            System.out.printf("Parallel Stream:   %.6f s (speedup vs naive: %.2fx)%n",
                    timeParallelStream, timeNaive / timeParallelStream);

            // extra: measure blocked performance for different block sizes
            System.out.println("Blocked variants (different block sizes):");
            for (int B : blockSizes) {
                double[][] cBlockedVar = MatrixUtils.zeroMatrix(n);
                long tbStart = System.nanoTime();
                DenseMatrixMult.multiplyBlocked(a, b, cBlockedVar, B);
                long tbEnd = System.nanoTime();
                double timeB = (tbEnd - tbStart) / 1e9;
                System.out.printf("  Blocked (B=%3d): %.6f s%n", B, timeB);
            }

            System.out.println();
        }
    }
}
