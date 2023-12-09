package src.main.java.tsp.algorithms;

import src.main.java.tsp.helpers.BiHashMap;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;
import java.awt.geom.Point2D;


import java.security.KeyPair;
import java.util.*;

public class HeldKarpAlgorithm implements ITspAlgorithm {
    
    BiHashMap<Point2D, HashSet<Point2D>, Double> dp = new BiHashMap<>();
    BiHashMap<Point2D, HashSet<Point2D>, Point2D> pb = new BiHashMap<>();
    Point2D originPoint2D;


    // generowanie podzbiorw dla grafu wejsciowego, polega na rekurencyjnym przejsciu przez tablice i
    // dodaniu lub nie dodaniu miasta do zbioru. podzbiory dodawane są w liściach drzewa rekurencyjnego

    private ArrayList<HashSet<Point2D>> generateSubsets(ArrayList<Point2D> cities, ArrayList<Point2D> subset,
                                                      ArrayList<HashSet<Point2D>> subsets, int index) {
        if(index >= cities.size()) {
            subsets.add(new HashSet<>(subset));
            return subsets;
        }
        subset.add(cities.get(index));
        subsets = generateSubsets(cities, subset, subsets, index + 1);
        subset.remove(subset.size() - 1);
        subsets = generateSubsets(cities, subset, subsets, index + 1);

        return subsets;
    }

    // obliczanie rozwiazanie dynamicznym programowaniem, argument subsets powienien byc posortowany rosnaco wzgledem
    // ilosc elementow
    private double calculateSolution(ArrayList<HashSet<Point2D>> subsets, ArrayList<Point2D> cities) {
        for(var subset : subsets) {
            for (var Point2D : cities) {
                if (subset.size() < cities.size() && !subset.contains(Point2D)) {
                    dp.put(Point2D, subset, calculateMiminumDistance(Point2D, subset));
                }

                if (subset.size() == cities.size()) {
                    dp.put(originPoint2D, subset, calculateMiminumDistance(originPoint2D, subset));
                }
            }
        }

        return dp.get(originPoint2D, subsets.get(subsets.size()- 1));
    }

    private double calculateMiminumDistance(Point2D c, HashSet<Point2D> s) {
        if(s.size() == 0) {
            return c.distance(originPoint2D);
        }

        // remember Point2D which would be chosen
        Point2D endingPoint2D = null;

        double dist = Float.MAX_VALUE;
        double lastdist = dist;

        for(var Point2D : new HashSet<>(s)) {
            s.remove(Point2D);
            dist = Math.min(dist, c.distance(Point2D) + dp.get(Point2D, s));

            if( dist != lastdist ) {
                lastdist = dist;
                endingPoint2D = Point2D;
            }

            s.add(Point2D);
        }

        // add Point2D to tracking
        pb.put(c, s, endingPoint2D);

        return dist;
    }

    private ArrayList<Point2D> retrieveOptimalTour(HashSet<Point2D> subset) {

        int n = subset.size();
        ArrayList<Point2D> tour = new ArrayList<>();
        Point2D next = originPoint2D;

        while(n >= 0) {
            tour.add(next);
            next = pb.get(next, subset);
            subset.remove(next);
            n--;
        }

        tour.add(tour.get(0));

        return tour;
    }
    

    @Override
    public TspSolution solve(TspInstance tspInstance) {
        var cities = tspInstance.getPointCollection();
        if(cities.size() == 0 | cities.size() == 1)
            return new TspSolution(new ArrayList<>());

        ArrayList<Point2D> citiesWihoutOrigin = new ArrayList<>(cities);
        originPoint2D = citiesWihoutOrigin.remove(0);

        var subsets = generateSubsets(citiesWihoutOrigin, new ArrayList<>(), new ArrayList<>(), 0);

        // prepare list of subsets by sorting in ascending order by number of elemnts
        subsets.sort(new Comparator<HashSet<Point2D>>() {
            @Override
            public int compare(HashSet<Point2D> o1, HashSet<Point2D> o2) {
                return Integer.compare(o1.size(), o2.size());
            }
        });

        ArrayList<Point2D> optimalTour = retrieveOptimalTour(subsets.get(subsets.size() - 1));

        return new TspSolution(optimalTour);
    }
}
