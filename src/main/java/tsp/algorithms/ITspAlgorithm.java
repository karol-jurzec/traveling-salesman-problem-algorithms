package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;

import java.util.ArrayList;

public interface ITspAlgorithm {
    public TspSolution solve(ArrayList<City> cities);
    public void printAlgorithm(ArrayList<ArrayList<City>> steps);
}
