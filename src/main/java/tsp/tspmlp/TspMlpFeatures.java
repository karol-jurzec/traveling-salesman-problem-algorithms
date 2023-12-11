package src.main.java.tsp.tspmlp;

import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.tspmlp.statistics.CostStatistics;
import src.main.java.tsp.tspmlp.statistics.GeometricStatistics;
import src.main.java.tsp.tspmlp.statistics.MstStatistics;
import src.main.java.tsp.tspmlp.statistics.NodeDistributionStatistics;


public class TspMlpFeatures {
    private TspInstance tspInstance;


    private int numberOfNodes;
    private final double requiredWorkingTime;




    // cost matrix
    CostStatistics costStatistics;

    // mst statistics
    MstStatistics mstStatistics;

    // node distribution features
    NodeDistributionStatistics nodeDistributionStatistics;

    // geometric features
    GeometricStatistics geometricStatistics;



    public TspMlpFeatures(TspInstance tspInstance, double requiredTime) {
        this.requiredWorkingTime = requiredTime;
        this.computeFeatures(tspInstance);
    }

    private void computeFeatures(TspInstance tspInstance) {
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
        geometricStatistics = new GeometricStatistics(points);

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


    @Override
    public String toString() {
        return numberOfNodes + "," + requiredWorkingTime + costStatistics.toString() +
                mstStatistics.toString() + nodeDistributionStatistics.toString() + geometricStatistics.toString();
    }

    public static String getFeatureNames() {
        return "numberOfNodes" + "," + "requiredWorkingTime" + CostStatistics.getFeatureNames() + MstStatistics.getFeatureNames() +
                NodeDistributionStatistics.getFeatureNames() + GeometricStatistics.getFeatureNames();
    }

}
