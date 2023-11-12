package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;

import java.util.*;

public class BruteForceAlgorithm implements ITspAlgorithm {


    // method calculating all permutations for given array
    private <T> ArrayList<ArrayList<T>> permute(ArrayList<T> arr, ArrayList<ArrayList<T>> permutations, int k) {
        for(int i = k; i < arr.size(); ++i) {
            Collections.swap(arr, i, k);
            permute(arr, permutations, k + 1);
            Collections.swap(arr, i, k);
        }
        if(k == arr.size() - 1) {
            permutations.add(new ArrayList<T>(arr));
        }
        return permutations;
    }

    private <T> boolean isForcingDirection(ArrayList<T> arr, T firstArrayElem, T secondArryElem) {
        // is one before two: (1, 2, 3) --> ok
        // (3, 1, 2) --> ok
        // (3, 2, 1) --> not ok

        for(int i = 0; i < arr.size(); ++i) {
            if(arr.get(i) == firstArrayElem)
                return true;
            if(arr.get(i) == secondArryElem)
                return false;
        }
        return true;
    }

    private <T> ArrayList<ArrayList<T>> generateUniquePermutations(ArrayList<T> arr) {
        // steps
        // 1. generate all permutations from 1...N-1 --> (n-1)! permutations
        // 2. filter them: if the 1st city is after 2nd delete permutation --> it will remove half of them
        // 3. append last city to each of permutations --> overall we get (n-1)!/2 perms

        if(arr.size() == 0) {
            return new ArrayList<ArrayList<T>>();
        }

        if(arr.size() < 3) {
            return new ArrayList<ArrayList<T>>(List.of(arr));
        }

        T last = arr.remove(arr.size() - 1);

        var permutations = new ArrayList<ArrayList<T>>();
        permute(arr, permutations, 0);

        // check if are forcing direction O(n!)
        Iterator<ArrayList<T>> iterator = permutations.iterator();
        while (iterator.hasNext()) {
            var perm = iterator.next();
            if (!isForcingDirection(perm, arr.get(0), arr.get(1))) {
                iterator.remove();
            }
        }

        for(var perm : permutations) {
            perm.add(last);
        }


        return permutations;
    }

    private float getTotalPathDistance(ArrayList<City> path) {
        float sum = 0;
        for(int i = 0; i < path.size(); ++i) {
            var next = path.get((i+1)%path.size());
            sum = sum + path.get(i).distanceToCity(next);
        }
        return sum;
    }

    @Override
    public TspSolution solve(ArrayList<City> cities) {

        var permutations = generateUniquePermutations(new ArrayList<>(cities));

        TspSolution tspSolution = new TspSolution(null, Long.MAX_VALUE);

        for(var perm : permutations) {
            float totalDistance = getTotalPathDistance(perm);
            if(totalDistance < tspSolution.getTotalDistance() ) {
                tspSolution.setPath(perm);
                tspSolution.setTotalDistance(totalDistance);
            }
        }

            return tspSolution;
    }

    @Override
    public void printAlgorithm(ArrayList<ArrayList<City>> steps) {

    }
}
