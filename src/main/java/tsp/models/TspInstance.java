package src.main.java.tsp.models;

import java.awt.*;
import java.io.*;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.stream.Stream;

public class TspInstance {
    String instanceDescription = "";
    ArrayList<Point2D> pointCollection = new ArrayList<>();

    public double minX = java.lang.Double.MAX_VALUE;
    public double minY = java.lang.Double.MAX_VALUE;
    public double maxX = java.lang.Double.MIN_VALUE;
    public double maxY = java.lang.Double.MIN_VALUE;


    public TspInstance(ArrayList<Point2D> pointCollection, String desc) {
        this.instanceDescription = desc;
        this.pointCollection = pointCollection;
        calculatePointsRange(pointCollection);
    }

    private void calculatePointsRange(ArrayList<Point2D> points) {
        for(int i = 0; i < points.size(); ++i ) {
            double x = points.get(i).getX();
            double y = points.get(i).getY();

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
    }

    private static String gatherInstanceDesc(File file) throws IOException {
        String desc = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("COMMENT")) {
                    var arr = Stream.of(line.split(":")).filter(w -> !w.isEmpty()).toArray(String[]::new);
                    if(arr.length > 1) {
                        desc = desc + arr[1].stripLeading() + ";\n";
                    }
                }
            }
        }

        return desc;
    }


    private static ArrayList<Point2D> gatherPointsList(File file) throws IOException {
        ArrayList<Point2D> points = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("NODE_COORD_SECTION")) break;
            }

            while ((line = br.readLine()) != null) {
                var arr = Stream.of(line.split(" ")).filter(w -> !w.isEmpty()).toArray(String[]::new);
                var point2D = new Point2D.Double();

                if(arr.length > 2) {
                    double x = java.lang.Double.parseDouble(arr[1]);
                    double y = java.lang.Double.parseDouble(arr[2]);

                    point2D.x = x;
                    point2D.y = y;

                    points.add(point2D);
                }
            }
        }
        return points;
    }


    public static TspInstance FileToTspInstance(File file) {
        String desc = null;
        ArrayList<Point2D> points = null;

        try {
            desc = gatherInstanceDesc(file);
            points= gatherPointsList(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new TspInstance(points, desc);
    }

    public ArrayList<Point2D> getPointCollection() {
        return this.pointCollection;
    }

    public String getInstanceDescription() { return this.instanceDescription; }
}
