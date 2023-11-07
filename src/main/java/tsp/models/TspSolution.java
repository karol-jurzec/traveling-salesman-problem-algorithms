package src.main.java.tsp.models;

import java.util.ArrayList;

public class TspSolution {
    private ArrayList<City> path;
    private float totalDistance;

    public TspSolution(ArrayList<City> path, float totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    public ArrayList<City> getPath() {
        return path;
    }

    public void setPath(ArrayList<City> path) {
        this.path = path;
    }

    public void setTotalDistance(float distance) {
        this.totalDistance = distance;
    }

}
