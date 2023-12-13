package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.awt.geom.Point2D;
import java.text.CollationKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TwoOptAlgorithm implements ITspAlgorithm {

    private ArrayList<Point2D> twoOptSwap(ArrayList<Point2D> cities, Point2D v1, Point2D v2) {
        ArrayList newGraph = new ArrayList<>(cities);

        var vOneIndex = cities.indexOf(v1);
        var vTwoIndex = cities.indexOf(v2);

        Collections.reverse(newGraph.subList(vOneIndex + 1 , vTwoIndex + 1)) ;

        return newGraph;
    }

    private double calculateDelta(Point2D v10, Point2D v11, Point2D v20, Point2D v21) {
        return - v10.distance(v11) - v20.distance(v21) + v10.distance(v20) + v21.distance(v11);
    }

    public TspSolution solve(TspInstance tspInstance) {
        var nn = new NearestNeighbourAlgorithm().solve(tspInstance).getPath();
        var points = tspInstance.getPointCollection();

        var bestTour = nn;
        var totalDistance = TspSolution.getTotalPathDistanceForPoints(nn);
        var foundImprovment = true;

        while(foundImprovment) {
            foundImprovment = false;

            for(int i = 0; i < points.size() - 2; ++i) {
                for(int j = i + 1; j < points.size(); ++j) {
                    double deltaLen = calculateDelta(bestTour.get(i), bestTour.get(i+1), bestTour.get(j), bestTour.get(j+1));
                    if(deltaLen < -0.001) {
                        bestTour = twoOptSwap(bestTour, bestTour.get(i), bestTour.get(j));
                        totalDistance += deltaLen;
                        foundImprovment = true;
                    }
                }
            }
        }

        return new TspSolution(bestTour)    ;
    }

    @Override
    public String toString() {
        return "2";
    }
}
