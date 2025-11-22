package benchmark;

public class RunAllBenchmarks {

    public static void main(String[] args) {

        // -------- Dense Benchmarks --------
        int[] denseSizes = {128, 256, 512, 1024};

        System.out.println("=== DENSE BENCHMARK ===");
        System.out.println("n, naive_s, improved_s, blocked_s");

        for (int n : denseSizes) {
            double[] res = BenchmarkDense.runOnce(n);
            double naive = res[0];
            double improved = res[1];
            double blocked = res[2];
            System.out.printf("%d, %.6f, %.6f, %.6f%n", n, naive, improved, blocked);
        }

        // -------- Random Sparse Benchmarks --------
        int[] sparseSizes = {500, 1000};
        double[] densities = {0.10, 0.01, 0.001};

        System.out.println();
        System.out.println("=== SPARSE (RANDOM) BENCHMARK ===");
        System.out.println("n, density, nnz, sparse_s, dense_s");

        for (int n : sparseSizes) {
            for (double density : densities) {
                double[] res = BenchmarkSparse.runOnce(n, density);
                int nnz = (int) res[0];
                double sparseTime = res[1];
                double denseTime = res[2];
                System.out.printf("%d, %.5f, %d, %.6f, %.6f%n",
                        n, density, nnz, sparseTime, denseTime);
            }
        }

        // -------- Real Sparse Matrix Benchmark (mc2depi) --------
        System.out.println();
        System.out.println("=== REAL MATRIX (mc2depi) BENCHMARK ===");
        BenchmarkMc2depi.main(new String[0]);
    }
}
