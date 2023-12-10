package src.main.java.tsp.tspmlp;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GeometricStatistics {

    //geometric features
    private double rectangleArea; // rectangle are which contains all values


    // convex hull O(n(log(n)) Graham
    private double convexHullAreaNormalized;
    private double convexHullNodeFraction; // fraction of nodes on convex hull

    private double convexHullDistToContourMean;//
    private double convexHullDistToContourStdDeviation;
    private double convexHullDistToContourMin;
    private double convexHullDistToContourMax;
    private double convexHullDistToContourMedian;

    private double convexHullLengthMean;
    private double convexHullLengthStdDeviation;
    private double convexHullLengthMin;
    private double convexHullLengthMax;
    private double convexHullLengthMedian;

    public GeometricStatistics(ArrayList<Point2D> points) {
        computeGeometricFeatures(points);
    }

    private void computeGeometricFeatures(ArrayList<Point2D> points) {
        var convexHull = new ConvexHullGraham(points).computeConvexHull();

        this.rectangleArea = computeRectangleAreaContainingPoints(points);
        this.convexHullAreaNormalized = computeConvexHullArea(convexHull);
        this.convexHullNodeFraction = convexHull.size() / points.size();

        double[] convexHullDistances = computeDistancesToConvexHull(points, convexHull);

        computeConvexHullDistancesStatistics(convexHullDistances);
        computeConvexHullEdgeLengthStatistics(convexHull);
    }

    private void computeConvexHullEdgeLengthStatistics(ArrayList<Point2D> convexHull) {
        double[] distances = calculateEdgeLengths(convexHull);

        double mean = TspMlpFeatures.computeMean(distances);
        double stdDeviation = TspMlpFeatures.computeStdDeviation(distances, mean);
        double min = Arrays.stream(distances).min().getAsDouble();
        double max = Arrays.stream(distances).max().getAsDouble();
        double median = computeMedian(distances);

        this.convexHullLengthMean = mean;
        this.convexHullLengthStdDeviation = stdDeviation;
        this.convexHullLengthMin = min;
        this.convexHullLengthMax = max;
        this.convexHullLengthMedian = median;
    }

    private double[] calculateEdgeLengths(ArrayList<Point2D> convexHull) {
        ArrayList<Double> edgeLengths = new ArrayList<>();

        int n = convexHull.size();

        for (int i = 0; i < n; i++) {
            int next = (i + 1) % n; // Wrap around for the last vertex

            double length = convexHull.get(i).distance(convexHull.get(next));
            edgeLengths.add(length);
        }

        return edgeLengths.stream().mapToDouble(i -> i).toArray();
    }

    private void computeConvexHullDistancesStatistics(double[] distances) {

        double mean = TspMlpFeatures.computeMean(distances);
        double stdDeviation = TspMlpFeatures.computeStdDeviation(distances, mean);
        double min = Arrays.stream(distances).min().getAsDouble();
        double max = Arrays.stream(distances).max().getAsDouble();
        double median = computeMedian(distances);

        this.convexHullDistToContourMean = mean;
        this.convexHullDistToContourStdDeviation = stdDeviation;
        this.convexHullDistToContourMin = min;
        this.convexHullDistToContourMax = max;
        this.convexHullDistToContourMedian = median;
    }

    private double computeMedian(double[] distances) {
        Arrays.sort(distances);
        double median;
        if (distances.length % 2 == 0)
            median = (distances[distances.length/2] + distances[distances.length/2 - 1])/2;
        else
            median = distances[distances.length/2];
        return median;
    }

    private double computeRectangleAreaContainingPoints(ArrayList<Point2D> points) {
        // Find the minimum and maximum x-coordinates and y-coordinates
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Point2D point : points) {
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }

        // Calculate the width and height of the rectangle
        double width = maxX - minX;
        double height = maxY - minY;

        // Calculate the area of the rectangle
        double area = width * height;

        return area;
    }

    private double computeConvexHullArea(ArrayList<Point2D> convexHull) {
        int n = convexHull.size();
        double area = 0.0;

        for (int i = 0; i < n; i++) {
            int next = (i + 1) % n;
            area += convexHull.get(i).getX() * convexHull.get(next).getY();
            area -= convexHull.get(next).getX() * convexHull.get(i).getY();
        }

        area = Math.abs(area) / 2.0;
        return area;
    }

    private double computeDistance(Point2D point, ArrayList<Point2D> convexHull) {
        double minDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < convexHull.size(); i++) {
            int next = (i + 1) % convexHull.size();

            double distance = computeDistanceToLine(point, convexHull.get(i), convexHull.get(next));
            minDistance = Math.min(minDistance, distance);
        }

        return minDistance;
    }

    private double[] computeDistancesToConvexHull(ArrayList<Point2D> points, ArrayList<Point2D> convexHull) {
        ArrayList<Double> distances = new ArrayList<>();

        for (Point2D point : points) {
            if (!isPointInsideConvexHull(point, convexHull)) {
                double distance = computeDistance(point, convexHull);
                distances.add(distance);
            }
        }


        return distances.stream().mapToDouble(i -> i).toArray();
    }

    private double computeDistanceToLine(Point2D point, Point2D start, Point2D end) {
        double x = point.getX();
        double y = point.getY();
        double x1 = start.getX();
        double y1 = start.getY();
        double x2 = end.getX();
        double y2 = end.getY();

        double num = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1);
        double den = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

        return num / den;
    }

    private boolean isPointInsideConvexHull(Point2D point, ArrayList<Point2D> convexHull) {
        return convexHull.contains(point);
    }
}
