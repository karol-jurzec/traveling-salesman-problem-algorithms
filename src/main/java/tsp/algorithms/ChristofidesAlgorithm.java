package src.main.java.tsp.algorithms;

import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.DenseEdmondsMaximumCardinalityMatching;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import src.main.java.tsp.helpers.KruskalMST;
import src.main.java.tsp.models.Edge;
import src.main.java.tsp.models.Point;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.awt.geom.Point2D;
import java.security.cert.PolicyNode;
import java.util.*;

public class ChristofidesAlgorithm implements ITspAlgorithm {


    /*

    steps for Christofides aglorithm:

    1. Create a minimum spanning tree T of G.
    2. Let O be the set of vertices with odd degree in T. By the handshaking lemma, O has an even number of vertices.
    3. Find a minimum-weight perfect matching M in the induced subgraph given by the vertices from O.
    4. Combine the edges of M and T to form a connected multigraph H in which each vertex has even degree.
    5. Form an Eulerian circuit in H.
    6. Make the circuit found in previous step into a Hamiltonian circuit by skipping repeated vertices (shortcutting).

    */


    private Point getMst(ArrayList<Point2D> points) {
        KruskalMST kruskalMST = new KruskalMST(points);
        var mst = kruskalMST.findMST();
        return convertListToTree(mst);
    }

    private Point convertListToTree(List<Edge> edges) {
        if (edges.isEmpty()) {
            return null;
        }

        Map<Point, Point> pointMap = new HashMap<>();
        for (Edge edge : edges) {
            Point srcPoint = pointMap.computeIfAbsent(edge.source, k -> { return edge.source; });
            Point destPoint = pointMap.computeIfAbsent(edge.destination, k -> { return edge.destination; });

            srcPoint.addNeighbour(destPoint);
            destPoint.addNeighbour(srcPoint);
        }

        return pointMap.values().iterator().next();
    }

    // degree is odd when number of neighbours is odd
    private boolean isDegreeOdd(int size) {
        return (size % 2) != 0;
    }

    private void computeOddDegreeVertices(Point p) {
        Queue<Point> queue = new LinkedList<Point>();
        queue.add(p);

        while(!queue.isEmpty()) {

            var curr = queue.poll();

            if( visited.get(curr) == null ) {

                visited.put(curr, true);
                if(isDegreeOdd(curr.neighbours.size())) {
                    oddDegreeVert.add(curr);
                }

                for(var n : curr.neighbours) {
                    queue.add(n);
                }
            }
        }
    }


    // finding min-weight perfect matching from list of vertices
    private ArrayList<Edge> findMinWeightMatching(ArrayList<Point> points) {
        // Assuming all points have odd degree
        ArrayList<Edge> allEdges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                allEdges.add(new Edge(points.get(i), points.get(j)));
            }
        }

        Collections.sort(allEdges, Comparator.comparingDouble(e -> e.weight));
        ArrayList<Edge> matching = new ArrayList<>();
        HashSet<Point> matchedPoints = new HashSet<>();

        for (Edge edge : allEdges) {
            if (!matchedPoints.contains(edge.source) && !matchedPoints.contains(edge.destination)) {
                matching.add(edge);
                matchedPoints.add(edge.source);
                matchedPoints.add(edge.destination);
            }
        }

        return matching;
    }

    private void combineEdgesToGraph(ArrayList<Edge> edges) {
        for(var e : edges) {
            e.source.neighbours.add(e.destination);
            e.destination.neighbours.add(e.source);
        }
    }


    // fleury algorithm for finding euler circuit
    private boolean isBridge(Point u, Point v) {
        // If the size of u's neighbor list is 1, then it is a bridge
        if (u.neighbours.size() == 1) {
            return true;
        }

        // Count the number of reachable vertices from u
        HashMap<Point, Boolean> isVisited = new HashMap<>();
        int count1 = dfsCount(u, isVisited);

        // Remove the edge (u, v) and count reachable vertices from u
        u.neighbours.remove(v);
        v.neighbours.remove(u);

        int count2 = dfsCount(u, isVisited);

        // Add the edge back to the graph
        u.addNeighbour(v);
        v.addNeighbour(u);

        // If the number of reachable vertices decreases, then it is a bridge
        return (count1 > count2);
    }

    private int dfsCount(Point v, HashMap<Point, Boolean> isVisited) {
        isVisited.put(v, true);
        int count = 1;
        for (Point adj : v.neighbours) {
            if (isVisited.get(adj) == null) {
                count += dfsCount(adj, isVisited);
            }
        }
        return count;
    }

    public ArrayList<Point> findEulerCircuit(Point p) {
        ArrayList<Point> circuit = new ArrayList<>();

        Point current = p;

        while (current != null) {
            Point next = null;
            for (Point adj : new ArrayList<>(current.neighbours)) {
                if (!isBridge(current, adj)) {
                    next = adj;
                    break;
                }
            }

            if (next == null && !current.neighbours.isEmpty()) {
                // All edges are bridges; pick any edge
                next = current.neighbours.get(0);
            }

            if (next != null) {
                // Add the edge to the circuit
                circuit.add(current);
                current.neighbours.remove(next);
                next.neighbours.remove(current);
                current = next;
            } else {
                // No more edges to traverse
                circuit.add(current);
                break;
            }
        }

        return circuit;
    }

    public ArrayList<Point2D> removeDuplicates(ArrayList<Point> points) {
        Set<Point2D> pointsSet = new LinkedHashSet<>(points);

        var path = new ArrayList<>(pointsSet);
        path.add(path.get(0));

        return path;
    }

    private HashMap<Point, Boolean> visited = new HashMap<>();
    private ArrayList<Point> oddDegreeVert = new ArrayList<>();


    @Override
    public TspSolution solve(TspInstance tspInstance) {
        // compute mst tree
        var mst = getMst(tspInstance.getPointCollection());

        // compute odd degree vertices
        computeOddDegreeVertices(mst);

        // computer min weight matching and connect it to tree
        var minWMatch = findMinWeightMatching(oddDegreeVert);
        combineEdgesToGraph(minWMatch);

        var eulerCircuit = findEulerCircuit(mst);
        var path = removeDuplicates(eulerCircuit);


        return new TspSolution(path);
    }
}
