package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import java.io.Serializable;

/**
 * Created by fabio on 10/5/17.
 * stores all the data of the Graph
 */

@SuppressWarnings("unused")
public class GraphContainerImpl implements GraphContainer, Serializable {


    private int maxLength = 100;

    private float[][] dataSet = new float[maxLength][];
    private double[] xDataSet = new double[maxLength];

    // length of the current dataSet
    private int currentLength = 0;


    /**
     *
     * @param xIndex The x index.
     * @param values The values.
     * @throws IllegalArgumentException when values is null or not the same length as the first value added
     */
    @Override
    public void addValues(double xIndex, float[] values) throws IllegalArgumentException {
        if (values == null){
            throw new IllegalArgumentException("Argument values can not be Null");
        }
        if (dataSet[0] != null && dataSet[0].length != values.length){
            throw new IllegalArgumentException("Argument values has to be the same length as the first values added");
        }

        if (currentLength < maxLength){
            dataSet[currentLength] = values.clone();
            xDataSet[currentLength] = xIndex;
            currentLength++;
        }else {

            // the dataSet has already the maximum number of Values.
            // remove first one, shift array and add at the end:
            for (int i = 0; i < maxLength-1; i++){
                dataSet[i] = dataSet[i+1].clone();
                xDataSet[i] = xDataSet[i+1];
            }
            dataSet[maxLength - 1] = values;
            xDataSet[maxLength - 1] = xIndex;
        }
    }

    @Override
    public float[][] getValues() {

        float[][] res = new float[currentLength][];

        System.arraycopy(dataSet, 0, res, 0, currentLength);

        return res;
    }


    public double[] getxIndexs(){
        double[] res = new double[currentLength];
        System.arraycopy(xDataSet, 0, res,0,currentLength);
        return res;
    }

}
