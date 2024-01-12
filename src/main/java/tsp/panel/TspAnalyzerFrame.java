package src.main.java.tsp.panel;

import javax.swing.*;
import java.awt.*;

public class TspAnalyzerFrame extends JFrame {

    ConfigurationPanel configurationPanel = new src.main.java.tsp.panel.ConfigurationPanel();
    VisualizationPanel visualizationPanel = new VisualizationPanel();

    public TspAnalyzerFrame() {

        this.setTitle("traveling salesman problem");
        this.setSize(1000, 1000);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(configurationPanel, BorderLayout.WEST);
        this.add(visualizationPanel, BorderLayout.CENTER);


        configurationPanel.loadInstancePanel.attach(visualizationPanel.drawingPanel);
        configurationPanel.loadInstancePanel.attach(configurationPanel.solvePanel);
        configurationPanel.loadInstancePanel.attach(configurationPanel.analysisPanel);
        configurationPanel.solvePanel.attach(visualizationPanel.drawingPanel);
        configurationPanel.analysisPanel.attach(configurationPanel.outputPanel);

        this.setVisible(true);
    }

}
