package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;

import java.text.CollationKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TwoOptAlgorithm implements ITspAlgorithm {

    private ArrayList<City> twoOptSwap(ArrayList<City> cities, City v1, City v2) {
        ArrayList newGraph = new ArrayList<>(cities);

        var vOneIndex = cities.indexOf(v1);
        var vTwoIndex = cities.indexOf(v2);

        if(vOneIndex > vTwoIndex) {
            System.out.println();
        }

        Collections.reverse(newGraph.subList(vOneIndex + 1 , vTwoIndex + 1)) ;

        return newGraph;
    }

    private double calculateDelta(City v10, City v11, City v20, City v21) {
        return - v10.distanceToCity(v11) - v20.distanceToCity(v21) + v10.distanceToCity(v20) + v21.distanceToCity(v11);
    }

    @Override
    public TspSolution solve(ArrayList<City> cities) {
        var nn = new NearestNeighbourAlgorithm().solve(cities);

        var bestTour = nn.getPath();
        var totalDistance = nn.getTotalDistance();
        var foundImprovment = true;

        while(foundImprovment) {
            foundImprovment = false;

            for(int i = 0; i < cities.size() - 2; ++i) {
                for(int j = i + 1; j < cities.size(); ++j) {
                    double deltaLen = calculateDelta(bestTour.get(i), bestTour.get(i+1), bestTour.get(j), bestTour.get(j+1));
                    if(deltaLen < 0) {
                        bestTour = twoOptSwap(bestTour, bestTour.get(i), bestTour.get(j));
                        totalDistance += deltaLen;
                        foundImprovment = true;
                    }
                }
            }
        }

        return new TspSolution(bestTour, totalDistance);
    }

    @Override
    public void printAlgorithm(ArrayList<ArrayList<City>> steps) {

    }
}
