package src.main.java.tsp.algorithms;


import src.main.java.tsp.TspSolver;
import src.main.java.tsp.helpers.BiHashMap;
import src.main.java.tsp.models.City;
import src.main.java.tsp.models.Road;
import src.main.java.tsp.models.TspSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class AntColonyAlgorithm implements ITspAlgorithm {
    private double importanceOfPheromone = 2.0;
    private double Q0 = 0.9;


    private BiHashMap<City, City, Double> pheromone;
    private BiHashMap<City, City, Double> distances;
    private ArrayList<Ant> antAgents;
    private ArrayList<City> globalBestTour;





    private City randomProportionalRule(Ant k, ArrayList<Double> arguments) {
        double totalSum = arguments.stream().mapToDouble(f -> f.doubleValue()).sum();

        double p = Math.random();
        double cumulativeProbability = 0.0;

        for(int i = 0; i < k.remainingCities.size(); ++i) {
            cumulativeProbability += arguments.get(i)/(totalSum * totalSum);
            if(p <= cumulativeProbability)
                return k.remainingCities.get(i);
        }

        return null;
    }

    private City getMostOptimalRoad(Ant k, ArrayList<Double> arguments) {
        double maxArg = Collections.max(arguments);
        int maxArgIndex = arguments.indexOf(maxArg);

        return k.remainingCities.get(maxArgIndex);
    }

    private City stateTransitionRule(Ant k) {
        ArrayList<Double> arguments = new ArrayList<>();

        for(var city : k.remainingCities) {
            var arg = pheromone.get(k.currentCity, city) * Math.pow(distances.get(k.currentCity, city), importanceOfPheromone);
            arguments.add(arg);
        }

        double q = Math.random();

        if(q <= Q0) {
            return getMostOptimalRoad(k, arguments);
        } else {
            return randomProportionalRule(k, arguments);
        }
    }


    private void initializePheromoneLevel(double pheromoneLevel, ArrayList<City> cities) {
        for(var cityOrigin : cities) {
            for (var cityDest : cities) {
                if(cityOrigin != cityDest) {
                    pheromone.put(cityDest, cityOrigin, pheromoneLevel);
                }
            }
        }
    }

    private void initializeAntAgents(ArrayList<City> cities) {
        Collections.shuffle(cities);

        for(int i = 0; i < antAgents.size(); ++i) {
            var startingCity = cities.get(i);
            antAgents.get(i).startingCity = startingCity;

            var remainingCities = new ArrayList<City>(cities);
            remainingCities.remove(startingCity);

            antAgents.get(i).remainingCities = remainingCities;
            antAgents.get(i).currentCity = startingCity;
        }

        Collections.shuffle(cities);
    }

    private void initializeDistance(ArrayList<City> cities) {
        for( var cityOrigin : cities) {
            for(var cityDest : cities) {
                distances.put(cityOrigin, cityDest, cityOrigin.distanceToCity(cityDest));
            }
        }
    }

    private void initialize(ArrayList<City> cities) {

        var solverNearestNeighbour = new TspSolver(new NearestNeighbourAlgorithm());
        var solutionNNLen = solverNearestNeighbour.solve(cities).getTotalDistance();

        double initialPheromoneLevel = 1/(cities.size() * solutionNNLen);

        initializePheromoneLevel(initialPheromoneLevel, cities);
        initializeDistance(cities);
        initializeAntAgents(cities);

    }




    @Override
    public TspSolution solve(ArrayList<City> cities) {
        return null;
    }

    @Override
    public void printAlgorithm(ArrayList<ArrayList<City>> steps) {

    }


    class Ant {

        private City startingCity;
        private City currentCity;
        private City nextCity;


        private ArrayList<City> remainingCities;

        private ArrayList<City> antPath;



    }




}
