package src.main.java.tsp.models;

import java.awt.geom.Point2D;
import java.util.ArrayList;



public class Point extends Point2D.Double {
    public ArrayList<Point> neighbours;

    public Point(double x, double y) {
        super(x, y);
        neighbours = new ArrayList<>();
    }

    public Point(Point2D p) {
        super(p.getX(), p.getY());
        neighbours = new ArrayList<>();
    }

    public void addNeighbour(Point point) {
        neighbours.add(point);
    }

    public static ArrayList<Point2D> convertPointArrayToPoint2D(ArrayList<Point> points) {
        ArrayList<Point2D> points2D = new ArrayList<>();

        for(var p : points) {
            points2D.add(p);
        }

        return points2D;
    }

    public static ArrayList<Point> convertPoint2dArrayToPoint(ArrayList<Point2D> points) {
        ArrayList<Point> points2D = new ArrayList<>();

        for(var p : points) {
            points2D.add(new Point(p.getX(), p.getY()));
        }

        return points2D;
    }

}