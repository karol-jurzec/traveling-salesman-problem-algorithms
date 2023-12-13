package src.main.java.tsp.tspmlp.NormalizeData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NormalizeData {

    //class which normalize data from csv file by dividing them by max_val of column

    public static List<List<Double>> readCSV(String inputFile) throws FileNotFoundException {
        List<List<Double>> data = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(inputFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                List<Double> row = new ArrayList<>();
                for (int i = 0; i < values.length; i++) {
                    row.add(Double.parseDouble(values[i]));
                }
                data.add(row);
            }
        }
        return data;
    }

    public static void writeCSV(String outputFile, List<List<Double>> data) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new File(outputFile))) {
            for (List<Double> row : data) {
                for (int i = 0; i < row.size(); i++) {
                    if (i > 0) {
                        writer.print(",");
                    }
                    writer.print(row.get(i));
                }
                writer.println();
            }
        }
    }

    public static void normalizeColumns(List<List<Double>> data) {
        for (int col = 1; col < data.get(0).size(); col++) {
            double max = findMax(data, col);

            // Normalize each column
            for (List<Double> row : data) {
                double originalValue = row.get(col);
                double normalizedValue = originalValue/max;
                row.set(col, normalizedValue);
            }
        }
    }

    private static double findMin(List<List<Double>> data, int columnIndex) {
        double min = Double.MAX_VALUE;
        for (List<Double> row : data) {
            double value = row.get(columnIndex);
            min = Math.min(min, value);
        }
        return min;
    }

    private static double findMax(List<List<Double>> data, int columnIndex) {
        double max = Double.MIN_VALUE;
        for (List<Double> row : data) {
            double value = row.get(columnIndex);
            max = Math.max(max, value);
        }
        return max;
    }
}
