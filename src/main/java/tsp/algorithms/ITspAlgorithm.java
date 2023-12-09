package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public interface ITspAlgorithm {
    public TspSolution solve(TspInstance tspInstance);
}
