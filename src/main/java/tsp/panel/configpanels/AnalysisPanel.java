package src.main.java.tsp.panel.configpanels;

import src.main.java.tsp.models.TspInstance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Observer;

public class AnalysisPanel extends JPanel implements ActionListener, LoadPanelObserver {

    JLabel jLabel = new JLabel("PERFORM ANALYSE: ");
    JTextField jTextField = new JTextField("enter runtime limit (s)");
    JButton jButton = new JButton("run");

    TspInstance tspInstance = null;


    public AnalysisPanel() {

        this.setPreferredSize(new Dimension(200, 400));
        this.setBackground(Color.lightGray);
        this.setLayout(null);

        jLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        jLabel.setBounds(10, 20, 200, 15);

        jTextField.setBounds(10, 50, 180, 30);
        jTextField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e){
                jTextField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                jTextField.setText(jTextField.getText());
            }
        });

        jTextField.setBackground(Color.white);
        jTextField.setCaretColor(Color.white);

        jButton.setBounds(30, 90, 140, 30);
        jButton.addActionListener(this);

        this.add(jTextField, Component.CENTER_ALIGNMENT);
        this.add(jButton, Component.CENTER_ALIGNMENT);
        this.add(jLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jButton) {

        }
    }

    @Override
    public void updateInstance(TspInstance tspInstance) {
        this.tspInstance = tspInstance;
    }
}
