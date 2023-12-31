package src.main.java.tsp.test;

import src.main.java.tsp.TspSolver;
import src.main.java.tsp.algorithms.BruteForceAlgorithm;
import src.main.java.tsp.algorithms.ChristofidesAlgorithm;
import src.main.java.tsp.algorithms.ITspAlgorithm;
import src.main.java.tsp.models.TspInstance;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TspTimer {

    private static final TspSolver tspSolver = new TspSolver(new ChristofidesAlgorithm());
    private static final String path = "/Users/karol/Desktop/uni/tsp/data/";

    public static TspInstance generateTSPInstance(int n) {
        Random random = new Random();

        ArrayList<Point2D> points = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            double x = random.nextDouble() * 200;
            double y = random.nextDouble() * 200;
            points.add(new Point2D.Double(x, y));
        }

        return new TspInstance(points, "", "");
    }

    public static double computeRunningTime(TspSolver tspSolver,
                                            TspInstance tspInstance) {
        long start = System.currentTimeMillis();
        tspSolver.solve(tspInstance);
        long end = System.currentTimeMillis();

        return (end - start)/1000.0;
    }

    private static void writeToFile(int n, double avg, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {

            writer.write(n + " " + avg);
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void algorithmTimer(int i, int interval, ITspAlgorithm algorithm, String name) {
        tspSolver.setAlgorithm(algorithm);

        for(int n = 3; n <= i; n = n + interval) {
            ArrayList<Double> compTimes = new ArrayList<>();

            for(int j = 0; j < 5; ++j) {
                var instance = generateTSPInstance(n);
                var runningTime = computeRunningTime(tspSolver, instance);
                compTimes.add(runningTime);
            }

            double avg = compTimes.stream().mapToDouble(a -> a).average().getAsDouble();
            writeToFile(n, avg, path + "_" + name);

        }
    }
}
