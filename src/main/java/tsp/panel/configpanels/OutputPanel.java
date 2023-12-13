package src.main.java.tsp.panel.configpanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class OutputPanel extends JPanel implements AnalysisPanelObserver {
    JLabel jLabel = new JLabel("OUTPUT: ");
    JTextField jTextField = new JTextField("proposed algorithm");

    public OutputPanel() {

        this.setPreferredSize(new Dimension(200, 400));
        this.setBackground(Color.lightGray);
        this.setLayout(null);

        jLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        jLabel.setBounds(10, 20, 200, 15);

        jTextField.setBounds(10, 50, 180, 30);
        jTextField.setFont(new Font("SansSerif", Font.ITALIC, 14));
        jTextField.setEditable(false);
        jTextField.setBackground(Color.white);
        jTextField.setCaretColor(Color.white);

        this.add(jTextField, Component.CENTER_ALIGNMENT);
        this.add(jLabel);
    }

    @Override
    public void updateOutput(String predictedAlgorithm) {
        this.jTextField.setText(predictedAlgorithm);
    }
}
