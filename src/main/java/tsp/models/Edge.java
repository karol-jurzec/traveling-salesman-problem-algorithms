package src.main.java.tsp.models;

import java.awt.*;
import java.awt.geom.Point2D;

public class Edge {
    public Point source, destination;
    public double weight;

    public Edge(Point source, Point destination) {
        this.source = source;
        this.destination = destination;
        this.weight = source.distance(destination);
    }

    public Edge(Point2D source, Point2D destination) {
        this.source = new Point(source);
        this.destination = new Point(destination);
        this.weight = source.distance(destination);
    }
}