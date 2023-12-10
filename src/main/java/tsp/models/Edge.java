package src.main.java.tsp.models;

import java.awt.geom.Point2D;

public class Edge {
    public Point2D source, destination;
    public double weight;

    public Edge(Point2D source, Point2D destination) {
        this.source = source;
        this.destination = destination;
        this.weight = source.distance(destination);
    }
}