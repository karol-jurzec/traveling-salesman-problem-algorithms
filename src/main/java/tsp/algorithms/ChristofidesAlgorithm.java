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

    private ArrayList<Edge> mstToPath(Point point) {
        Queue<Point> queue = new LinkedList<>();
        HashMap<Point, Boolean> visited = new HashMap<>();
        ArrayList<Edge> edges = new ArrayList<>();
        queue.add(point);

        while(!queue.isEmpty()) {
            var curr = queue.poll();
            if(visited.get(curr) == null) {
                visited.put(curr, true);
                for(var n : curr.neighbours) {
                    if(visited.get(n) == null) {
                        edges.add(new Edge(curr, n));
                        queue.add(n);
                    }
                }
            }
        }
        return edges;
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
        HashMap<Point, Boolean> visited = new HashMap<>();

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

        System.out.println();
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

    private boolean isBridge(Point u, Point v) {
        if (u.neighbours.size() == 1) {
            return true;
        }

        u.neighbours.remove(v);
        v.neighbours.remove(u);


        boolean canConnect = dfsCheckConnection(v, u, new HashSet<>());

        u.neighbours.add(v);
        v.neighbours.add(u);

        return !canConnect;
    }

    private boolean dfsCheckConnection(Point current, Point target, HashSet<Edge> visitedEdges) {
        if (current == target) {
            return true;
        }

        for (Point neighbor : current.neighbours) {
            Edge edge = new Edge(current, neighbor);
            if (!visitedEdges.contains(edge)) {
                visitedEdges.add(edge);
                if (dfsCheckConnection(neighbor, target, visitedEdges)) {
                    return true;
                }
            }
        }
        return false;
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
                next = current.neighbours.get(0);
            }
                if (next != null) {
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

    private ArrayList<Point> oddDegreeVert = new ArrayList<>();

    @Override
    public TspSolution solve(TspInstance tspInstance) {
        oddDegreeVert = new ArrayList<>();

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
