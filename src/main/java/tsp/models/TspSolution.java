package src.main.java.tsp.models;

import java.util.ArrayList;

public class TspSolution {
    private ArrayList<City> path;
    private double totalDistance;

    public TspSolution(ArrayList<City> path, double totalDistance) {
        this.path = path;
        this.totalDistance = totalDistance;
    }

    public TspSolution(ArrayList<City> path) {
        this.path = path;
        this.totalDistance = getTotalPathDistance(path);
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public ArrayList<City> getPath() {
        return path;
    }

    public void setPath(ArrayList<City> path) {
        this.path = path;
    }

    public void setTotalDistance(double distance) {
        this.totalDistance = distance;
    }

    private double getTotalPathDistance(ArrayList<City> path) {
        double sum = 0;
        for(int i = 0; i < path.size(); ++i) {
            var next = path.get((i+1)%path.size());
            sum = sum + path.get(i).distanceToCity(next);
        }
        return sum;
    }
}
