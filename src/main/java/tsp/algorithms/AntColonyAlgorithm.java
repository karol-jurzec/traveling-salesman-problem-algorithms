package src.main.java.tsp.algorithms;


import src.main.java.tsp.TspSolver;
import src.main.java.tsp.helpers.BiHashMap;
import src.main.java.tsp.models.Road;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.awt.geom.Point2D;
import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class AntColonyAlgorithm implements ITspAlgorithm {
    private final double importanceOfPheromone = 2.0; // beta
    private final double pheromoneDecayParameter = 0.1; // alfa and p
    private double initialPheromoneValue = 0.0;
    private final double Q0 = 0.9; // parameter 0 <= q0 <= 1
    private final int numberOfAntAgents2 = 10; //


    private final BiHashMap<Point2D, Point2D, Double> pheromone;
    private final BiHashMap<Point2D, Point2D, Double> distances;
    private ArrayList<Ant> antAgents;
    private ArrayList<Point2D> globalBestTour;
    private double globalBestTourLength = Double.MAX_VALUE;



    public AntColonyAlgorithm(int numberOfAnts) {
        pheromone = new BiHashMap<>();
        distances = new BiHashMap<>();

        antAgents = new ArrayList<>();
        for(int i = 0; i < numberOfAnts; ++i) {
            antAgents.add(new Ant());
        }
    }

    private void initializePheromoneTabel(ArrayList<Point2D> cities) {
        for(var Point2DOrigin : cities) {
            for (var Point2DDest : cities) {
                if(Point2DOrigin != Point2DDest) {
                    pheromone.put(Point2DDest, Point2DOrigin, this.initialPheromoneValue);
                }
            }
        }
    }

    private void initializeAntAgents(ArrayList<Point2D> citiesInput) {
        var cities = new ArrayList<>(citiesInput);
        Collections.shuffle(cities);

        for(int i = 0; i < antAgents.size(); ++i) {
            antAgents.set(i, new Ant());
        }

        for(int i = 0; i < antAgents.size(); ++i) {
            var startingPoint2D = cities.get(i);
            antAgents.get(i).startingPoint2D = startingPoint2D;

            var remainingCities = new ArrayList<Point2D>(cities);
            remainingCities.remove(startingPoint2D);

            antAgents.get(i).remainingCities = remainingCities;
            antAgents.get(i).currentPoint2D = startingPoint2D;
            antAgents.get(i).antPath.add(startingPoint2D);
        }
    }

    private void initializeDistance(ArrayList<Point2D> cities) {
        for( var Point2DOrigin : cities) {
            for(var Point2DDest : cities) {
                distances.put(Point2DOrigin, Point2DDest, Point2DOrigin.distance(Point2DDest));
            }
        }
    }

    private Point2D randomProportionalRule(Ant k, ArrayList<Double> arguments) {
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

    private Point2D getMostOptimalRoad(Ant k, ArrayList<Double> arguments) {
        double maxArg = Collections.max(arguments);
        int maxArgIndex = arguments.indexOf(maxArg);

        return k.remainingCities.get(maxArgIndex);
    }

    private Point2D stateTransitionRule(Ant k) {
        ArrayList<Double> arguments = new ArrayList<>();

        for(var Point2D : k.remainingCities) {
            var arg = pheromone.get(k.currentPoint2D, Point2D) * Math.pow(1/distances.get(k.currentPoint2D, Point2D), importanceOfPheromone);
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
        k.nextPoint2D = stateTransitionRule(k);
        k.remainingCities.remove(k.nextPoint2D);
        k.antPath.add(k.nextPoint2D);
    }

    private void antGoBackToInitialPoint2D(Ant k) {
        k.nextPoint2D = k.startingPoint2D;
        k.antPath.add(k.nextPoint2D);
    }

    private void antMoveToNextPoint2D(Ant k) {
        k.currentPoint2D = k.nextPoint2D;
    }

    private void performLocalUpdate(Ant k) {
        var origin = k.currentPoint2D;
        var destination = k.nextPoint2D;

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

    private void initialize(TspInstance tspInstance) {
        var cities = tspInstance.getPointCollection();
        var solutionNN = new NearestNeighbourAlgorithm().solve(tspInstance).getPath();
        var solutionNNLen = TspSolution.getTotalPathDistanceForPoints(solutionNN);

        this.initialPheromoneValue = Math.pow(cities.size() * solutionNNLen, -1);

        initializePheromoneTabel(cities);
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
                //ants go back to the initial Point2D
                for(int j = 0;  j < antAgents.size(); ++j) {
                    antGoBackToInitialPoint2D(antAgents.get(j));
                }
            }

            // local updates after calculations, moving ants to next Point2D
            for(int j = 0; j < antAgents.size(); ++j) {
                performLocalUpdate(antAgents.get(j));
                antMoveToNextPoint2D(antAgents.get(j));
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

            var newPheromoneVal = (1 - pheromoneDecayParameter) * pheromone.get(origin, destination)
                    + pheromoneDecayParameter * deltaGlobal();

            pheromone.put(origin, destination, newPheromoneVal);
        }
    }

    public void refresh(ArrayList<Point2D> cities) {
        initializeAntAgents(cities);
    }

    public TspSolution solve(TspInstance tspInstance) {
        if(tspInstance.getSize() < antAgents.size()) {
            antAgents = new ArrayList<>();
            for(int i = 0; i < tspInstance.getSize(); ++i) {
                antAgents.add(new Ant());
            }
        }

        var cities = tspInstance.getPointCollection();
        ArrayList<Point2D> Point2DList = new ArrayList<>(cities);

        initialize(tspInstance);

        for(int i = 0; i < 500; ++i) {
            buildAntTours(Point2DList.size());
            performGlobalUpdate();
            refresh(cities);
        }

        return new TspSolution(globalBestTour);
    }

    class Ant {

        private Point2D startingPoint2D;
        private Point2D currentPoint2D;
        private Point2D nextPoint2D;

        private ArrayList<Point2D> remainingCities;

        private final ArrayList<Point2D> antPath;

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
                l = l + antPath.get(i).distance(next);
            }

            this.antPathTotalLength = l;
        }
    }

    @Override
    public String toString() {
        return "Ant Colony Algorithm";
    }
}
