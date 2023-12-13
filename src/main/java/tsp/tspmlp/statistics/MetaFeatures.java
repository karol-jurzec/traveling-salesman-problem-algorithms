package src.main.java.tsp.tspmlp.statistics;

import java.util.*;

public class MetaFeatures {

    public int numberOfVertices;
    public int numberOfEdges;
    public double eLow;
    public double eHigh;
    public double eAvg;
    public double eMedian;
    public int eLavg;
    public double vLower;


    public MetaFeatures(int n, double[][] distances){
        this.numberOfVertices = n;
        this.numberOfEdges = (n*(n-1))/2;
        computeMetaFeatures(distances);
    }

    private double computeLowestEdgeCost(double[] dist) {
        return Arrays.stream(dist).min().getAsDouble();
    }

    private double computeHighestEdgeCost(double[] dist) {
        return Arrays.stream(dist).max().getAsDouble();
    }

    private double computeAverageEdgesCost(double[] dist) {
        double sum = Arrays.stream(dist).sum();
        return (sum/dist.length);
    }

    private double computeQuantityOfEdgeCostMode(double[] dist) {
        Map<Double, Integer> frequencyMap = new HashMap<>();

        // Count the frequency of each element
        for (double value : dist) {
            if (frequencyMap.containsKey(value)) {
                frequencyMap.put(value, frequencyMap.get(value) + 1);
            } else {
                frequencyMap.put(value, 1);
            }
        }

        Set<Double> modes = new HashSet<>();
        int maxFrequency = 0;

        // Find the modes
        for (Map.Entry<Double, Integer> entry : frequencyMap.entrySet()) {
            double value = entry.getKey();
            int frequency = entry.getValue();

            if (frequency == maxFrequency) {
                modes.add(value);
            } else if (frequency > maxFrequency) {
                maxFrequency = frequency;
                modes.clear();
                modes.add(value);
            }
        }

        return modes.size();
    }

    private double computeFrequencyOfEdgeCostMode(double[] dist) {
        Map<Double, Integer> frequencyMap = new HashMap<>();

        // Count the frequency of each element
        for (double value : dist) {
            if (frequencyMap.containsKey(value)) {
                frequencyMap.put(value, frequencyMap.get(value) + 1);
            } else {
                frequencyMap.put(value, 1);
            }
        }


        int maxFrequency = Collections.max(frequencyMap.entrySet(), Map.Entry.comparingByValue()).getValue();
        return maxFrequency;
    }

    private double[] findModes(double[] edgeCosts) {
        Map<Double, Integer> frequencyMap = new HashMap<>();

        // Count the frequency of each edge cost
        for (double cost : edgeCosts) {
            frequencyMap.put(cost, frequencyMap.getOrDefault(cost, 0) + 1);
        }

        int maxFrequency  = 0;

        // Find the maximum frequency
        for (int frequency : frequencyMap.values()) {
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
            }
        }

        final int frequency = maxFrequency;

        // Find modes
        return frequencyMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(frequency))
                .mapToDouble(Map.Entry::getKey)
                .toArray();
    }

    private double computeModeAvg(double[] costs) {
        double[] modes = findModes(costs);

        if (modes.length == 0) {
            return 0.0; // Return 0 for an empty array
        }

        double sum = 0.0;
        for (double value : modes) {
            sum += value;
        }

        return sum / modes.length;
    }

    public double computeMedian(double[] values) {
        // Sort the array
        Arrays.sort(values);

        int length = values.length;

        // If the length is odd, return the middle element
        if (length % 2 != 0) {
            return values[length / 2];
        }

        // If the length is even, return the average of the two middle elements
        int middleIndex1 = length / 2 - 1;
        int middleIndex2 = length / 2;

        return (values[middleIndex1] + values[middleIndex2]) / 2.0;
    }

    public int countLowerThanAverage(double[] values, double average) {
        int count = 0;
        for (double value : values) {
            if (value < average) {
                count++;
            }
        }
        return count;
    }

    public double calculateSumOfLowestEdges(double[] values, int V) {
        if (V <= 0) {
            return 0.0; // Return 0 if V is non-positive
        }

        // Sort the array in ascending order
        Arrays.sort(values);

        // Sum the first V elements
        double sum = 0.0;
        for (int i = 0; i < Math.min(V, values.length); i++) {
            sum += values[i];
        }

        return sum;
    }

    public void computeMetaFeatures(double[][] distMatrix) {
        int n = distMatrix.length;
        double[] dist = new double[(n*(n-1))/2];

        for(int i = 0, k = 0; i < distMatrix.length; ++i) {
            for(int j = i + 1; j < distMatrix.length; ++j) {
                if(i != j) {
                    dist[k++] = distMatrix[i][j];
                }
            }
        }

        this.eLow = computeLowestEdgeCost(dist);
        this.eHigh = computeHighestEdgeCost(dist);
        this.eAvg = computeAverageEdgesCost(dist);
        this.eMedian = computeMedian(dist);
        this.eLavg = countLowerThanAverage(dist, eAvg);
        this.vLower = calculateSumOfLowestEdges(dist, this.numberOfVertices);
    }

    @Override
    public String toString() {
        return
                numberOfVertices +
                "," + numberOfEdges +
                "," + eLow +
                "," + eHigh +
                "," + eAvg +
                "," + eMedian +
                "," + eLavg +
                "," + vLower;
    }
}
