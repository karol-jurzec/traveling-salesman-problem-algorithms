package src.main.java.tsp.tspmlp;

import src.main.java.tsp.models.TspInstance;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class TspMlpFeatures {
    private TspInstance tspInstance;


    private int numberOfNodes;
    private double requiredWorkingTime;

    // cost matrix
    CostStatistics costStatistics;

    // mst statistics
    MstStatistics mstStatistics;

    // node distribution features
    NodeDistributionStatistics nodeDistributionStatistics;

    // geometric features
    GeometricStatistics geometricStatistics;

    public TspMlpFeatures(double requiredTime) {
        this.requiredWorkingTime = requiredTime;
    }

    public void computeFeatures(TspInstance tspInstance) {
        var points = tspInstance.getPointCollection();
        var distMatrix = tspInstance.getDistanceMatrix();

        this.tspInstance = tspInstance;
        this.numberOfNodes = points.size();

        //cost matrix
        costStatistics = new CostStatistics(distMatrix);

        //mst kruskal method
        mstStatistics = new MstStatistics(points);

        //node distribution
        nodeDistributionStatistics = new NodeDistributionStatistics(points, distMatrix);

        //geometric features




    }

    public static double computeMean(double[] distances) {
        double sum = 0.0;

        for (double distance : distances) {
            sum += distance;
        }

        double mean = sum / distances.length;
        return mean;
    }

    public static double computeMean(int[] distances) {
        double sum = 0.0;

        for (double distance : distances) {
            sum += distance;
            if(sum == 0) {
                System.out.println();
            }
        }

        double mean = sum / distances.length;
        return mean;
    }

    public static double computeStdDeviation(double[] distances, double mean) {
        int n = distances.length;
        double sumSquare = 0.0;

        for (double distance : distances) {
            sumSquare += distance * distance;
        }

        double variance = (sumSquare / n) - (mean * mean);

        if(variance > 0) {
            double stdDeviation = Math.sqrt(variance);
            return stdDeviation;
        }

        return 0.0;
    }
    public static double computeStdDeviation(int[] distances, double mean) {
        int n = distances.length;
        double sumSquare = 0.0;

        for (double distance : distances) {
            sumSquare += distance * distance;
        }

        double variance = (sumSquare / n) - (mean * mean);

        if(variance > 0) {
            double stdDeviation = Math.sqrt(variance);
            return stdDeviation;
        }

        return 0.0;
    }


    public static double computeSkewness(double[] distances, double mean, double stdDeviation) {
        int n = distances.length;
        double skewness = 0.0;

        for (double value : distances) {
            skewness += Math.pow((value - mean) / stdDeviation, 3);
        }

        return (skewness * n) / ((n - 1) * (n - 2));
    }
    public static double computeSkewness(int[] distances, double mean, double stdDeviation) {
        int n = distances.length;
        double skewness = 0.0;

        for (double value : distances) {
            skewness += Math.pow((value - mean) / stdDeviation, 3);
        }

        return (skewness * n) / ((n - 1) * (n - 2));
    }


    public void tspMlpTest() {

    }

}
