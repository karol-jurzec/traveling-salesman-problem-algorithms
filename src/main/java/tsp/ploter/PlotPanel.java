package src.main.java.tsp.ploter;

import src.main.java.tsp.models.City;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class PlotPanel extends JPanel {
    ArrayList<City> cities;
    ArrayList<City> path;

    int marg = 60;

    public PlotPanel(ArrayList<City> cities) {
        this.cities = cities;
    }

    private void drawPath(Graphics g) {
        Graphics2D graph = (Graphics2D)g;

        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.setPaint(Color.BLACK);

        int h = getHeight();

        for(int i = 0; i < path.size(); ++i) {
            Point origin = path.get(i).getCoordinates();
            Point destination = path.get((i + 1)%path.size()).getCoordinates();

            graph.draw(new Line2D.Double(origin.x, h - origin.y, destination.x, h-destination.y));
        }
    }

    private int getMax() {
        int max = Integer.MIN_VALUE;
        for(var city : cities) {
            max = Math.max(max, city.getCoordinates().y);
        }

        return max;
    }

    private void drawGraph(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        //double x = (double)(width-2*marg)/(cities.size()-1);
        //double scale = (double)(height-2*marg)/getMax();


        Graphics2D graph = (Graphics2D)g;
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        //set color for points
        graph.setPaint(Color.RED);

        for(var city : cities) {

            int x = city.getCoordinates().x;
            int y = city.getCoordinates().y;

            graph.fill(new Ellipse2D.Double(x - 4, height - y - 4, 8, 8));
        }
    }

    private void doDrawing(Graphics g) {
        drawGraph(g);
        drawPath(g);
    }

    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPath(g);
        doDrawing(g);
    }

    public void setPath(ArrayList<City> path) {
        this.path = path;
    }
}



