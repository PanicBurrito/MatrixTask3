package sparse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mc2Reader {

    public static SparseMatrixCSR readCSR(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("%")) continue;
                break;
            }

            if (line == null) {
                throw new IOException("Unerwartetes Dateiende in " + path);
            }

            String[] parts = line.split("\\s+");
            if (parts.length < 3) {
                throw new IOException("Ungültige Headerzeile: " + line);
            }

            int nRows = Integer.parseInt(parts[0]);
            int nCols = Integer.parseInt(parts[1]);
            int nnz = Integer.parseInt(parts[2]);

            List<Integer> rowList = new ArrayList<>(nnz);
            List<Integer> colList = new ArrayList<>(nnz);
            List<Double> valList = new ArrayList<>(nnz);

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("%")) {
                    continue;
                }
                String[] toks = line.split("\\s+");
                if (toks.length < 3) continue;

                int row = Integer.parseInt(toks[0]);
                int col = Integer.parseInt(toks[1]);
                double value = Double.parseDouble(toks[2]);

                rowList.add(row - 1);
                colList.add(col - 1);
                valList.add(value);
            }

            if (rowList.size() != nnz) {
                System.out.println("Warnung: eingelesene nnz = " + rowList.size() +
                        ", im Header stand nnz = " + nnz);
                nnz = rowList.size();
            }

            int[] rowPtr = new int[nRows + 1];
            int[] colIndex = new int[nnz];
            double[] values = new double[nnz];

            for (int r : rowList) {
                rowPtr[r + 1]++;
            }

            for (int i = 0; i < nRows; i++) {
                rowPtr[i + 1] += rowPtr[i];
            }

            int[] current = rowPtr.clone();

            for (int k = 0; k < nnz; k++) {
                int r = rowList.get(k);
                int destPos = current[r];
                colIndex[destPos] = colList.get(k);
                values[destPos] = valList.get(k);
                current[r]++;
            }

            return new SparseMatrixCSR(nRows, nCols, values, colIndex, rowPtr);
        }
    }
}
