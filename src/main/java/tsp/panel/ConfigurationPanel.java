package src.main.java.tsp.panel;

import src.main.java.tsp.panel.configpanels.AnalysisPanel;
import src.main.java.tsp.panel.configpanels.LoadInstancePanel;
import src.main.java.tsp.panel.configpanels.OutputPanel;
import src.main.java.tsp.panel.configpanels.SolvePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ConfigurationPanel extends JPanel {
    LoadInstancePanel loadInstancePanel = new LoadInstancePanel();
    AnalysisPanel analysisPanel = new AnalysisPanel();
    OutputPanel outputPanel = new OutputPanel();
    SolvePanel solvePanel = new SolvePanel();

    ConfigurationPanel() {
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 2, 2, 2),  new EtchedBorder()));
        this.setLayout(new GridLayout(4, 1));
        this.setBackground(Color.lightGray);
        this.setPreferredSize(new Dimension(200, 200));

        this.add(loadInstancePanel);
        //this.add(analysisPanel);
        //this.add(outputPanel);
        this.add(solvePanel);

    }
}
