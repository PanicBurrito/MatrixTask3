package sparse;

public class SparseOps {

    // y = A * x   (A in CSR, x dense Vektor)
    public static double[] multiply(SparseMatrixCSR A, double[] x) {
        if (x.length != A.cols) {
            throw new IllegalArgumentException("Dimension mismatch: x.length=" +
                    x.length + " but A.cols=" + A.cols);
        }

        double[] y = new double[A.rows];

        for (int i = 0; i < A.rows; i++) {
            double sum = 0.0;
            int rowStart = A.rowPtr[i];
            int rowEnd   = A.rowPtr[i + 1];

            for (int idx = rowStart; idx < rowEnd; idx++) {
                int j = A.colIndex[idx];
                sum += A.values[idx] * x[j];
            }
            y[i] = sum;
        }

        return y;
    }

    // Optionale Hilfe: Sparse × Dense-Matrix (SpMM) über SpMV pro Spalte
    public static double[][] multiply(SparseMatrixCSR A, double[][] B) {
        int nColsB = B[0].length;
        if (B.length != A.cols) {
            throw new IllegalArgumentException("Dimension mismatch: B.rows=" +
                    B.length + " but A.cols=" + A.cols);
        }

        double[][] C = new double[A.rows][nColsB];

        double[] x = new double[A.cols];
        for (int col = 0; col < nColsB; col++) {
            // Spalte von B in Vektor x kopieren
            for (int i = 0; i < A.cols; i++) {
                x[i] = B[i][col];
            }
            double[] y = multiply(A, x);
            // zurück in C schreiben
            for (int i = 0; i < A.rows; i++) {
                C[i][col] = y[i];
            }
        }
        return C;
    }
}