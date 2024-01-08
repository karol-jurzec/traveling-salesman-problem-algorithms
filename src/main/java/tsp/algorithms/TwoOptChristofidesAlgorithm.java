package src.main.java.tsp.algorithms;

import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

public class TwoOptChristofidesAlgorithm extends TwoOptAlgorithm {

    @Override
    public TspSolution solve(TspInstance tspInstance) {
        var christTour = new ChristofidesAlgorithm().solve(tspInstance).getPath();
        var points = tspInstance.getPointCollection();

        var bestTour = christTour;
        var foundImprovment = true;

        while(foundImprovment) {
            foundImprovment = false;

            for(int i = 0; i < points.size() - 2; ++i) {
                for(int j = i + 1; j < points.size(); ++j) {
                    double deltaLen = calculateDelta(bestTour.get(i), bestTour.get(i+1), bestTour.get(j), bestTour.get(j+1));
                    if(deltaLen < -0.001) {
                        bestTour = twoOptSwap(bestTour, bestTour.get(i), bestTour.get(j));
                        foundImprovment = true;
                    }
                }
            }
        }


        return new TspSolution(bestTour);
    }
}
