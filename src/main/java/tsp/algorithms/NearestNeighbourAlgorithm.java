package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NearestNeighbourAlgorithm implements ITspAlgorithm {

    private ArrayList<City> NNAlgorithm(ArrayList<City> cities) {
        ArrayList<City> nnPath = new ArrayList<>(Arrays.asList(cities.get(0)));
        HashMap<City, Boolean> visited = new HashMap<>( );

        for(var city : cities) {
            visited.put(city, false);
        }

        while(nnPath.size() < cities.size()) {
            City curr = nnPath.get(nnPath.size() - 1);
            City nearest = new City("XYZ", 0, 0);
            float weight = Float.MAX_VALUE;

            for(var road : curr.getRoads()) {
                if(!visited.get(road.getDestination()) && road.getWeight() < weight) {
                    weight = road.getWeight();
                    nearest = road.getDestination();
                }
            }

            visited.put(nearest, true);
            nnPath.add(nearest);
        }

        return nnPath;
    }


    @Override
    public TspSolution solve(ArrayList<City> cities) {
        ArrayList <City> ans = NNAlgorithm(cities);
        return new TspSolution(ans);
    }

    @Override
    public void printAlgorithm(ArrayList<ArrayList<City>> steps) {

    }
}
