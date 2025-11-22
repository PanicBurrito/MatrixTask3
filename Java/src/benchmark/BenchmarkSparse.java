package benchmark;

import sparse.SparseMatrixCSR;
import sparse.SparseOps;
import sparse.SparseUtils;
import dense.MatrixUtils;
import dense.DenseMatrixMult;

public class BenchmarkSparse {

    public static double[] runOnce(int n, double density) {
        SparseMatrixCSR A = SparseUtils.randomSparseCSR(n, density, 42L);

        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = i * 0.001;
        }

        long tStartSp = System.nanoTime();
        double[] ySparse = SparseOps.multiply(A, x);
        long tEndSp = System.nanoTime();
        double timeSparse = (tEndSp - tStartSp) / 1e9;

        double[][] denseA = SparseUtils.toDense(A);
        double[] yDense = new double[n];

        long tStartDense = System.nanoTime();
        DenseMatrixMult.multiplyDenseSpMV(denseA, x, yDense);
        long tEndDense = System.nanoTime();
        double timeDense = (tEndDense - tStartDense) / 1e9;

        int nnz = A.values.length;

        return new double[]{nnz, timeSparse, timeDense};
    }

    public static void main(String[] args) {
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 500;
        double density = (args.length > 1) ? Double.parseDouble(args[1]) : 0.01;

        System.out.printf("n = %d, density = %.5f%n", n, density);

        SparseMatrixCSR A = SparseUtils.randomSparseCSR(n, density, 42L);
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = i * 0.001;
        }

        long tStartSp = System.nanoTime();
        double[] ySparse = SparseOps.multiply(A, x);
        long tEndSp = System.nanoTime();
        double timeSparse = (tEndSp - tStartSp) / 1e9;

        System.out.printf("Sparse SpMV: %.6f s (nnz = %d)%n",
                timeSparse, A.values.length);

        double[][] denseA = SparseUtils.toDense(A);
        double[] yDense = new double[n];

        long tStartDense = System.nanoTime();
        DenseMatrixMult.multiplyDenseSpMV(denseA, x, yDense);
        long tEndDense = System.nanoTime();
        double timeDense = (tEndDense - tStartDense) / 1e9;

        System.out.printf("Dense SpMV: %.6f s%n", timeDense);

        double maxDiff = 0.0;
        for (int i = 0; i < n; i++) {
            double diff = Math.abs(ySparse[i] - yDense[i]);
            if (diff > maxDiff) maxDiff = diff;
        }
        System.out.printf("Max difference (sparse vs dense) = %.3e%n", maxDiff);
    }
}
