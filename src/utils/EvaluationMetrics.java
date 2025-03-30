package utils;

public class EvaluationMetrics {
    public static double measureAccuracy(int[] trueLabels, int[] predictedLabels){
        if(trueLabels.length != predictedLabels.length){
            throw new IllegalArgumentException("Array must be the same length");
        }
        int correct = 0;
        for (int i = 0; i < trueLabels.length; i++) {
            if(trueLabels[i] == predictedLabels[i]){
             correct++;
            }
        }
        return (double) correct / trueLabels.length;
    }
}
