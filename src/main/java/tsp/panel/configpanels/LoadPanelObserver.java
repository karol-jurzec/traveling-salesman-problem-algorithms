package src.main.java.tsp.panel.configpanels;

import src.main.java.tsp.models.TspInstance;

public interface LoadPanelObserver {
    public void updateInstance(TspInstance tspInstance);
}
