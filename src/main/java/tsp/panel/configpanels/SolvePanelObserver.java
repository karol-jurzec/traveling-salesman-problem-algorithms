package src.main.java.tsp.panel.configpanels;

import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

public interface SolvePanelObserver {
    public void updateTspSolution(TspSolution tspSolution);
}
