package src.main.java.tsp;

import src.main.java.tsp.algorithms.ITspAlgorithm;
import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TspSolver {
    private final ITspAlgorithm algorithm;

    public TspSolver(ITspAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public TspSolution solve(TspInstance tspInstance) {
        return algorithm.solve(tspInstance);
    }

    @Override
    public String toString() {
        return algorithm.toString();
    }
}
