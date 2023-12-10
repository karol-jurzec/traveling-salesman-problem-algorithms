package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class LinKernighanAlgorithm implements ITspAlgorithm {

    private ArrayList<Point2D> points;

    private ArrayList<Point2D> bestTour;
    private int size;

    private ArrayList<Point2D> tPoints = new ArrayList<>(); // contains indexes of T1 nodes

    private int t1Current = -1;


    private ArrayList<Point2D> createRandomTour() {
        ArrayList<Point2D> newRandomTour = new ArrayList<>(points);
        Collections.shuffle(newRandomTour);
        return newRandomTour;
    }

    private Point2D chooseX1(Point2D t1) {
        //t1Curr is place where t1 is placed, we want to go
        return null;
    }



    public void test(TspInstance instance) {
        this.points = instance.getPointCollection();
        this.size = points.size();

        //this.t1Ids = IntStream.range(0, size).toArray();

        //step 1 - generate random initial tour T
        this.bestTour = createRandomTour();

        while(++t1Current < size) {
            //step two - pick t1 and initialize i = 1
            int i = 0;
            Point2D t1 = bestTour.get(t1Current);
            tPoints.add(t1);
            //step three - chose x1 - means to pick t2
            Point2D t2 = chooseX1(t1);





        }


    }

    @Override
    public TspSolution solve(TspInstance tspInstance) {
        return null;
    }
}
