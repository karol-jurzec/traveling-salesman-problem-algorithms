package src.main.java.tsp.algorithms;

import src.main.java.tsp.TspSolver;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

public class ThreeOptAlgorithm implements ITspAlgorithm {

    public double delta = 0;

    private ArrayList<Point2D> twoOptSwap(ArrayList<Point2D> cities, int vOneIndex, int vTwoIndex) {
        ArrayList<Point2D> newGraph = new ArrayList<>(cities);

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

    private double distance(Point2D p1, Point2D p2) {
        return Math.sqrt(
                Math.pow(p2.getX() - p1.getX(), 2) +
                        Math.pow(p2.getY() - p1.getY(), 2)
        );
    }

    private CASE calculateMinDeltaLen(ArrayList<Point2D> cities, int x, int y, int z) {
        ArrayList<Point2D> tour = new ArrayList<>(cities);

        Point2D xPoint2D = tour.get(x);
        Point2D xPoint2D2 = tour.get(x + 1);

        double xEdgeLen = xPoint2D.distance(xPoint2D2);

        Point2D yPoint2D = tour.get(y);
        Point2D yPoint2D2 = tour.get(y + 1);

        double yEdgeLen = yPoint2D.distance(yPoint2D2);

        Point2D zPoint2D = tour.get(z);
        Point2D zPoint2D2 = tour.get(z + 1);

        double zEdgeLen = zPoint2D.distance(zPoint2D2);

        ArrayList<Double> deltaLengths = new ArrayList<>();

        // case 1
        var deletedLen = xEdgeLen + zEdgeLen;
        var addedLen = xPoint2D.distance(zPoint2D) + xPoint2D2.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 2
        deletedLen = yEdgeLen + zEdgeLen;
        addedLen = yPoint2D.distance(zPoint2D) + yPoint2D2.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 3

        deletedLen = xEdgeLen + yEdgeLen;
        addedLen = xPoint2D.distance(yPoint2D) + xPoint2D2.distance(yPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 4

        deletedLen = xEdgeLen + yEdgeLen + zEdgeLen;
        addedLen = xPoint2D.distance(yPoint2D) + xPoint2D2.distance(zPoint2D) + yPoint2D2.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 6
        addedLen = xPoint2D.distance(zPoint2D) + yPoint2D2.distance(xPoint2D2) + yPoint2D.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 5
        addedLen = xPoint2D.distance(yPoint2D2) + zPoint2D.distance(yPoint2D) + xPoint2D2.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 7
        addedLen = xPoint2D.distance(yPoint2D2) + zPoint2D.distance(xPoint2D2) + yPoint2D.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);



        double minDelta = Collections.min(deltaLengths);
        this.delta = minDelta;

        if(minDelta >= 0) {
            return CASE.zero;
        }

        int minIndex = deltaLengths.indexOf(minDelta);
        return CASE.values()[minIndex + 1];
    }

    public boolean calculateDiff(ArrayList<Point2D> modified, ArrayList<Point2D> origin) {
        double modifiedLen = TspSolution.getTotalPathDistanceForPoints(modified);
        double originLen = TspSolution.getTotalPathDistanceForPoints(origin);

        return modifiedLen < originLen;
    }

    private ArrayList<Point2D> applyThreeOpt(ArrayList<Point2D> cities, int x, int y, int z) {
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

    void threeOptTest(ArrayList<Point2D> cities) {
        ArrayList<Point2D> tour = new ArrayList<>(cities);

        int x = 0, y = 2, z = 4;


        //delta len

        Point2D xPoint2D = tour.get(x);
        Point2D xPoint2D2 = tour.get(x + 1);

        double xEdgeLen = xPoint2D.distance(xPoint2D2);

        Point2D yPoint2D = tour.get(y);
        Point2D yPoint2D2 = tour.get(y + 1);

        double yEdgeLen = yPoint2D.distance(yPoint2D2);

        Point2D zPoint2D = tour.get(z);
        Point2D zPoint2D2 = tour.get(z + 1);

        double zEdgeLen = zPoint2D.distance(zPoint2D2);

        ArrayList<Double> deltaLengths = new ArrayList<>();

        // case 1
        var deletedLen = xEdgeLen + zEdgeLen;
        var addedLen = xPoint2D.distance(zPoint2D) + xPoint2D2.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 2
        deletedLen = yEdgeLen + zEdgeLen;
        addedLen = yPoint2D.distance(zPoint2D) + yPoint2D2.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 3

        deletedLen = xEdgeLen + yEdgeLen;
        addedLen = xPoint2D.distance(yPoint2D) + xPoint2D2.distance(yPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 4

        deletedLen = xEdgeLen + yEdgeLen + zEdgeLen;
        addedLen = xPoint2D.distance(yPoint2D) + xPoint2D2.distance(zPoint2D) + yPoint2D2.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 6
        addedLen = xPoint2D.distance(zPoint2D) + yPoint2D2.distance(xPoint2D2) + yPoint2D.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 5
        addedLen = xPoint2D.distance(yPoint2D2) + zPoint2D.distance(yPoint2D) + xPoint2D2.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);

        // case 7
        addedLen = xPoint2D.distance(yPoint2D2) + zPoint2D.distance(xPoint2D2) + yPoint2D.distance(zPoint2D2);
        deltaLengths.add(-deletedLen + addedLen);


        var originLen = TspSolution.getTotalPathDistanceForPoints(tour);

        //case 1
        var caseOneTest = twoOptSwap(tour, z + 1, x);
        var caseOneTourLen = TspSolution.getTotalPathDistanceForPoints(caseOneTest);
        double diffOneTour = Math.abs(originLen - caseOneTourLen);

        //case 2
        var caseTwoTest = twoOptSwap(tour, y + 1, z);
        var caseTwoTourLen = TspSolution.getTotalPathDistanceForPoints(caseTwoTest);
        double diffTwoTour = Math.abs(originLen - caseTwoTourLen);

        //case 3
        var caseThreeTest = twoOptSwap(tour, x + 1, y);
        var caseThreeTourLen = TspSolution.getTotalPathDistanceForPoints(caseThreeTest);
        double diffThreeTour = Math.abs(originLen - caseThreeTourLen);


        //case 4
        var caseFourTest = twoOptSwap(tour, y + 1, z);
        caseFourTest = twoOptSwap(caseFourTest, x + 1, y);
        var caseFourTourLen = TspSolution.getTotalPathDistanceForPoints(caseFourTest);
        double diffFourTour = Math.abs(originLen - caseFourTourLen);

        //case 5
        var caseFiveTest = twoOptSwap(tour, z + 1, x);
        caseFiveTest = twoOptSwap(caseFiveTest, y + 1, z);
        var caseFiveTourLen = TspSolution.getTotalPathDistanceForPoints(caseFiveTest);
        double diffFiveTour = Math.abs(originLen - caseFiveTourLen);


        //case 6
        var caseSixTest = twoOptSwap(tour, z + 1, x);
        caseSixTest = twoOptSwap(caseSixTest, x + 1, y);
        var caseSixTourLen = TspSolution.getTotalPathDistanceForPoints(caseSixTest);
        double diffSixTour = Math.abs(originLen - caseSixTourLen);


        //case 7

        var caseSevenTest = twoOptSwap(tour, z + 1, x);
        caseSevenTest = twoOptSwap(caseSevenTest, x + 1, y);
        caseSevenTest =  twoOptSwap(caseSevenTest, y + 1, z);
        var caseSevenTourLen = TspSolution.getTotalPathDistanceForPoints(caseSevenTest);
        double diffSevenTour = Math.abs(originLen - caseSevenTourLen);


        return;

    }

    @Override
    public TspSolution solve(TspInstance tspInstance) {
        var tour = new NearestNeighbourAlgorithm().solve(tspInstance).getPath();


        var foundImprovment = true;


        while(foundImprovment) {
            foundImprovment = false;
            for(int i = 0; i < tour.size() - 3; ++i) {
                for(int j = i + 1; j < tour.size() - 2; ++j) {
                    for(int k = j + 1; k < tour.size() - 1; ++k) {
                        if(checkConstraints(i, j, k)) {
                            var threeOpt = applyThreeOpt(tour, i, j, k);

                            //var calculateTour = new TspSolution(threeOpt);
                            //var secTour = new TspSolution(tour);

                            if(!threeOpt.equals(tour)) {
                                tour = threeOpt;
                                foundImprovment = true;
                            }
                        }
                    }
                }
            }
        }

        //return new TspSolution(tour);
        return new TspSolution(tour);
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
