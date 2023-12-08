package src.main.java.tsp.panel.configpanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SolvePanel extends JPanel implements ActionListener {
    String[] testComboBoxItems = {"branch and bound", "naive", "bellmand-held-karp"};

    JLabel jLabel = new JLabel("SOLVE PROBLEM: ");
    JComboBox jComboBox = new JComboBox(testComboBoxItems);
    JButton jButton = new JButton("run algorithm");

    public SolvePanel() {

        this.setPreferredSize(new Dimension(200, 400));
        this.setBackground(Color.lightGray);
        this.setLayout(null);

        jLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        jLabel.setBounds(10, 20, 200, 15);

        jComboBox.setBounds(10, 50, 180, 30);

        jButton.setBounds(30, 90, 140, 30);
        jButton.addActionListener(this);

        this.add(jComboBox, Component.CENTER_ALIGNMENT);
        this.add(jButton, Component.CENTER_ALIGNMENT);
        this.add(jLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jButton) {
            System.out.println("jButton");
        }

        if(e.getSource() == jComboBox) {
            System.out.println("combo, rip");
        }
    }

}
