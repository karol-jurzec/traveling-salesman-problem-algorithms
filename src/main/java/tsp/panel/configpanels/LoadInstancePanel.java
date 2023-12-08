package src.main.java.tsp.panel.configpanels;

import src.main.java.tsp.models.TspInstance;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class LoadInstancePanel extends JPanel implements ActionListener {
    JLabel jLabel = new JLabel("ENTER INSTANCE: ");
    JTextField jTextField = new JTextField("load instance...");
    JTextArea jText= new JTextArea("");
    JButton jButton = new JButton("search tsp instance");


    ArrayList<Observer> observers = new ArrayList<>();

    public LoadInstancePanel() {
        this.setPreferredSize(new Dimension(200, 500));
        this.setBackground(Color.lightGray);
        this.setLayout(null);

        jLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        jLabel.setBounds(10, 10, 200, 15);

        jTextField.setBounds(10, 40, 180, 30);
        jTextField.setBackground(Color.white);
        jTextField.setEditable(false);
        jTextField.setCaretColor(Color.white);

        jText.setBounds(10, 75, 180, 80);
        jText.setBackground(Color.white);
        jText.setEditable(false);
        jText.setCaretColor(Color.white);
        jText.setLineWrap(true);
        jText.setWrapStyleWord(true);

        jButton.setBounds(30, 160, 140, 30);
        jButton.addActionListener(this);

        this.add(jTextField, Component.CENTER_ALIGNMENT);
        this.add(jText, Component.CENTER_ALIGNMENT);
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
                var tspInstance = TspInstance.FileToTspInstance(file);

                this.jTextField.setText(file.getName());
                this.jText.setText(tspInstance.getInstanceDescription());
                notifyAllObservers(tspInstance);
            }
        }
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(TspInstance tspInstance) {
        for(var observer : observers) {
            observer.updateInstance(tspInstance);
        }
    }
}




