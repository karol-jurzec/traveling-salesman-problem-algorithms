package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;
import src.main.java.tsp.panel.AlgorithmVisualizer;
import src.main.java.tsp.panel.drawpanels.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class NearestNeighbourAlgorithm implements ITspAlgorithm, AlgorithmVisualizer {
    public NearestNeighbourAlgorithm() {}

    public NearestNeighbourAlgorithm(DrawingPanel dp) {
        this.dp = dp;
        this.dp.setVisualizer(this);
    }

    private DrawingPanel dp = null;
    private ArrayList<Line2D> steps = new ArrayList<Line2D>();


    private ArrayList<Point2D> NNAlgorithm(TspInstance tspInstance) {
        var points = tspInstance.getPointCollection();
        ArrayList<Integer> ids = tspInstance.getIdCollection();
        //double[][] distMatrix = tspInstance.getDistanceMatrix();

        ArrayList<Integer> nnIdPath = new ArrayList<>(Collections.singletonList(ids.get(0)));
        HashMap<Integer, Boolean> visited = new HashMap<>( );

        for(var p : ids) {
            visited.put(p, false);
        }

        while(nnIdPath.size() < points.size()) {
            int curr = nnIdPath.get(nnIdPath.size() - 1);
            int nearest = -1;
            double weight = Float.MAX_VALUE;

            for(int i = 0; i < points.size(); ++i) {
                Point2D p1 = points.get(curr);
                Point2D p2 = points.get(i);
                if(i != curr && !visited.get(i) && p1.distance(p2) < weight) {
                    weight = p1.distance(p2);
                    nearest = i;
                }
            }

            visited.put(curr, true);
            nnIdPath.add(nearest);

        }

        nnIdPath.add(nnIdPath.get(0));


        var nnPath = new ArrayList<Point2D>();
        nnIdPath.forEach(x -> nnPath.add(points.get(x)));

        return nnPath;
    }

    public void addStep(Point2D p1, Point2D p2) {
        steps.add(new Line2D.Double(p1, p2));
    }

    @Override
    public TspSolution solve(TspInstance tspInstance) {
        ArrayList<Point2D> ans = NNAlgorithm(tspInstance);
        return new TspSolution(ans);
    }

    @Override
    public String toString() {
        return "0";
    }

    @Override
    public void draw(Graphics g)  {
        for (Line2D line : steps) {
            dp.plotLine2D(line.getP1(), line.getP2(), g);
        }




    }


}
