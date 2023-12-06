package src.main.java.tsp.panel;

import javax.swing.*;
import java.awt.*;

public class TspAnalyzerFrame extends JFrame {

    ConfigurationPanel configurationPanel = new ConfigurationPanel();

    public TspAnalyzerFrame() {

        this.setTitle("Traveling salesman problem - algorithm selection");
        this.setSize(1000, 1000);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(configurationPanel, BorderLayout.WEST);



        this.setVisible(true);

    }

}
