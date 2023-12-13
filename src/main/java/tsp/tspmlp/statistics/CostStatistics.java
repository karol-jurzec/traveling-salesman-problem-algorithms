package src.main.java.tsp.tspmlp.statistics;

import src.main.java.tsp.tspmlp.TspMlpFeatures;

public class CostStatistics {
    public double distanceCostMean;
    public double distanceStdDeviation;
    public double distanceSkew;

    public CostStatistics(double [][]costMatrix) {
        computeCostStatistics(costMatrix);
    }

    private void computeCostStatistics(double [][]costMatrix) {

        int n = costMatrix.length;
        double[] distances = new double[n * n];
        int index = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distances[index++] = costMatrix[i][j];
            }
        }

        double mean = TspMlpFeatures.computeMean(distances);
        double stdDeviation = TspMlpFeatures.computeStdDeviation(distances, mean);
        double skewness = TspMlpFeatures.computeSkewness(distances, mean, stdDeviation);

        this.distanceCostMean = mean;
        this.distanceStdDeviation = stdDeviation;
        this.distanceSkew = skewness;
    }

    @Override
    public String toString() {
        return "," + distanceCostMean + "," + distanceStdDeviation + "," + distanceSkew;
    }

    public static String getFeatureNames() {
        return "," + "distCostMean" + "," + "distStdDeviation" + "," + "distSkew";
    }

    public double[] toArray() {
        return new double[] {distanceCostMean, distanceStdDeviation, distanceSkew};
    }


}
