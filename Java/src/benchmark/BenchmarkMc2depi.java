package benchmark;

import sparse.Mc2Reader;
import sparse.SparseMatrixCSR;
import sparse.SparseOps;

import java.io.IOException;

public class BenchmarkMc2depi {

    public static void main(String[] args) {
        String path = (args.length > 0)
                ? args[0]
                : "/Users/jonas.mmllr/Downloads/mc2depi/mc2depi.mtx";

        try {
            System.out.println("Read matrix of: " + path);
            long tLoadStart = System.nanoTime();
            SparseMatrixCSR A = Mc2Reader.readCSR(path);
            long tLoadEnd = System.nanoTime();

            System.out.printf("Matrix loaded: %d x %d, nnz = %d%n",
                    A.rows, A.cols, A.values.length);
            System.out.printf("Loadtime: %.3f s%n", (tLoadEnd - tLoadStart) / 1e9);

            int n = A.cols;

            double[] x = new double[n];
            for (int i = 0; i < n; i++) {
                x[i] = 1.0;
            }

            SparseOps.multiply(A, x);

            long tStart = System.nanoTime();
            double[] y = SparseOps.multiply(A, x);
            long tEnd = System.nanoTime();

            double time = (tEnd - tStart) / 1e9;
            System.out.printf("SpMV time (mc2depi * x): %.6f s%n", time);

            double sum = 0.0;
            for (double v : y) sum += v;
            System.out.printf("Sum(y) = %.6e%n", sum);

        } catch (IOException e) {
            System.err.println("Error at reading matrix: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
