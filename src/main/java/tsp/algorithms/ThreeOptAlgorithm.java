package src.main.java.tsp.algorithms;

import src.main.java.tsp.TspSolver;
import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;

import java.util.ArrayList;
import java.util.Collections;

public class ThreeOptAlgorithm implements ITspAlgorithm {

    public double delta = 0;

    private ArrayList<City> twoOptSwap(ArrayList<City> cities, int vOneIndex, int vTwoIndex) {
        ArrayList<City> newGraph = new ArrayList<>(cities);

        // we dont swap first origin cities
        //if(vOneIndex == 0 || vOneIndex == cities.size() - 1 || vTwoIndex == 0 || vTwoIndex == cities.size() - 1 )
        //    return newGraph;

        if(vOneIndex > vTwoIndex) {
            Collections.reverse(newGraph.subList(vTwoIndex + 1, vOneIndex));
        } else {
            Collections.reverse(newGraph.subList(vOneIndex, vTwoIndex + 1));
        }

        return newGraph;
    }

    private CASE calculateMinDeltaLen(ArrayList<City> cities, int x, int y, int z) {
        ArrayList<City> tour = new ArrayList<>(cities);

        City xCity = tour.get(x);
        City xCity2 = tour.get(x + 1);

        double xEdgeLen = xCity.distanceToCity(xCity2);

        City yCity = tour.get(y);
        City yCity2 = tour.get(y + 1);

        double yEdgeLen = yCity.distanceToCity(yCity2);

        City zCity = tour.get(z);
        City zCity2 = tour.get(z + 1);

        double zEdgeLen = zCity.distanceToCity(zCity2);

        ArrayList<Double> deltaLengths = new ArrayList<>();

        // case 1
        var deletedLen = xEdgeLen + zEdgeLen;
        var addedLen = xCity.distanceToCity(zCity) + xCity2.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 2
        deletedLen = yEdgeLen + zEdgeLen;
        addedLen = yCity.distanceToCity(zCity) + yCity2.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 3

        deletedLen = xEdgeLen + yEdgeLen;
        addedLen = xCity.distanceToCity(yCity) + xCity2.distanceToCity(yCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 4

        deletedLen = xEdgeLen + yEdgeLen + zEdgeLen;
        addedLen = xCity.distanceToCity(yCity) + xCity2.distanceToCity(zCity) + yCity2.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 6
        addedLen = xCity.distanceToCity(zCity) + yCity2.distanceToCity(xCity2) + yCity.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 5
        addedLen = xCity.distanceToCity(yCity2) + zCity.distanceToCity(yCity) + xCity2.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 7
        addedLen = xCity.distanceToCity(yCity2) + zCity.distanceToCity(xCity2) + yCity.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);



        double minDelta = Collections.min(deltaLengths);
        this.delta = minDelta;

        if(minDelta >= 0) {
            return CASE.zero;
        }

        int minIndex = deltaLengths.indexOf(minDelta);
        return CASE.values()[minIndex + 1];
    }

    public boolean calculateDiff(ArrayList<City> modified, ArrayList<City> origin) {
        double modifiedLen = new TspSolution(modified).getTotalDistance();
        double originLen = new TspSolution(origin).getTotalDistance();

        return modifiedLen < originLen;
    }

    private ArrayList<City> applyThreeOpt(ArrayList<City> cities, int x, int y, int z) {
        var tour = new ArrayList<>(cities);
        var caseNum = calculateMinDeltaLen(tour, x, y, z);

        switch (caseNum) {
            case zero:
                return tour;
            case one:
                return twoOptSwap(tour, z + 1, x);
            case two:
                return twoOptSwap(tour, y + 1, z);
            case three:
                return twoOptSwap(tour, x + 1, y);
            case four:
                tour = twoOptSwap(tour, y + 1, z);
                return twoOptSwap(tour, x + 1, y);
            case five:
                tour = twoOptSwap(tour, z + 1, x);
                tour = twoOptSwap(tour, y + 1, z);
                return calculateDiff(tour, cities) ? tour : cities;
            case six:
                tour = twoOptSwap(tour, z + 1, x);
                tour = twoOptSwap(tour, x + 1, y);
                return calculateDiff(tour, cities) ? tour : cities;
            case seven:
                tour = twoOptSwap(tour, z + 1, x);
                tour = twoOptSwap(tour, x + 1, y);
                tour = twoOptSwap(tour, y + 1, z);
                return calculateDiff(tour, cities) ? tour : cities;
            default:
                return null;
        }
    }

    private boolean checkConstraints(int i, int j, int k) {

        // k>j>i or i>k>j or j>k>i, but not
        // i>j>k nor j>k>i nor k>i>j

        if(k > j && j > i)
            return true;


        if(i > k && k > j)
            return true;


        if(j > k && k > i)
            return true;

        return false;
    }

    void threeOptTest(ArrayList<City> cities) {
        ArrayList<City> tour = new ArrayList<>(cities);

        int x = 0, y = 2, z = 4;


        //delta len

        City xCity = tour.get(x);
        City xCity2 = tour.get(x + 1);

        double xEdgeLen = xCity.distanceToCity(xCity2);

        City yCity = tour.get(y);
        City yCity2 = tour.get(y + 1);

        double yEdgeLen = yCity.distanceToCity(yCity2);

        City zCity = tour.get(z);
        City zCity2 = tour.get(z + 1);

        double zEdgeLen = zCity.distanceToCity(zCity2);

        ArrayList<Double> deltaLengths = new ArrayList<>();

        // case 1
        var deletedLen = xEdgeLen + zEdgeLen;
        var addedLen = xCity.distanceToCity(zCity) + xCity2.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 2
        deletedLen = yEdgeLen + zEdgeLen;
        addedLen = yCity.distanceToCity(zCity) + yCity2.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 3

        deletedLen = xEdgeLen + yEdgeLen;
        addedLen = xCity.distanceToCity(yCity) + xCity2.distanceToCity(yCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 4

        deletedLen = xEdgeLen + yEdgeLen + zEdgeLen;
        addedLen = xCity.distanceToCity(yCity) + xCity2.distanceToCity(zCity) + yCity2.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 6
        addedLen = xCity.distanceToCity(zCity) + yCity2.distanceToCity(xCity2) + yCity.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 5
        addedLen = xCity.distanceToCity(yCity2) + zCity.distanceToCity(yCity) + xCity2.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 7
        addedLen = xCity.distanceToCity(yCity2) + zCity.distanceToCity(xCity2) + yCity.distanceToCity(zCity2);
        deltaLengths.add(-deletedLen + addedLen);


        var originTour = new TspSolution(tour);
        var originLen = originTour.getTotalDistance();

        //case 1
        var caseOneTest = twoOptSwap(tour, z + 1, x);
        var caseOneTour = new TspSolution(caseOneTest);
        double diffOneTour = Math.abs(originLen - caseOneTour.getTotalDistance());

        //case 2
        var caseTwoTest = twoOptSwap(tour, y + 1, z);
        var caseTwoTour = new TspSolution(caseTwoTest);
        double diffTwoTour = Math.abs(originLen - caseTwoTour.getTotalDistance());

        //case 3
        var caseThreeTest = twoOptSwap(tour, x + 1, y);
        var caseThreeTour = new TspSolution(caseThreeTest);
        double diffThreeTour = Math.abs(originLen - caseThreeTour.getTotalDistance());


        //case 4
        var caseFourTest = twoOptSwap(tour, y + 1, z);
        caseFourTest = twoOptSwap(caseFourTest, x + 1, y);
        var caseFourTour = new TspSolution(caseFourTest);
        double diffFourTour = Math.abs(originLen - caseFourTour.getTotalDistance());

        //case 5
        var caseFiveTest = twoOptSwap(tour, z + 1, x);
        caseFiveTest = twoOptSwap(caseFiveTest, y + 1, z);
        var caseFiveTour = new TspSolution(caseFiveTest);
        double diffFiveTour = Math.abs(originLen - caseFiveTour.getTotalDistance());


        //case 6
        var caseSixTest = twoOptSwap(tour, z + 1, x);
        caseSixTest = twoOptSwap(caseSixTest, x + 1, y);
        var caseSixTour = new TspSolution(caseSixTest);
        double diffSixTour = Math.abs(originLen - caseSixTour.getTotalDistance());


        //case 7

        var caseSevenTest = twoOptSwap(tour, z + 1, x);
        caseSevenTest = twoOptSwap(caseSevenTest, x + 1, y);
        caseSevenTest =  twoOptSwap(caseSevenTest, y + 1, z);
        var caseSevenTour = new TspSolution(caseSevenTest);
        double diffSevenTour = Math.abs(originLen - caseSevenTour.getTotalDistance());


        return;

    }

    @Override
    public TspSolution solve(ArrayList<City> cities) {
        var tour = new NearestNeighbourAlgorithm().solve(new ArrayList<>(cities)).getPath();
        var foundImprovment = true;


        while(foundImprovment) {
            foundImprovment = false;
            for(int i = 0; i < tour.size() - 3; ++i) {
                for(int j = i + 1; j < tour.size() - 2; ++j) {
                    for(int k = j + 1; k < tour.size() - 1; ++k) {
                        if(checkConstraints(i, j, k)) {
                            var threeOpt = applyThreeOpt(tour, i, j, k);

                            var calculateTour = new TspSolution(threeOpt);
                            var secTour = new TspSolution(tour);

                            if(!threeOpt.equals(tour)) {
                                tour = threeOpt;
                                foundImprovment = true;
                            }
                        }
                    }
                }
            }
        }

        return new TspSolution(tour);
    }

    @Override
    public void printAlgorithm(ArrayList<ArrayList<City>> steps) {

    }

    enum CASE {
        zero,
        one,
        two,
        three,
        four,
        five,
        six,
        seven
    }

}