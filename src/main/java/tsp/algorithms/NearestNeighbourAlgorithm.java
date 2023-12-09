package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NearestNeighbourAlgorithm implements ITspAlgorithm {

    private ArrayList<Point2D> NNAlgorithm(TspInstance tspInstance) {

        ArrayList<Point2D> points = tspInstance.getPointCollection();
        ArrayList<Integer> ids = tspInstance.getIdCollection();
        //double[][] distMatrix = tspInstance.getDistanceMatrix();

        ArrayList<Integer> nnIdPath = new ArrayList<>(Arrays.asList(ids.get(0)));
        HashMap<Integer, Boolean> visited = new HashMap<>( );

        for(var p : ids) {
            visited.put(p, false);
        }

        while(nnIdPath.size() < points.size()) {
            int curr = nnIdPath.get(nnIdPath.size() - 1);
            int nearest = -1;
            double weight = Float.MAX_VALUE;

            for(int i = 0; i < points.size(); ++i) {
                Point2D p1 = points.get(curr);
                Point2D p2 = points.get(i);
                if(i != curr && !visited.get(i) && p1.distance(p2) < weight) {
                    weight = p1.distance(p2);
                    nearest = i;
                }
            }

            visited.put(curr, true);
            nnIdPath.add(nearest);
        }

        nnIdPath.add(nnIdPath.get(0));

        var nnPath = new ArrayList<Point2D>();

        nnIdPath.forEach(x -> nnPath.add(points.get(x)));

        return nnPath;
    }

    @Override
    public TspSolution solve(TspInstance tspInstance) {
        ArrayList<Point2D> ans = NNAlgorithm(tspInstance);
        return new TspSolution(ans);
    }
}
