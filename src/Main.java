package src;

import src.main.java.tsp.algorithms.*;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;
import src.main.java.tsp.panel.TspAnalyzerFrame;
import src.main.java.tsp.tspmlp.ConvexHullGraham;
import src.main.java.tsp.tspmlp.KruskalMST;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        var instance = TspInstance.FileToTspInstance(new File("/Users/karol/Desktop/uni/ajio/used_tsp_inst/portgen4992.tsp"));

        //var mst = new KruskalMST(instance.getPointCollection()).findMST();
        //var convexHUll = new ConvexHullGraham(instance.getPointCollection()).computeConvexHull();

        TspAnalyzerFrame tspAnalyzer = new TspAnalyzerFrame();
    }
}


