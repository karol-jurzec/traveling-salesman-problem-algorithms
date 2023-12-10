package src.main.java.tsp.tspmlp;

import src.main.java.tsp.models.Edge;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class KruskalMST {
    private ArrayList<Point2D> vertices;
    private ArrayList<Edge> edges;

    public KruskalMST(ArrayList<Point2D> points) {
        this.vertices = points;
        this.edges = new ArrayList<Edge>();

        initializeEdges();
    }

    public void addEdge(Point2D source, Point2D destination) {
        Edge edge = new Edge(source, destination);
        edges.add(edge);
    }

    public void initializeEdges() {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                Point2D source = vertices.get(i);
                Point2D destination = vertices.get(j);

                addEdge(source, destination);
            }
        }
    }

    private int find(int[] parent, int i) {
        if (parent[i] != i) {
            parent[i] = find(parent, parent[i]); // Path compression
        }
        return parent[i];
    }

    public ArrayList<Edge> findMST() {
        ArrayList<Edge> result = new ArrayList<>();

        // Sort edges based on weight
        Collections.sort(edges, Comparator.comparingDouble(edge -> edge.weight));

        // Create a parent array for union-find
        int[] parent = new int[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            parent[i] = i;
        }

        for (Edge edge : edges) {
            int sourceParent = find(parent, vertices.indexOf(edge.source));
            int destParent = find(parent, vertices.indexOf(edge.destination));

            // Check if including this edge forms a cycle
            if (sourceParent != destParent) {
                // Include the edge in the MST
                result.add(edge);

                // Union the sets of source and destination
                parent[sourceParent] = destParent;
            }
        }

        return result;
    }
}

