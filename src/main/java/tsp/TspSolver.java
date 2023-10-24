package src.main.java.tsp;

import src.main.java.tsp.algorithms.ITspAlgorithm;
import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;

import java.util.ArrayList;

public class TspSolver {
    private ITspAlgorithm algorithm;

    public TspSolver(ITspAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public TspSolution solve(ArrayList<City> cities) {
        return algorithm.solve(cities);
    }
}
