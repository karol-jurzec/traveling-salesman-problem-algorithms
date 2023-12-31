package src;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.LabelLastTimeStepPreProcessor;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.Nd4jBackend;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import src.main.java.tsp.TspSolver;
import src.main.java.tsp.algorithms.ChristofidesAlgorithm;
import src.main.java.tsp.algorithms.HeldKarpAlgorithm;
import src.main.java.tsp.algorithms.NearestNeighbourAlgorithm;
import src.main.java.tsp.algorithms.TwoOptAlgorithm;
import src.main.java.tsp.models.Point;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.panel.TspAnalyzerFrame;
import src.main.java.tsp.test.TspTimer;
import src.main.java.tsp.tspmlp.TspMlpFeatures;
import src.main.java.tsp.tspmlp.dataset.DatasetGenerator;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;

import static src.main.java.tsp.tspmlp.NormalizeData.NormalizeData.*;

public class Main {

    public static int RANGE = 900;

    public static void generateTSPInstance(int n, String path, String instanceName) {
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + instanceName))) {
            // Write header

            writer.write("COMMENT : TSP instance with " + n + " points");
            writer.newLine();
            writer.write("NODE_COORD_SECTION");
            writer.newLine();

            // Write node coordinates
            for (int i = 1; i <= n; i++) {
                double x = random.nextDouble() * 200;
                double y = random.nextDouble() * 200;

                writer.write(i + " " + x + " " + y);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateRandomTspTour(int n) {
        String path = "/Users/karol/Desktop/uni/ajio/generated_tsp_inst/";
        Random random = new Random();

        for(int i = 1; i <= n; ++i) {
            int nn = random.nextInt(20) + 5;
            generateTSPInstance(nn, path, "random_tsp_" + i +"_"+ nn + ".tsp");
        }
    }

    public static void modelLearningCode() {
        //generateRandomTspTour(800);

        //DatasetGenerator dg = new DatasetGenerator();
        //dg.prepareDataSet();

      /*  int seed = 123;
        double learningRate = 0.01;
        int batchSize = 50;
        int nEpochs = 30;
        int numInputs = 32;
        int numOutputs = 4;
        int numHiddenNodes = 20;



        RecordReader recordReader = new CSVRecordReader();
        recordReader.initialize(new FileSplit(new File("/Users/karol/Desktop/uni/ajio/data_set/data_test_normalized.csv")));
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, batchSize, 0, 4);

        RecordReader recordReaderEv = new CSVRecordReader();
        recordReaderEv.initialize(new FileSplit(new File("/Users/karol/Desktop/uni/ajio/data_set/data_test_normalized_ev.csv")));
        DataSetIterator iteratorEv = new RecordReaderDataSetIterator(recordReaderEv, batchSize, 0, 4);



        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(123) // Set a seed for reproducibility
                .updater(new Adam(learningRate))
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(numInputs)
                        .nOut(numOutputs)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(numInputs)
                        .nOut(numOutputs)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build();



        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.addListeners(new ScoreIterationListener(50));
        model.init();

        int numEpochs = 10; // Set the number of training epochs
        for (int epoch = 0; epoch < numEpochs; epoch++) {
            model.fit(iterator);
        }

        Evaluation eval = model.evaluate(iteratorEv);
        System.out.println(eval.stats());

        model.save(new File("/Users/karol/Desktop/uni/ajio/data_set/tsp_model.zip"), true);*/
    }




    public static void main(String[] args) {

        TspTimer.algorithmTimer(6000, 50, new TwoOptAlgorithm(), "2-opt");
        //TspAnalyzerFrame tspAnalyzer = new TspAnalyzerFrame();
    }
}


