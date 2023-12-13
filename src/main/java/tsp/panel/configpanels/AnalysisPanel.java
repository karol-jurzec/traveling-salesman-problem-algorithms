package src.main.java.tsp.panel.configpanels;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.tspmlp.TspMlpFeatures;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;

public class AnalysisPanel extends JPanel implements ActionListener, LoadPanelObserver {

    JLabel jLabel = new JLabel("PERFORM ANALYSE: ");
    JTextField jTextField = new JTextField("enter runtime limit (s)");
    JButton jButton = new JButton("run");

    TspInstance tspInstance = null;
    MultiLayerNetwork model = null;

    ArrayList<AnalysisPanelObserver> observers = new ArrayList<>();

    public AnalysisPanel() {
        readModel();

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

    private void readModel() {
        try {
            this.model = ModelSerializer.restoreMultiLayerNetwork("/Users/karol/Desktop/uni/ajio/data_set/tsp_model.zip");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private String classNumToAlgorithmName(int classNum) {
        switch (classNum) {
            case 0:
                return "Nearest neighbour algorithm";
            case 1:
                return "Ant colony algorithm";
            case 2:
                return "Two-opt algorithm";
            case 3:
                return "Three-opt algorithm";
            default:
                return "Butelka zwrotna 50 groszy";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jButton) {
            var requiredTime = Double.parseDouble(jTextField.getText());
            double[] features = new TspMlpFeatures(tspInstance, requiredTime).featuresToDoubleArray();
            //prediction
            INDArray featuresArray = Nd4j.create(features, new int[]{1, features.length});
            INDArray prediction = model.output(featuresArray);
            int predictedClass = Nd4j.argMax(prediction, 1).getInt(0);
            notifyAllObservers(classNumToAlgorithmName(predictedClass));
        }
    }

    public void notifyAllObservers(String output) {
        for(var obs : observers) {
            obs.updateOutput(output);
        }
    }

    public void attach(AnalysisPanelObserver observer) {
        observers.add(observer);
    }


    @Override
    public void updateInstance(TspInstance tspInstance) {
        this.tspInstance = tspInstance;
    }
}
