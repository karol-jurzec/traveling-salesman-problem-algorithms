package src.main.java.tsp.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ConfigurationPanel extends JPanel implements ActionListener {


    JLabel jLabel = new JLabel("TSP instance");
    JButton jButton = new JButton("Load TSP instance");

    ConfigurationPanel() {
        jButton.setBounds(100, 100, 100, 50);
        jButton.addActionListener(this);



        this.add(jButton);

        this.setBackground(Color.lightGray);
        this.setPreferredSize(new Dimension(200, 200));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jButton) {
            JFileChooser fileChooser = new JFileChooser();

            int resp = fileChooser.showOpenDialog(null);

            if(resp == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }
}
