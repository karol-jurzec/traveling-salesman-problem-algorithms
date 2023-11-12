package src.main.java.tsp.algorithms;

import src.main.java.tsp.helpers.BiHashMap;
import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;

import java.security.KeyPair;
import java.util.*;

public class HeldKarpAlgorithm implements ITspAlgorithm {




    BiHashMap<City, HashSet<City>, Float> dp = new BiHashMap<>();
    BiHashMap<City, HashSet<City>, City> pb = new BiHashMap<>();
    City originCity;


    // generowanie podzbiorw dla grafu wejsciowego, polega na rekurencyjnym przejsciu przez tablice i
    // dodaniu lub nie dodaniu miasta do zbioru. podzbiory dodawane są w liściach drzewa rekurencyjnego

    private ArrayList<HashSet<City>> generateSubsets(ArrayList<City> cities, ArrayList<City> subset,
                                                      ArrayList<HashSet<City>> subsets, int index) {
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
    private float calculateSolution(ArrayList<HashSet<City>> subsets, ArrayList<City> cities) {
        for(var subset : subsets) {
            for (var city : cities) {
                if (subset.size() < cities.size() && !subset.contains(city)) {
                    dp.put(city, subset, calculateMiminumDistance(city, subset));
                }

                if (subset.size() == cities.size()) {
                    dp.put(originCity, subset, calculateMiminumDistance(originCity, subset));
                }
            }
        }

        return dp.get(originCity, subsets.get(subsets.size()- 1));
    }

    private float calculateMiminumDistance(City c, HashSet<City> s) {
        if(s.size() == 0) {
            return c.distanceToCity(originCity);
        }

        // remember city which would be chosen
        City endingCity = null;

        float dist = Float.MAX_VALUE;
        float lastdist = dist;

        for(var city : new HashSet<>(s)) {
            s.remove(city);
            dist = Math.min(dist, c.distanceToCity(city) + dp.get(city, s));

            if( dist != lastdist ) {
                lastdist = dist;
                endingCity = city;
            }

            s.add(city);
        }

        // add city to tracking
        pb.put(c, s, endingCity);

        return dist;
    }

    private ArrayList<City> retrieveOptimalTour(HashSet<City> subset) {

        int n = subset.size();
        ArrayList<City> tour = new ArrayList<>();
        City next = originCity;

        while(n >= 0) {
            tour.add(next);
            next = pb.get(next, subset);
            subset.remove(next);
            n--;
        }

        return tour;
    }



    @Override
    public TspSolution solve(ArrayList<City> cities) {
        if(cities.size() == 0 | cities.size() == 1)
            return new TspSolution(new ArrayList<>(), 0);

        ArrayList<City> citiesWihoutOrigin = new ArrayList<>(cities);
        originCity = citiesWihoutOrigin.remove(0);

        var subsets = generateSubsets(citiesWihoutOrigin, new ArrayList<>(), new ArrayList<>(), 0);

        // prepare list of subsets by sorting in ascending order by number of elemnts
        subsets.sort(new Comparator<HashSet<City>>() {
            @Override
            public int compare(HashSet<City> o1, HashSet<City> o2) {
                return Integer.compare(o1.size(), o2.size());
            }
        });

        float distance = calculateSolution(subsets, cities);
        ArrayList<City> optimalTour = retrieveOptimalTour(subsets.get(subsets.size() - 1));

        return new TspSolution(optimalTour, distance);
    }

    @Override
    public void printAlgorithm(ArrayList<ArrayList<City>> steps) {

    }
}
