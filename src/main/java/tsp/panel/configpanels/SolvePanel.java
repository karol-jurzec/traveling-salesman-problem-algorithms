package src.main.java.tsp.panel.configpanels;

import src.main.java.tsp.TspSolver;
import src.main.java.tsp.algorithms.*;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.models.TspSolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class SolvePanel extends JPanel implements ActionListener, AnalysisPanelObserver {

    JLabel jLabel = new JLabel("SOLVE PROBLEM: ");
    JComboBox jComboBox = new JComboBox(Algorithm.values());
    JButton jButton = new JButton("run algorithm");
    JTextArea jText= new JTextArea("");

    TspInstance tspInstance = null;
    TspSolver tspSolver = new TspSolver(new BruteForceAlgorithm());

    ArrayList<SolvePanelObserver> observers = new ArrayList<>();


    public SolvePanel() {

        this.setPreferredSize(new Dimension(200, 400));
        this.setBackground(Color.lightGray);
        this.setLayout(null);

        jLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        jLabel.setBounds(10, 20, 200, 15);

        jComboBox.setBounds(10, 50, 180, 30);
        jComboBox.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                changeCurrentAlgorithm(e);
            }
        });

        jText.setBounds(10, 90, 180, 40);
        jText.setBackground(Color.white);
        jText.setEditable(false);
        jText.setCaretColor(Color.white);
        jText.setLineWrap(true);
        jText.setWrapStyleWord(true);

        jButton.setBounds(30, 140, 140, 30);
        jButton.addActionListener(this);

        this.add(jComboBox, Component.CENTER_ALIGNMENT);
        this.add(jButton, Component.CENTER_ALIGNMENT);
        this.add(jText, Component.CENTER_ALIGNMENT);

        this.add(jLabel);
    }

    public void changeCurrentAlgorithm(ActionEvent e) {
        Algorithm selectedIndex = Algorithm.values()[jComboBox.getSelectedIndex()];

        switch (selectedIndex) {
            case BRUTE_FORCE:
                tspSolver = new TspSolver(new BruteForceAlgorithm());
                return;

            case ANT_COLONY:
                tspSolver = new TspSolver(new AntColonyAlgorithm(10));
                return;

            case HELD_KARP:
                tspSolver = new TspSolver(new HeldKarpAlgorithm());
                return;

            case NEAREST_NEIGHBOUR:
                tspSolver = new TspSolver(new NearestNeighbourAlgorithm());
                return;

            case THREE_OPT:
                tspSolver = new TspSolver(new ThreeOptAlgorithm());
                return;

            case TWO_OPT:
                tspSolver = new TspSolver(new TwoOptAlgorithm());
                return;

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jButton) {
            if(tspInstance != null) {
                var solution = tspSolver.solve(tspInstance);

                DecimalFormat formatter = new DecimalFormat("#.######", DecimalFormatSymbols.getInstance( Locale.ENGLISH ));
                formatter.setRoundingMode( RoundingMode.DOWN );

                jText.setText("current tour distance: " + formatter.format(solution.getTotalDistance()));

                notifyAllObservers(solution);

            } else {
                JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
            }
        }
    }

    @Override
    public void updateInstance(TspInstance tspInstance) {
        this.tspInstance = tspInstance;
    }

    public void attach(SolvePanelObserver observer) {
        observers.add(observer);
    }

    private void notifyAllObservers(TspSolution tspSolution) {
        for(var observer : observers) {
            observer.updateTspSolution(tspSolution);
        }
    }

    private enum Algorithm {
        BRUTE_FORCE("Brute force algorithm"),
        ANT_COLONY("Ant colony algorithm"),
        HELD_KARP("Bellman-Held-Karp algorithm"),
        NEAREST_NEIGHBOUR("Nearest neighbour algorithm"),
        THREE_OPT("Three-opt algorithm"),
        TWO_OPT("Two-opt algorithm");

        private String name;

        Algorithm(String name) {
            this.name = name;
        }

        @Override public String toString() { return name; }
    }
}
