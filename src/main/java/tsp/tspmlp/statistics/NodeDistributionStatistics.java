package src.main.java.tsp.tspmlp.statistics;

import src.main.java.tsp.tspmlp.TspMlpFeatures;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NodeDistributionStatistics {
    private double normalizedDistanceStdDeviation;
    private double distinctDistancesFraction;
    private double meanDistanceToCentroidNode; // mean of distances to centroid node


    public NodeDistributionStatistics(ArrayList<Point2D> points, double[][] distMatrix) {
        computeNodeDistributionStatistics(distMatrix, points);
    }

    private void computeNodeDistributionStatistics(double[][] distMatrix, ArrayList<Point2D> points) {
        double normalizedStdDev = computeNormalizedStdDeviation(distMatrix);
        double distinctDistFraction = computeFractionOfDistinctDistances(distMatrix);
        double meanDistToCentroidPoint = computeMeanDistanceToCentroidPoint(points);

        this.normalizedDistanceStdDeviation = normalizedStdDev;
        this.distinctDistancesFraction = distinctDistFraction;
        this.meanDistanceToCentroidNode = meanDistToCentroidPoint;
    }

    private double computeNormalizedStdDeviation(double[][] distMatrix) {
        double[] distances = new double[distMatrix.length * distMatrix.length];
        int index = 0;

        for (int i = 0; i < distMatrix.length; i++) {
            for (int j = 0; j < distMatrix.length; j++) {
                distances[index++] = distMatrix[i][j];
            }
        }

        double mean = TspMlpFeatures.computeMean(distances);
        double stdDev = TspMlpFeatures.computeStdDeviation(distances, mean);

        double normalizedStdDeviation = (stdDev / 400.0) * (stdDev / 400.0);
        return normalizedStdDeviation;
    }

    private double computeFractionOfDistinctDistances(double[][] distMatrix) {
        double[] distances = new double[distMatrix.length * distMatrix.length];
        int index = 0;

        for (int i = 0; i < distMatrix.length; i++) {
            for (int j = 0; j < distMatrix.length; j++) {
                distances[index++] = distMatrix[i][j];
            }
        }

        Set<Double> distinctDistances = new HashSet<>();

        for (double distance : distances) {
            distinctDistances.add(distance);
        }

        // Calculate the fraction of distinct distances
        double fractionOfDistinctDistances = (double) distinctDistances.size() / distances.length;

        return fractionOfDistinctDistances;
    }

    private double computeMeanDistanceToCentroidPoint(ArrayList<Point2D> point) {
        var centroid = calculateCentroidPoint(point);
        double[] distances = new double[point.size()];

        for(int i = 0; i < point.size(); ++i) {
            distances[i] = centroid.distance(point.get(i));
        }

        return TspMlpFeatures.computeMean(distances);
    }

    private Point2D calculateCentroidPoint(ArrayList<Point2D> points) {
        double sumX = 0.0;
        double sumY = 0.0;

        // Calculate the sum of x and y coordinates
        for (Point2D point : points) {
            sumX += point.getX();
            sumY += point.getY();
        }

        // Calculate the average x and y coordinates
        double avgX = sumX / points.size();
        double avgY = sumY / points.size();

        return new Point2D.Double(avgX, avgY);
    }

    @Override
    public String toString() {
        return "," + normalizedDistanceStdDeviation +
                "," + distinctDistancesFraction +
                "," + meanDistanceToCentroidNode;
    }

    public double[] toArray() {
        return new double[] {normalizedDistanceStdDeviation, distinctDistancesFraction, meanDistanceToCentroidNode};
    }

    public static String getFeatureNames() {
        return "," + "normalizedDistanceStdDeviation" +
                "," + "distinctDistancesFraction" +
                "," + "meanDistanceToCentroidNode";
    }
}
