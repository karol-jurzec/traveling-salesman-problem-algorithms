package src.main.java.tsp.panel.configpanels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class LoadInstancePanel extends JPanel implements ActionListener {
    JLabel jLabel = new JLabel("ENTER INSTANCE: ");
    JTextField jTextField = new JTextField("load instance...");
    JButton jButton = new JButton("load tsp instance");

    ArrayList<Observer> observers = new ArrayList<>();

    public LoadInstancePanel() {
        this.setPreferredSize(new Dimension(200, 400));
        this.setBackground(Color.lightGray);
        this.setLayout(null);

        jLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        jLabel.setBounds(10, 20, 200, 15);

        jTextField.setBounds(10, 50, 180, 30);
        jTextField.setBackground(Color.white);
        jTextField.setEditable(false);
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
            JFileChooser fileChooser = new JFileChooser();

            int resp = fileChooser.showOpenDialog(null);

            if(resp == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                this.jTextField.setText(file.getName());
            }
        }
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers() {
        for(var observer : observers) {
            observer.update();
        }
    }
}




