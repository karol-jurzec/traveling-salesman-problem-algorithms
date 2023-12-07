package src.main.java.tsp.panel;


import src.main.java.tsp.models.City;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.panel.configpanels.Observer;
import src.main.java.tsp.panel.drawpanels.DrawingPanel;
import src.main.java.tsp.ploter.PlotPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class VisualizationPanel extends JPanel {
    DrawingPanel drawingPanel = new DrawingPanel();

    VisualizationPanel() {
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 2, 2, 2),  new EtchedBorder()));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);

        this.add(drawingPanel, BorderLayout.CENTER);

    }
}
