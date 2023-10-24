package src.main.java.tsp.graph;

import src.main.java.tsp.models.City;

import java.util.ArrayList;
import java.util.Random;

public class GraphGenerator {

    public static ArrayList<City> generateSymetricGraph(int size) {
        Random rand = new Random();
        char name = 'A';

        ArrayList<City> cities = new ArrayList<>();

        for(int i = 0; i < size; ++i) {
            var city = new City(Character.toString(name++),
                    rand.nextInt(20) + 1,
                    rand.nextInt(20) + 1);

            cities.add(city);
        }

        for(var city : cities) {
            for(var destination : cities) {
                if(city != destination) {
                    city.addRoad(destination);
                }
            }
        }

        return cities;
    }
}
