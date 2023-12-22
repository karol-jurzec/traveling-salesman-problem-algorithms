package src.main.java.tsp.models;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TspSolution {
    private ArrayList<Point2D> path;
    private double totalDistance;
    private ArrayList<Point2D> points;


    public ArrayList<Edge> edges;

    public TspSolution(ArrayList<Point2D> path, double totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public TspSolution(ArrayList<Point2D> path) {
        this.path = path;
        this.totalDistance = getTotalPathDistance(path);
    }

    public TspSolution(ArrayList<Point2D> path, ArrayList<Edge> edges) {
        this.edges = edges;
        this.totalDistance = getTotalPathDistance(path);
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public ArrayList<Point2D> getPath() {
        return path;
    }

    public void setPath(ArrayList<Point2D> path) {
        this.path = path;
    }

    public void setTotalDistance(double distance) {
        this.totalDistance = distance;
    }

    private double getTotalPathDistance(ArrayList<Point2D> path) {
        double sum = 0;
        for(int i = 0; i < path.size(); ++i) {
            var next = path.get((i+1)%path.size());
            sum = sum + path.get(i).distance(next);
        }
        return sum;
    }

    public static double getTotalPathDistanceForPoints(ArrayList<Point2D> path) {
        double sum = 0;
        for(int i = 0; i < path.size(); ++i) {
            var next = path.get((i+1)%path.size());
            sum = sum + path.get(i).distance(next);
        }
        return sum;
    }
}
