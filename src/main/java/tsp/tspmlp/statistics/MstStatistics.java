package src.main.java.tsp.tspmlp.statistics;

import src.main.java.tsp.helpers.KruskalMST;
import src.main.java.tsp.models.Edge;
import src.main.java.tsp.tspmlp.TspMlpFeatures;

import java.awt.geom.Point2D;
import java.util.*;

public class MstStatistics {
    // minimum spanning tree - Kruskal method in O(E(log(V)))
    private double mstSum;
    private double mstMean;
    private double mstStdDeviation;
    private double mstSkew;


    // node degree for mst
    private double mstNodeDegreeMean;
    private double mstNodeDegreeStdDeviation;
    private double mstNodeDegreeSkew;

    // node depth
    private double mstNodeDepthMean;
    private double mstNodeDepthStdDeviation;
    private double mstNodeDepthMax;
    private double mstNodeDepthMedian;

    public MstStatistics(ArrayList<Point2D> points) {
        computeMstStatistics(points);
    }

    public double[] computeDistanceArray(ArrayList<Edge> edges) {
        double[] dist = new double[edges.size()];

        for(int i = 0; i < edges.size(); ++i) {
            dist[i] = edges.get(i).weight;
        }

        return dist;
    }

    private void computeMstStatistics(ArrayList<Point2D> points) {
        var mst = new KruskalMST(points).findMST();
        double[] dist = computeDistanceArray(mst);

        computeDistanceStatistics(dist);
        computeDegreeStatistics(points, mst);
        computeDepthStatistics(points, mst);
    }

    private void computeDistanceStatistics(double[] dist) {
        var sum = Arrays.stream(dist).sum();
        var mean = TspMlpFeatures.computeMean(dist);
        var stdDev = TspMlpFeatures.computeStdDeviation(dist, mean);
        var skewness = TspMlpFeatures.computeSkewness(dist, mean, stdDev);

        this.mstSum = sum;
        this.mstMean = mean;
        this.mstStdDeviation = stdDev;
        this.mstSkew = skewness;
    }

    private int[] computeNodeDegrees(ArrayList<Point2D> points, ArrayList<Edge> edges) {
        int[] degrees = new int[points.size()];

        for (Edge edge : edges) {
            degrees[points.indexOf(edge.source)]++;
            degrees[points.indexOf(edge.destination)]++;
        }

        return degrees;
    }

    private void computeDegreeStatistics(ArrayList<Point2D> points, ArrayList<Edge> edges) {
        int[] degrees = computeNodeDegrees(points, edges);

        double mean = TspMlpFeatures.computeMean(degrees);
        double stdDev = TspMlpFeatures.computeStdDeviation(degrees, mean);
        double skewness = TspMlpFeatures.computeSkewness(degrees, mean, stdDev);

        this.mstNodeDegreeMean = mean;
        this.mstNodeDegreeStdDeviation = stdDev;
        this.mstNodeDegreeSkew = skewness;
    }

    // bfs used for calculating nodes depth
    private void bfs(int rootIndex, int[] depths, boolean[] visited, ArrayList<Edge> edges, ArrayList<Point2D> points) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(rootIndex);
        visited[rootIndex] = true;
        depths[rootIndex] = 0;

        while (!queue.isEmpty()) {
            int currentIndex = queue.poll();

            for (Edge edge : edges) {
                int neighborIndex;
                if (edge.source.equals(points.get(currentIndex)) && !visited[points.indexOf(edge.destination)]) {
                    neighborIndex = points.indexOf(edge.destination);
                } else if (edge.destination.equals(points.get(currentIndex)) && !visited[points.indexOf(edge.source)]) {
                    neighborIndex = points.indexOf(edge.source);
                } else {
                    continue;
                }

                queue.offer(neighborIndex);
                visited[neighborIndex] = true;
                depths[neighborIndex] = depths[currentIndex] + 1;
            }
        }
    }

    private int[] computeNodeDepths(int rootIndex, ArrayList<Edge> edges, ArrayList<Point2D> points) {
        int n = points.size();
        int[] depths = new int[n];
        boolean[] visited = new boolean[n];

        // Use BFS to compute depths
        bfs(rootIndex, depths, visited, edges, points);

        return depths;
    }

    private double computeMedian(int[] nodeDepths) {
        Arrays.sort(nodeDepths);
        double median;
        if (nodeDepths.length % 2 == 0) {
            median = (nodeDepths[nodeDepths.length / 2 - 1] + nodeDepths[nodeDepths.length / 2]) / 2.0;
        } else {
            median = nodeDepths[nodeDepths.length / 2];
        }
        return median;
    }

    private void computeDepthStatistics(ArrayList<Point2D> points, ArrayList<Edge> edges) {
        int[] nodeDepths = computeNodeDepths(0, edges, points);

        double mean = TspMlpFeatures.computeMean(nodeDepths);
        double stdDev = TspMlpFeatures.computeStdDeviation(nodeDepths, mean);
        double maxDepth = Arrays.stream(nodeDepths).max().getAsInt();
        double median = computeMedian(nodeDepths);

        this.mstNodeDepthMean = mean;
        this.mstNodeDepthStdDeviation = stdDev;
        this.mstNodeDepthMax = maxDepth;
        this.mstNodeDepthMedian = median;
    }

    @Override
    public String toString() {
        return "," + mstSum +
                "," + mstMean +
                "," + mstStdDeviation +
                "," + mstSkew +
                "," + mstNodeDegreeMean +
                "," + mstNodeDegreeStdDeviation +
                "," + mstNodeDegreeSkew +
                "," + mstNodeDepthMean +
                "," + mstNodeDepthStdDeviation +
                "," + mstNodeDepthMax +
                "," + mstNodeDepthMedian;
    }

    public double[] toArray() {
        return new double[] {mstSum, mstMean, mstStdDeviation, mstSkew, mstNodeDegreeMean,
        mstNodeDegreeStdDeviation, mstNodeDegreeSkew, mstNodeDepthMean, mstNodeDepthStdDeviation,
        mstNodeDepthMax, mstNodeDepthMedian};
    }

    public static String getFeatureNames() {
        return "," + "mstSum" +
                "," + "mstMean" +
                "," + "mstStdDeviation" +
                "," + "mstSkew" +
                "," + "mstNodeDegreeMean" +
                "," + "mstNodeDegreeStdDeviation" +
                "," + "mstNodeDegreeSkew" +
                "," + "mstNodeDepthMean" +
                "," + "mstNodeDepthStdDeviation" +
                "," + "mstNodeDepthMax" +
                "," + "mstNodeDepthMedian";
    }
}

