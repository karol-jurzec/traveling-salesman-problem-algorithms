package src;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.Nd4jBackend;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.panel.TspAnalyzerFrame;
import src.main.java.tsp.tspmlp.TspMlpFeatures;
import src.main.java.tsp.tspmlp.dataset.DatasetGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
            int nn = random.nextInt(195) + 5;
            generateTSPInstance(nn, path, "random_tsp_" + i +"_"+ nn + ".tsp");
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException, Nd4jBackend.NoAvailableBackendException {

        //generateRandomTspTour(1000);

        //DatasetGenerator dg = new DatasetGenerator();
        //dg.prepareDataSet();

        TspInstance instance = TspInstance.FileToTspInstance(new File("/Users/karol/Desktop/uni/ajio/used_tsp_inst/ch150.tsp"));

        int seed = 123;
        double learningRate = 0.01;
        int batchSize = 50;
        int nEpochs = 30;
        int numInputs = 6;
        int numOutputs = 6;
        int numHiddenNodes = 20;

        RecordReader recordReader = new CSVRecordReader();
        recordReader.initialize(new FileSplit(new File("/Users/karol/Desktop/uni/ajio/data_set/data.csv")));
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, batchSize, 0, 6);


        RecordReader recordReaderEv = new CSVRecordReader();
        recordReaderEv.initialize(new FileSplit(new File("/Users/karol/Desktop/uni/ajio/data_set/data_ev.csv")));
        DataSetIterator iteratorEv = new RecordReaderDataSetIterator(recordReaderEv, batchSize, 0, 6);

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(123) // Set a seed for reproducibility
                .updater(new Adam(learningRate))
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(numInputs)
                        .nOut(64)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(64)
                        .nOut(numOutputs)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build();


        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();

        int numEpochs = 10; // Set the number of training epochs
        for (int epoch = 0; epoch < numEpochs; epoch++) {
            model.fit(iterator);
        }

        Evaluation eval = model.evaluate(iteratorEv);
        System.out.println(eval.stats());



        TspAnalyzerFrame tspAnalyzer = new TspAnalyzerFrame();
    }
}


