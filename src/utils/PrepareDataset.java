package utils;
import java.io.*;
import java.util.*;

public class PrepareDataset {
    public static class DatasetSplit {
        public double[][] trainInputs;
        public double[][] testInputs;
        public int[] trainLabels;
        public int[] testLabels;
    }

    public static DatasetSplit trainTestSplit(String csvPath, double trainRatio) throws IOException {
        List<double[]> inputList = new ArrayList<>();
        List<Integer> labelList = new ArrayList<>();

        //load and filter the dataset
        BufferedReader br = new BufferedReader(new FileReader(csvPath));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] tokens = line.split(",");
            if (tokens.length < 5) continue;
            String labelStr = tokens[4].trim().toLowerCase();
            if (!(labelStr.equals("setosa") || labelStr.equals("versicolor"))) continue;

            double[] features = new double[4];
            for (int i = 0; i < 4; i++) {
                features[i] = Double.parseDouble(tokens[i]);
            }
            int label = labelStr.equals("setosa") ? 1 : 0;
            inputList.add(features);
            labelList.add(label);
        }
        br.close();
        List<double[]> class0Inputs = new ArrayList<>();
        List<double[]> class1Inputs = new ArrayList<>();

        for (int i = 0; i < labelList.size(); i++) {
            if (labelList.get(i) == 0) {
                class0Inputs.add(inputList.get(i));
            } else {
                class1Inputs.add(inputList.get(i));
            }

        }
        DatasetSplit split = new DatasetSplit();
        split = stratifiedSplit(class0Inputs, class1Inputs, trainRatio);
        return split;
    }
private static DatasetSplit stratifiedSplit(List<double[]> class0, List<double[]> class1, double trainRatio) {
        Collections.shuffle(class0);
        Collections.shuffle(class1);

        int trainSize0 = (int) (trainRatio * class0.size());
        int trainSize1 = (int) (trainRatio * class1.size());

        List<double[]> trainInputs = new ArrayList<>();
        List<Integer> trainLabels = new ArrayList<>();
        List<double[]> testInputs = new ArrayList<>();
        List<Integer> testLabels = new ArrayList<>();
        for (int i = 0; i < class0.size(); i++) {
            if( i < trainSize0){
                trainInputs.add(class0.get(i));
                trainLabels.add(0);
            }else{
                testInputs.add(class0.get(i));
                testLabels.add(0);
            }
        }
        for (int i = 0; i < class1.size(); i++) {
            if( i < trainSize1){
                trainInputs.add(class1.get(i));
                trainLabels.add(1);
            }else{
                testInputs.add(class1.get(i));
                testLabels.add(1);
            }
        }
    DatasetSplit split = new DatasetSplit();
    split.trainInputs = trainInputs.toArray(new double[0][0]);
    split.trainLabels = trainLabels.stream().mapToInt(i -> i).toArray();
    split.testInputs = testInputs.toArray(new double[0][0]);
    split.testLabels = testLabels.stream().mapToInt(i -> i).toArray();
    return split;

}
}
