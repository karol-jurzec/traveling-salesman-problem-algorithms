package src.main.java.tsp.algorithms;

import src.main.java.tsp.helpers.KruskalMST;
import src.main.java.tsp.models.Edge;
import src.main.java.tsp.models.Point;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

public class TwoApproxAlgorithm implements ITspAlgorithm {
    // creating MST with Kruskal algorithm O(|E|log|V|) -- E - edges, V - vertices

    private ArrayList<Edge> getMst(ArrayList<Point2D> points) {
        KruskalMST kruskalMST = new KruskalMST(points);
        var mst = kruskalMST.findMST();
        return mst;
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

    private void dfs(Point start) {
        Stack<Point> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Point p = stack.pop();

            if (visited.get(p) != null) {
                continue;
            }

            visited.put(p, true);
            path.add(p);

            for (Point n : p.neighbours) {
                if (visited.get(n) == null) {
                    stack.push(n);
                }
            }
        }
    }

    private ArrayList<Point2D> computeDfsPath(Point p) {

        dfs(p);

        if(path.size() == 0 || path.size() == 1) {
            return Point.convertPointArrayToPoint2D(path);
        }

        path.add(path.get(0));
        return Point.convertPointArrayToPoint2D(path);
    }

    private ArrayList<Point> path = new ArrayList<>();
    private HashMap<Point, Boolean> visited = new HashMap<>();


    @Override
    public TspSolution solve(TspInstance tspInstance) {
        var mst = getMst(tspInstance.getPointCollection());
        var mstTree = convertListToTree(mst);
        var path = computeDfsPath(mstTree);

        return new TspSolution(path );
    }
}
