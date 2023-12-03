package src;

import src.main.java.tsp.TspSolver;
import src.main.java.tsp.algorithms.*;
import src.main.java.tsp.graph.GraphGenerator;
import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspSolution;
import src.main.java.tsp.ploter.PlotPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {

    public static final int RANGE = 900;

    public static int counter = 0;
    public static ArrayList<ArrayList<Integer>> notUniquePermutations = new ArrayList<>();

    public static ArrayList<ArrayList<Integer>> permutations = new ArrayList<>();

    public static boolean comparePermutations(ArrayList<Integer> perm1, ArrayList<Integer> perm2) {
        int n = perm1.size();
        for(int i = 0; i < n; ++i) {
            boolean isRepForward = true, isRepBack = true;
            for(int j = i, k = 0; k < perm1.size(); ++k, j = (j + 1)%n) {
                if(perm1.get(j) != perm2.get(k)) {
                    isRepForward = false;
                }

                int z = (((-j % n) + n) % n); //backwards perm
                if(perm1.get(z) != perm2.get(k)) {
                    isRepBack = false;
                }

                if(!isRepForward && !isRepBack)
                    break;
            }
            if(isRepForward || isRepBack) {
                notUniquePermutations.add(perm1);
                notUniquePermutations.add(perm2);
                return false;
            }
        }
        return true;
    }

    public static boolean comparePermutations() {
        for(int i = 0; i < permutations.size(); ++i) {
            for(int j = i + 1; j < permutations.size(); ++j) {
                if(!comparePermutations(permutations.get(i), permutations.get(j)))
                    return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        // var solverBruteForce = new TspSolver(new BruteForceAlgorithm());
        // var solutionBruteForce = solverBruteForce.solve(cities);




        var cities = GraphGenerator.generateSymetricGraph(105);

        //var solverNearestNeighbour = new TspSolver(new NearestNeighbourAlgorithm());
        //var solutionNN = solverNearestNeighbour.solve(cities);

       var solverThreeOptAlgorithm = new TspSolver(new ThreeOptAlgorithm());
       var solutionThreeOptAlgorithm = solverThreeOptAlgorithm.solve(cities);

        var solverTwoOptAlgorithm = new TspSolver(new TwoOptAlgorithm());
        var solutionTwoOptAlgorithm = solverTwoOptAlgorithm.solve(cities);

        var solverAntColony = new TspSolver(new AntColonyAlgorithm(10));
        var solutionAntColony = solverAntColony.solve(cities);

        //var solverHeldKarpAlgorithm = new TspSolver(new HeldKarpAlgorithm());
        //var solutionHeldKarpAlgorithm = solverHeldKarpAlgorithm.solve(cities);

        //var solverBruteForce = new TspSolver(new BruteForceAlgorithm());
        //var solutionBruteForce = solverBruteForce.solve(cities);


        var panel = new PlotPanel(cities);
        panel.setPath(solutionThreeOptAlgorithm.getPath());

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(RANGE, RANGE);
        frame.add(panel);
        frame.setVisible(true);
    }
}

