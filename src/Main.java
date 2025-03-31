import model.Perceptron;
import utils.EvaluationMetrics;
import utils.PrepareDataset;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Perceptron perceptron = null;

        try {
            String filepath = "data/iris.csv";
            double trainRatio = 0.7;
            int maxEpochs = 100;
            double alpha = 0.1;
            double threshold = 0.5;

            PrepareDataset.DatasetSplit split = PrepareDataset.trainTestSplit(filepath, trainRatio);

            double[][] trainInputs = split.trainInputs;
            int[] trainLabels = split.trainLabels;
            double[][] testInputs = split.testInputs;
            int[] testLabels = split.testLabels;

            int dimension = trainInputs[0].length;
            perceptron = new Perceptron(dimension, alpha, threshold);

            double[] epochAccuracies = new double[maxEpochs];
            int epochsRun = perceptron.train(trainInputs, trainLabels, maxEpochs, epochAccuracies);

            int[] prediction = new int[testLabels.length];
            for (int i = 0; i < testInputs.length; i++) {
                prediction[i] = perceptron.predict(testInputs[i]);
            }

            double testAccuracy = EvaluationMetrics.measureAccuracy(testLabels, prediction);
            System.out.println("Training completed in " + epochsRun + " epochs");
            System.out.println("Final test accuracy: " + testAccuracy);

            System.out.println("\nAccuracy per epoch: ");
            for (int i = 0; i < epochsRun; i++) {
                System.out.printf("Epoch %d: %.2f%%\n", i + 1, epochAccuracies[i] * 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("model.txt"))) {
            if (perceptron != null) {
                double[] weights = perceptron.getWeights();
                double threshold = perceptron.getThreshold();

                for (int i = 0; i < weights.length; i++) {
                    writer.write(weights[i] + (i < weights.length - 1 ? "," : ""));
                }
                writer.write("," + threshold);
                writer.newLine();

                System.out.println("\nModel parameters saved to model.txt.");
            } else {
                System.err.println("Perceptron is null. Model not saved.");
            }
        } catch (IOException e) {
            System.err.println("Failed to save weights: " + e.getMessage());
        }
    }
}
