package src.main.java.tsp.tspmlp;

import src.main.java.tsp.models.Edge;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

public class ConvexHullGraham {
    private static double minY = Double.MAX_VALUE;
    private static double minX = Double.MAX_VALUE;

    ArrayList<Point2D> points;

    public ConvexHullGraham(ArrayList<Point2D> points) {
        this.points = points;
    }

    public ArrayList<Point2D> computeConvexHull() {
        findPivot(points);

        // Sort points based on polar angle
        Collections.sort(points, new Comparator<Point2D>() {
            public int compare(Point2D p1, Point2D p2) {
                double angle1 = Math.atan2(p1.getY() - minY, p1.getX() - minX);
                double angle2 = Math.atan2(p2.getY() - minY, p2.getX() - minX);

                if (angle1 < angle2) return -1;
                if (angle1 > angle2) return 1;

                return Double.compare(p1.distance(minX, minY), p2.distance(minX, minY));
            }
        });

        // Build the convex hull
        Stack<Point2D> hull = new Stack<>();
        hull.push(points.get(0));
        hull.push(points.get(1));

        for (int i = 2; i < points.size(); i++) {
            while (hull.size() >= 2 && !isLeftTurn(hull.elementAt(hull.size() - 2), hull.peek(), points.get(i))) {
                hull.pop();
            }
            hull.push(points.get(i));
        }

        return new ArrayList<>(hull);
    }

    private void findPivot(ArrayList<Point2D> points) {
        for (Point2D point : points) {
            if (point.getY() < minY || (point.getY() == minY && point.getX() < minX)) {
                minY = point.getY();
                minX = point.getX();
            }
        }
    }

    private boolean isLeftTurn(Point2D a, Point2D b, Point2D c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX()) > 0;
    }
}
