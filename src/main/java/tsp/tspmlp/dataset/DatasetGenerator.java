package src.main.java.tsp.tspmlp.dataset;

import src.main.java.tsp.TspSolver;
import src.main.java.tsp.algorithms.*;
import src.main.java.tsp.models.TspInstance;
import src.main.java.tsp.tspmlp.TspMlpFeatures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class DatasetGenerator {

    final private String TSP_INST_PATH = "/Users/karol/Desktop/uni/ajio/generated_tsp_inst/";
    final private String DATA_SET_PATH = "/Users/karol/Desktop/uni/ajio/data_set/data.csv";

    TspSolver bruteForce = new TspSolver(new BruteForceAlgorithm());
    TspSolver heldKarp = new TspSolver(new HeldKarpAlgorithm());
    TspSolver nearestN = new TspSolver(new NearestNeighbourAlgorithm());
    TspSolver twoOpt = new TspSolver(new TwoOptAlgorithm());
    TspSolver threeOpt = new TspSolver(new ThreeOptAlgorithm());
    TspSolver antColony = new TspSolver(new AntColonyAlgorithm(10));


    private double generateRandomTime(int numberOfNodes) {
        double baseTime = 0.001; // Base time for small instances
        double timeMultiplier = 0.0001; // Multiplier for each node
        double randomnessFactor = 0.55; // Introduce some randomness

        // Generate a random additional time based on the number of nodes
        double additionalTime = timeMultiplier * numberOfNodes * numberOfNodes;

        // Introduce randomness
        double randomComponent = randomnessFactor * additionalTime * new Random().nextDouble();

        // Combine base time, additional time, and randomness
        return baseTime + additionalTime + randomComponent;
    }

    private ArrayList<TspInstance> readTspInstancesFromFolder() {
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


    private static final ExecutorService executor = Executors.newCachedThreadPool();


    private void submitAlgorithmTask(TspInstance tspInstance, TspSolver algorithm, double requiredTime,
                                     ArrayList<Future<Result>> futures) {
        Callable<Result> task = () -> computeResultForAlgorithm(tspInstance, algorithm);
        Future<Result> future = executor.submit(task);
        futures.add(future);
    }

    private String computeBestAlgorithm(TspInstance tspInstance, double requiredTime) {
        ArrayList<Future<Result>> futures = new ArrayList<>();

        if(tspInstance.getSize() < 8) {
            submitAlgorithmTask(new TspInstance(tspInstance), bruteForce, requiredTime, futures);
        }
        if(tspInstance.getSize() < 17) {
            submitAlgorithmTask(new TspInstance(tspInstance), heldKarp, requiredTime, futures);
        }
        submitAlgorithmTask(tspInstance, antColony, requiredTime, futures);
        submitAlgorithmTask(tspInstance, twoOpt, requiredTime, futures);
        submitAlgorithmTask(tspInstance, threeOpt, requiredTime, futures);
        submitAlgorithmTask(tspInstance, nearestN, requiredTime, futures);

        String bestAlgorithm = "";
        double bestDistance = Double.MAX_VALUE;

        try {
            // Wait for all tasks to complete and find the best result
            for (Future<Result> future : futures) {
                Result result = future.get();
                if (result != null && result.elapsedTime <= requiredTime && result.distance < bestDistance) {
                    bestDistance = result.distance;
                    bestAlgorithm = result.name;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return bestAlgorithm;

/*
        double start = 0.0, elapsedTime = 0.0, currMin = Double.MAX_VALUE, bestMin = Double.MAX_VALUE;
        String currBest = "", withBestLen = "";

        //brute force
        if(tspInstance.getSize() <= 8) {
            var res = computeResultForAlgorithm(tspInstance, bruteForce);
            if(res.elapsedTime <= requiredTime) {
               currMin = res.elapsedTime;
               currBest = "Brute force";
            }
        }

        //held karp
        if(tspInstance.getSize() < 17) {
            var res = computeResultForAlgorithm(tspInstance, heldKarp);
            if(res.elapsedTime <= requiredTime) {
                if(res.distance < currMin) {
                    currBest = "Held karp";
                    currMin = res.distance;
                }
            }
        }

        //ant colony
        var res = computeResultForAlgorithm(tspInstance, antColony);
        if(res.elapsedTime <= requiredTime) {
            if(res.distance < currMin) {
                currBest = "Ant colony";
                currMin = res.distance;
            }
        }

        //two-opt
        res = computeResultForAlgorithm(tspInstance, twoOpt);
        if(res.elapsedTime <= requiredTime) {
            if(res.distance < currMin) {
                currBest = "Two-opt";
                currMin = res.distance;
            }
        }

        //three-opt
        res = computeResultForAlgorithm(tspInstance, threeOpt);
        if(res.elapsedTime <= requiredTime) {
            if(res.distance < currMin) {
                currBest = "Three-opt";
                currMin = res.distance;
            }
        }

        //nearest neighbour
        res = computeResultForAlgorithm(tspInstance, nearestN);
        if(res.elapsedTime <= requiredTime) {
            if(res.distance < currMin) {
                currBest = "Nearest-neighbour";
                currMin = res.distance;
            }
        }

        if(currMin == Double.MAX_VALUE) {
            System.out.println();
        }

        return currBest;*/
    }

    private String getFeaturesString(TspInstance tspInstance, double timeToCalc) {
        TspMlpFeatures tspMlpFeatures = new TspMlpFeatures(tspInstance,timeToCalc);
        return tspMlpFeatures.toString();
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
            double timeToCalc = generateRandomTime(inst.getSize());
            String label = computeBestAlgorithm(inst, timeToCalc);
            String features = getFeaturesString(inst, timeToCalc);
            saveDataSetToPath(DATA_SET_PATH, label, features);
        }

        executor.shutdown();
    }

    public void prepareDataSet() {
        var tspInstances = readTspInstancesFromFolder();
        processInstances(tspInstances);
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


