package model;

public class Perceptron {
    private int dimension;
    private double [] weights;
    private double threshold;
    private double alpha;

    public Perceptron(int dimension, double alpha,double threshold){
        this.dimension=dimension;
        this.alpha=alpha;
        this.threshold=threshold;
        this.weights = new double[dimension];
        initializeWeights();
    }
    private void initializeWeights(){
        for(int i = 0; i< weights.length; i++){
            weights[i] = Math.random() - 0.5;
        }
    }
    public int predict(double[] inputs){
        double sum = 0.0;
        for(int i = 0; i < inputs.length; i++){
            sum += weights[i] * inputs[i];
        }
        return sum >= threshold ? 1 : 0;
    }
    public int train(double [][] inputs, int [] labels, int maxEpochs,double[] epochAccuracies){
        int epoch= 0;
        int numSamples = inputs.length;

        while (epoch < maxEpochs){
            int correct = 0;
            for(int i = 0 ; i <numSamples; i++){
                int prediction = predict(inputs[i]);
                int error = labels[i] - prediction;

                //weight update
                for (int j = 0; j < weights.length; j++){
                    weights[j] += alpha*error*inputs[i][j];
                }
                if (error == 0) correct++;
            }
            epochAccuracies[epoch] = (double)correct / (double)numSamples;
            if (correct == numSamples) break;
            epoch++;
        }
        return epoch;
    }
    public double[] getWeights(){
        return weights;
    }
    public double getThreshold(){
        return threshold;
    }
}
