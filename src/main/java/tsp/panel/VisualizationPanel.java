package src.main.java.tsp.panel;


import src.main.java.tsp.panel.configpanels.Observer;

import javax.swing.*;
import java.awt.*;

public class VisualizationPanel extends JPanel implements Observer {

    VisualizationPanel() {
        this.setLayout(null);
        this.setBackground(Color.blue);
        this.setPreferredSize(new Dimension(200, 1000));
    }

    @Override
    public void update() {

    }
}
