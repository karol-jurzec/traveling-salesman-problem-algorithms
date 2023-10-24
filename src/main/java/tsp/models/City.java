package src.main.java.tsp.models;

import java.awt.*;
import java.util.ArrayList;

public class City {
    private String name;
    private Point coordinates;
    private ArrayList<Road> roads;

    public City(String name, int x, int y) {
        this.name = name;
        this.coordinates = new Point(x, y);
        roads = new ArrayList<>();
    }

    private float calculateEuclideanDistance(City origin, City destination) {
        return (float)origin.coordinates.distance(destination.coordinates.x, destination.coordinates.y);
    }

    public void addRoad(City city) {
        float weight = calculateEuclideanDistance(this, city);
        Road road = new Road(city, weight);
        roads.add(road);
    }

    public void addRoad(City city, float weight) {
        Road road = new Road(city, weight);
        roads.add(road);
    }

    public float distanceToCity(City city) {
        for(var road : roads) {
            if(road.getDestination() == city) {
                return road.getWeight();
            }
        }

        return 0;
    }

}
