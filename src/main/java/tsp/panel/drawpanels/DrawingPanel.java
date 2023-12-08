package src.main.java.tsp.panel.drawpanels;

import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.panel.configpanels.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class DrawingPanel extends JPanel implements Observer {
    final double POINT_RADIUS = 4;
    final int PADDING = 50;

    JLabel jLabel1 = new JLabel("");
    JLabel jLabel2 = new JLabel("");

    TspInstance tspInstance = null;

    public DrawingPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);

        this.add(jLabel1, BorderLayout.PAGE_END);
        this.add(jLabel2, BorderLayout.PAGE_START);
    }

    private double calculateScale(double size, double max, double min) {
        return (size) / (max - min);
    }

    public void drawPoints(Graphics g) {
        int width = getWidth() - PADDING;
        int height = getHeight() - PADDING;

        double scaleX = calculateScale(width, tspInstance.maxX, tspInstance.minX);
        double scaleY = calculateScale(height, tspInstance.maxY, tspInstance.minY);

        jLabel1.setText("x-axis scale: " + scaleX);
        jLabel2.setText("y-axis scale: " + scaleY);

        Graphics2D graph = (Graphics2D)g;
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //set color for points
        graph.setPaint(Color.RED);

        for(var point : tspInstance.getPointCollection()) {
            plotPoint2D(point, graph, scaleX, scaleY);
        }
    }

    private void plotPoint2D(Point2D point2D, Graphics2D graph, double scaleX, double scaleY ) {
        double x = (point2D.getX() - tspInstance.minX) * scaleX + PADDING/2;
        double y = (point2D.getY() - tspInstance.minY) * scaleY + PADDING/2;
        int height = getHeight();

        graph.fill(new Ellipse2D.Double(x - POINT_RADIUS/2, height - y - POINT_RADIUS/2, POINT_RADIUS, POINT_RADIUS));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(tspInstance != null) {
            drawPoints(g);
        }
    }

    @Override
    public void updateInstance(TspInstance tspInstance) {
        this.tspInstance = tspInstance;
        repaint();
    }
}
