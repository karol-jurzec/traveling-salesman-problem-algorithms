package src.main.java.tsp.models;

public class Road {
    private City destination;
    private float weight;

    public Road(City destination, float weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public City getDestination() { return destination; }

    public float getWeight() { return weight; }
}
