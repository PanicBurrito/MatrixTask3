package sparse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SparseUtils {

    /**
     * Erzeugt eine zufällige Sparse-Matrix in CSR.
     *
     * @param n        Zeilen/Spalten (quadratisch)
     * @param density  Anteil der Nicht-Null-Einträge (z.B. 0.01 = 1 %)
     * @param seed     RNG Seed
     */
    public static SparseMatrixCSR randomSparseCSR(int n, double density, long seed) {
        Random random = new Random(seed);

        List<Double> valuesList = new ArrayList<>();
        List<Integer> colIndexList = new ArrayList<>();
        int[] rowPtr = new int[n + 1];

        int nnzSoFar = 0;

        for (int i = 0; i < n; i++) {
            rowPtr[i] = nnzSoFar;
            for (int j = 0; j < n; j++) {
                if (random.nextDouble() < density) {
                    double v = random.nextDouble();
                    valuesList.add(v);
                    colIndexList.add(j);
                    nnzSoFar++;
                }
            }
        }
        rowPtr[n] = nnzSoFar;

        double[] values = new double[nnzSoFar];
        int[] colIndex = new int[nnzSoFar];

        for (int k = 0; k < nnzSoFar; k++) {
            values[k] = valuesList.get(k);
            colIndex[k] = colIndexList.get(k);
        }

        return new SparseMatrixCSR(n, n, values, colIndex, rowPtr);
    }

    // Optional: Dense Matrix mit denselben Nicht-Null-Positionen generieren (für Vergleich)
    public static double[][] toDense(SparseMatrixCSR A) {
        double[][] dense = new double[A.rows][A.cols];
        for (int i = 0; i < A.rows; i++) {
            int start = A.rowPtr[i];
            int end   = A.rowPtr[i + 1];
            for (int idx = start; idx < end; idx++) {
                int j = A.colIndex[idx];
                dense[i][j] = A.values[idx];
            }
        }
        return dense;
    }
}
