package src.main.java.tsp.tspmlp.dataset;

import src.main.java.tsp.TspSolver;
import src.main.java.tsp.algorithms.*;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.tspmlp.TspMlpFeatures;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static src.main.java.tsp.tspmlp.NormalizeData.NormalizeData.*;

public class DatasetGenerator {

    final private String TSP_INST_PATH = "/Users/karol/Desktop/uni/ajio/generated_tsp_inst/";

    final private String DATA_SET_PATH = "/Users/karol/Desktop/uni/ajio/data_set/data_test.csv";
    final private String DATA_SET_PATH_EV = "/Users/karol/Desktop/uni/ajio/data_set/data_ev.csv";


    private static final ExecutorService executor = Executors.newCachedThreadPool();

    TspSolver nearestN = new TspSolver(new NearestNeighbourAlgorithm());
    TspSolver twoOpt = new TspSolver(new TwoOptAlgorithm());
    TspSolver threeOpt = new TspSolver(new ThreeOptAlgorithm());
    TspSolver antColony = new TspSolver(new AntColonyAlgorithm(10));

    private double generateRandomTime(int numberOfNodes) {
        double baseTime = 0.001; // Base time for small instances
        double timeMultiplier = 0.0001; // Multiplier for each node
        double randomnessFactor = 0.55; // Introduce some randomness

        double additionalTime = timeMultiplier * numberOfNodes * numberOfNodes;

        double randomComponent = randomnessFactor * additionalTime * new Random().nextDouble();

        return baseTime + additionalTime + randomComponent;
    }

    public ArrayList<TspInstance> readTspInstancesFromFolder() {
        File folder = new File(TSP_INST_PATH);
        File[] listOfFiles = folder.listFiles();
        ArrayList<TspInstance> tspInstances = new ArrayList<>();

        for(var file : listOfFiles) {
            if(file.getName().contains("tsp")) {
                var tspInst = TspInstance.FileToTspInstance(file);
                tspInstances.add(tspInst);
            }
        }

        return tspInstances;
    }

    private Result computeResultForAlgorithm(TspInstance tspInstance, TspSolver tspSolver) {
        double start = System.currentTimeMillis();
        var solution = tspSolver.solve(tspInstance).getTotalDistance();
        double elapsedTime = System.currentTimeMillis() - start;
        return new Result(solution, elapsedTime/1000.0, tspSolver.toString());
    }

    private void submitAlgorithmTask(TspInstance tspInstance, TspSolver algorithm,
                                     ArrayList<Future<Result>> futures) {
        Callable<Result> task = () -> computeResultForAlgorithm(tspInstance, algorithm);
        Future<Result> future = executor.submit(task);
        futures.add(future);
    }

    private String computeBestAlgorithm(TspInstance tspInstance, double requiredTime) {
        ArrayList<Future<Result>> futures = new ArrayList<>();

        submitAlgorithmTask(tspInstance, antColony, futures);
        submitAlgorithmTask(tspInstance, twoOpt, futures);
        submitAlgorithmTask(tspInstance, threeOpt, futures);
        submitAlgorithmTask(tspInstance, nearestN, futures);

        String bestAlgorithm = "";
        double bestDistance = Double.MAX_VALUE;

        try {
            // Wait for all tasks to complete and find the best result
            for (Future<Result> future : futures) {
                Result result = future.get();
                if( result.distance == bestDistance)
                    return "";
                if (result != null && result.elapsedTime < requiredTime && result.distance < bestDistance) {
                    bestDistance = result.distance;
                    bestAlgorithm = result.name;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return bestAlgorithm;
    }

    private String getFeaturesString(TspInstance tspInstance, double timeToCalc) {
        TspMlpFeatures tspMlpFeatures = new TspMlpFeatures(tspInstance,timeToCalc);
        return tspInstance.getSize() + "," + timeToCalc + tspMlpFeatures;
    }

    private void saveLabelAndFeatureNamesToPath(String path, String featureNames) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.append("Algorithm name" + "," + featureNames);
            writer.newLine(); // Add a new line for clarity
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    private void saveDataSetToPath(String path, String label, String features) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.append(label + "," + features);
            writer.newLine(); // Add a new line for clarity
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }

    private void processInstances(ArrayList<TspInstance> tspInstances) {
        //saveLabelAndFeatureNamesToPath(DATA_SET_PATH, TspMlpFeatures.getFeatureNames());
        for(var inst : tspInstances) {
            double time = generateRandomTime(inst.getSize());
            String label = computeBestAlgorithm(inst, time);
            if(label != "") {
                String features = getFeaturesString(inst, time);
                saveDataSetToPath(DATA_SET_PATH, label, features);
            }
        }

        executor.shutdown();
    }

    private void normalizeDataSet(String path) throws FileNotFoundException {
        List<List<Double>> data = readCSV(path);
        normalizeColumns(data);
        writeCSV(path + "v2", data);
    }

    public void prepareDataSet() throws FileNotFoundException {
        var tspInstances = readTspInstancesFromFolder();
        processInstances(tspInstances);
        normalizeDataSet(DATA_SET_PATH);
    }

    class Result {
        public String name;
        public double distance;
        public double elapsedTime;

        public Result(double distance, double elapsedTime, String name ) {
            this.name = name;
            this.distance = distance;
            this.elapsedTime = elapsedTime;
        }
    }
}


