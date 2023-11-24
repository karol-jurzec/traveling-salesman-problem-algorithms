package src.main.java.tsp.algorithms;


import src.main.java.tsp.TspSolver;
import src.main.java.tsp.helpers.BiHashMap;
import src.main.java.tsp.models.City;
import src.main.java.tsp.models.Road;
import src.main.java.tsp.models.TspSolution;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class AntColonyAlgorithm implements ITspAlgorithm {
    private double importanceOfPheromone = 2.0; // beta
    private double pheromoneDecayParameter = 0.1; // alfa and p
    private double initialPheromoneValue = 0.0;
    private double Q0 = 0.9; // parameter 0 <= q0 <= 1


    private BiHashMap<City, City, Double> pheromone;
    private BiHashMap<City, City, Double> distances;
    private ArrayList<Ant> antAgents;
    private ArrayList<City> globalBestTour;
    private double globalBestTourLength = Double.MAX_VALUE;


    public AntColonyAlgorithm(int numberOfAnts) {
        pheromone = new BiHashMap<>();
        distances = new BiHashMap<>();

        antAgents = new ArrayList<>();
        for(int i = 0; i < numberOfAnts; ++i) {
            antAgents.add(new Ant());
        }
    }



    private void initializePheromoneTabel(double pheromoneLevel, ArrayList<City> cities) {
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
            antAgents.set(i, new Ant());
        }

        for(int i = 0; i < antAgents.size(); ++i) {
            var startingCity = cities.get(i);
            antAgents.get(i).startingCity = startingCity;

            var remainingCities = new ArrayList<City>(cities);
            remainingCities.remove(startingCity);

            antAgents.get(i).remainingCities = remainingCities;
            antAgents.get(i).currentCity = startingCity;
            antAgents.get(i).antPath.add(startingCity);
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

    private City randomProportionalRule(Ant k, ArrayList<Double> arguments) {
        double totalSum = arguments.stream().mapToDouble(f -> f.doubleValue()).sum();

        double p = Math.random();
        double cumulativeProbability = 0.0;

        for(int i = 0; i < k.remainingCities.size(); ++i) {
            cumulativeProbability += arguments.get(i)/totalSum;
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


    private double deltaLocal() {
        return this.initialPheromoneValue;
    }

    private double deltaGlobal() { return 1/globalBestTourLength; }


    private void antCalculateNextMove(Ant k) {
        k.nextCity = stateTransitionRule(k);
        k.remainingCities.remove(k.nextCity);
        k.antPath.add(k.nextCity);
    }

    private void antGoBackToInitialCity(Ant k) {
        k.nextCity = k.startingCity;
        k.antPath.add(k.nextCity);
    }

    private void antMoveToNextCity(Ant k) {
        k.currentCity = k.nextCity;
    }

    private void performLocalUpdate(Ant k) {
        var origin = k.currentCity;
        var destination = k.nextCity;

        var newPheromoneVal = (1 - pheromoneDecayParameter) * pheromone.get(origin, destination)
                + pheromoneDecayParameter * deltaLocal();
        pheromone.put(origin, destination, newPheromoneVal);
    }

    private void calculateTourLengths() {
        for(int j = 0; j < antAgents.size(); ++j) {
            antAgents.get(j).calculateAntPathLength();
        }
    }

    private void calculateGlobalBestTour() {
        for(int j = 0; j < antAgents.size(); ++j) {
            if(antAgents.get(j).antPathTotalLength < globalBestTourLength) {
                globalBestTour = antAgents.get(j).antPath;
                globalBestTourLength = antAgents.get(j).antPathTotalLength;
            }
        }
    }



    private void initialize(ArrayList<City> cities) {

        var solverNearestNeighbour = new TspSolver(new NearestNeighbourAlgorithm());
        var solutionNNLen = solverNearestNeighbour.solve(cities).getTotalDistance();

        this.initialPheromoneValue = 1/(cities.size() * solutionNNLen);

        initializePheromoneTabel(this.initialPheromoneValue, cities);
        initializeDistance(cities);
        initializeAntAgents(cities);

    }

    private void buildAntTours(int n) {
        for(int i = 0; i < n; ++i) {
            if(i < n - 1) {
                for(int j = 0; j < antAgents.size(); ++j) {
                    antCalculateNextMove(antAgents.get(j));
                }
            } else {
                //ants go back to the initial city
                for(int j = 0;  j < antAgents.size(); ++j) {
                    antGoBackToInitialCity(antAgents.get(j));
                }
            }

            // local updates after calculations, moving ants to next city
            for(int j = 0; j < antAgents.size(); ++j) {
                performLocalUpdate(antAgents.get(j));
                antMoveToNextCity(antAgents.get(j));
            }
        }
    }

    private void performGlobalUpdate() {
        calculateTourLengths();
        calculateGlobalBestTour();

        int n = globalBestTour.size();

        for(int i = 0; i < n - 1; ++i) {
            var origin = globalBestTour.get(i);
            var destination = globalBestTour.get(i + 1);

            var pher = pheromone.get(origin, destination);
            if(pher == null ) {
                System.out.println();
            }

            var newPheromoneVal = (1 - pheromoneDecayParameter) * pheromone.get(origin, destination)
                    + pheromoneDecayParameter * deltaGlobal();

            pheromone.put(origin, destination, newPheromoneVal);
        }
    }

    public void refresh(ArrayList<City> cities) {
        initializeAntAgents(cities);
    }


    @Override
    public TspSolution solve(ArrayList<City> cities) {
        ArrayList<City> cityList = new ArrayList<>(cities);

        for(int i = 0; i < 1000; ++i) {
            initialize(cityList);
            buildAntTours(cityList.size());
            performGlobalUpdate();
            refresh(cities);
        }

        return new TspSolution(globalBestTour, globalBestTourLength);
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

        private double antPathTotalLength;

        public Ant() {
            remainingCities = new ArrayList<>();
            antPath = new ArrayList<>();
        }


        private void calculateAntPathLength() {
            double l = 0.0;
            int n = antPath.size();

            for(int i = 0; i < n - 1; ++i) {
                var next = antPath.get(i + 1);
                l = l + antPath.get(i).distanceToCity(next);
            }

            this.antPathTotalLength = l;
        }

    }




}
