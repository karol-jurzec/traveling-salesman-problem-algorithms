package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;

import java.util.ArrayList;

public class HeldKarpAlgorithm implements ITspAlgorithm {

    // generowanie podzbiorw dla grafu wejsciowego, polega na rekurencyjnym przejsciu przez tablice i
    // dodaniu lub nie dodaniu miasta do zbioru. podzbiory dodawane są w liściach drzewa rekurencyjnego

    public static ArrayList<ArrayList<City>> generateSubsets(ArrayList<City> cities, ArrayList<City> subset,
                                                      ArrayList<ArrayList<City>> subsets, int index) {
        if(index >= cities.size()) {
            subsets.add(new ArrayList<>(subset));
            return subsets;
        }
        subset.add(cities.get(index));
        subsets = generateSubsets(cities, subset, subsets, index + 1);
        subset.remove(subset.size() - 1);
        subsets = generateSubsets(cities, subset, subsets, index + 1);

        return subsets;
    }





    @Override
    public TspSolution solve(ArrayList<City> cities) {
        return null;
    }
}
