package src.main.java.tsp.models;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(source, edge.source) &&
                Objects.equals(destination, edge.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }
}