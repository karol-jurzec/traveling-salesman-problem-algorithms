package src.main.java.tsp.panel;


import src.main.java.tsp.panel.drawpanels.DrawingPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class VisualizationPanel extends JPanel {
    DrawingPanel drawingPanel = new DrawingPanel();

    VisualizationPanel() {
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 2, 2, 2),  new EtchedBorder()));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);

        this.add(drawingPanel, BorderLayout.CENTER);

    }
}
