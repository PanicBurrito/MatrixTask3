package sparse;

public class SparseMatrixCSR {
    public final int rows;
    public final int cols;
    public final double[] values;   // alle Nicht-Null-Werte
    public final int[] colIndex;    // Spaltenindex zu jedem value
    public final int[] rowPtr;      // Startindex pro Zeile (Länge = rows + 1)

    public SparseMatrixCSR(int rows, int cols, double[] values, int[] colIndex, int[] rowPtr) {
        this.rows = rows;
        this.cols = cols;
        this.values = values;
        this.colIndex = colIndex;
        this.rowPtr = rowPtr;
    }
}
