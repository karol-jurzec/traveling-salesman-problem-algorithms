/*
package src.main.java.tsp.algorithms;

import org.nd4j.linalg.api.ops.impl.shape.ExpandDims;
import src.main.java.tsp.models.Point;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import java.util.*;


import java.awt.geom.Point2D;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LinKernighanAlgorithm implements ITspAlgorithm {

    private ArrayList<Point2D> points;
    private ArrayList<Integer> tour;
    Random rand;



    private int N;
    private double[][] dist;
    private int[] tPoints;



    private int t1Current = -1;


    private ArrayList<Integer> createRandomTour() {
        var range = IntStream.range(0, N).toArray();
        var newRandomTour = Arrays.stream(range)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(newRandomTour);
        return new ArrayList<>(newRandomTour);
    }

    private void calculateTourEdges() {
        for(int i = 0; i < N; ++i) {
            var p1 = tour.get(i);
            var p2 = tour.get( (i + 1) % N );

            tourEdges.add(new Edge(p1, p2));
            tourEdges.add(new Edge(p2, p1));

        }
    }

    private int findT0() {
        int n = altT0.size() - 1;
        var r = rand.nextInt((n - 0) + 1) + 0;

        var t0 = altT0.remove(r);
        return t0;
    }
    private int findT1(int t0) {
        int t0Index = tour.indexOf(t0);

        int r = tour.get( (t0Index + 1) % N );
        int l = tour.get( (t0Index - 1 + N ) % N );

        double gainOne = dist[t0][r];
        double gainTwo = dist[t0][l];

        if(gainOne > gainTwo) {
            altX1 = l;
            xSet.add(new Edge(t0, r));
            return r;
        }

        altX1 = r;
        xSet.add(new Edge(t0, l));
        return l;
    }
    private int findT2(int t0, int t1) {
        for(int i = 0; i < N; ++i) {
            if(i != t1 && !tourEdges.contains(new Edge(t1, i))) {
                double c = dist[t0][t1] - dist[t1][i];
                if(c > 0) {
                    altY1.add(i);
                }
            }
        }

        if(altY1.size() == 0)
            return -1;

        int t2 = altY1.remove(altY1.size());
        ySet.add(new Edge(t1, t2));

        G = G + dist[t0][t1] - dist[t1][t2];
        return t2;
    }

    private int findT2i(int tLast, int i) {
        int tLastIndex = tour.indexOf(tLast);

        int r = tour.get( (tLastIndex + 1) % N );
        int l = tour.get( (tLastIndex - 1 + N ) % N );

        double gainOne = dist[tLast][r];
        double gainTwo = dist[tLast][l];

        var e1 = new Edge(tLast, r);
        var e2 = new Edge(tLast, l);

        if(ySet.contains(e1) && ySet.contains(e2)) {
            return -1;
        }

        if( i == 1 ) {
            if(gainOne > gainTwo) {
                if(!ySet.contains(e2)) {
                    altX2 = l;
                }
            } else {
                if(!ySet.contains(e1)) {
                    altX2 = r;
                }
            }
        }

        return gainOne > gainTwo ? r : l;
    }

    private boolean isJoinedToT0(int t2i) {
        int t2iIndex = tour.indexOf(t2i);
        int t0 = tPoints[0];

        int r = tour.get( (t2iIndex + 1) % N );

        if(r == t0) {
            return true;
        }

        int l = tour.get( (t2iIndex - 1 + N) % N );

        if(l == t0) {
            return true;
        }

        return false;
    }

    //returns true if tour was updated
    private boolean updateTourChoosingXi(int i) {
        int tLast = tPoints[2 * i];

        int t2i = findT2i(tLast, i);
        tPoints[2 * i + 1] = t2i;

        xSet.add(new Edge(tLast, t2i));

        if(!isJoinedToT0(t2i)) {

            double gain = dist[tLast][t2i] - dist[t2i][tPoints[0]];
            G += gain;

            ySet.add(new Edge(t2i, tPoints[0]));

            if(G > 0) {
                updateTour();
                return true;
            }
        }

        return false;
    }

    private void updateTour() {



    }

    LinkedHashSet tourEdges = new LinkedHashSet<Edge>();
    ArrayList<Integer> altT0 = null;
    ArrayList<Integer> altY1 = new ArrayList<>();

    int altX1, altX2;

    LinkedHashSet xSet = new LinkedHashSet<Edge>();
    LinkedHashSet ySet = new LinkedHashSet<Edge>();


    double G = 0;

    @Override
    public TspSolution solve(TspInstance tspInstance) {
        this.N = tspInstance.getSize();
        this.points = tspInstance.getPointCollection();
        this.tPoints = new int[N * N];
        this.dist = tspInstance.getDistanceMatrix();


        this.tour = createRandomTour();
        altT0 = new ArrayList<>(tour);
        calculateTourEdges();

        int i = 0;
        while( true ) {

            if(altT0.size() > 0) {


                tPoints[0] = findT0();
                tPoints[1] = findT1(tPoints[0]);
                //t3 -- if -1 then such edge doesnt exist
                int t3 = findT2(tPoints[0], tPoints[1]);
                if(t3 == -1 ) { xSet = new LinkedHashSet<>(); continue; }
                tPoints[2] = t3;

                i = i + 1;
                while( true ) {
                   // var xi = chooseXi(i);
                }


            } else {
                break;
            }
        }

        return null;
    }

    class Edge {
        public final Integer source;
        public final Integer destination;

        public Edge(Integer source, Integer destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return Objects.equals(source, edge.source) &&
                    Objects.equals(destination, edge.destination);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, destination);
        }

    }
}
*/
