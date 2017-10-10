package ch.ethz.inf.vs.a1.fstreun.vs_fstreun_sensors;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by fabio on 10/9/17.
 * Thread Safe version of the GraphContainerImpl
 */

public class GraphContainerThreadSave implements GraphContainer, Serializable {

    private int maxLength = 100;

    private float[][] dataSet = new float[maxLength][];
    private double[] xDataSet = new double[maxLength];

    // length of the current dataSet
    private int currentLength = 0;

    /**
     * Adds Value to the data set
     * @param xValue corresponding to the yValues
     * @param yValues .
     * @throws IllegalArgumentException if yValues is null or not the same size as previous ones
     */
    @Override
    synchronized public void addValues(double xValue, float[] yValues) throws IllegalArgumentException {
        if (yValues == null){
            throw new IllegalArgumentException("Argument yValues can not be Null");
        }
        if (dataSet[0] != null && dataSet[0].length != yValues.length){
            throw new IllegalArgumentException("Argument yValues has to be the same length as the first yValues added");
        }

        if (currentLength < maxLength){
            dataSet[currentLength] = yValues.clone();
            xDataSet[currentLength] = xValue;
            currentLength++;
        }else {

            // the dataSet has already the maximum number of Values.
            // remove first one, shift array and add at the end:
            for (int i = 0; i < maxLength-1; i++){
                dataSet[i] = dataSet[i+1].clone();
                xDataSet[i] = xDataSet[i+1];
            }
            dataSet[maxLength - 1] = yValues;
            xDataSet[maxLength - 1] = xValue;
        }
    }

    /**
     * Adds Value to the data set if the xValue is strict bigger then the latest one
     * @param xValue corresponding to the yValues
     * @param yValues .
     * @return true if Data was added, false if not
     * @throws IllegalArgumentException if yValues is null or not the same size as previous ones
     */
    synchronized public boolean addValuesSafe(double xValue, float[] yValues) throws IllegalArgumentException {
        if (currentLength > 0 && xDataSet[currentLength-1] >= xValue){
            // already bigger or equal xValue added
            return false;
        }else{
            addValues(xValue, yValues);
            return true;
        }
    }

    /**
     * Adds a new xValue and a copy of the latest yValue to the data.
     * If no latest yValue exists an zero array of size numberOfValues is added.
     * @param xValue .
     * @param numberOfValues .
     */
    synchronized public void addXValue(double xValue, int numberOfValues){
        if (currentLength > 0){
            addValues(xValue, dataSet[currentLength-1]);
        }else{
            addValues(xValue, new float[numberOfValues]);
        }
    }

    /**
     * Adds a new xValue and a copy of the latest yValue to the data,
     * if the xValue is strict bigger then the latest xValue added.
     * If no latest yValue exists an zero array of size numberOfValues is added.
     * @param xValue
     * @param numberOfValues
     * @return
     */
    synchronized public boolean addXValueSafe(double xValue, int numberOfValues){
        if (currentLength > 0){
            return addValuesSafe(xValue, dataSet[currentLength-1]);
        }else{
            return addValuesSafe(xValue, new float[numberOfValues]);
        }
    }

    @Override
    synchronized public float[][] getValues() {
        float[][] res = new float[currentLength][];
        System.arraycopy(dataSet, 0, res, 0, currentLength);
        return res;
    }

    synchronized public double[] getXValues() {
        double[] res = new double[currentLength];
        System.arraycopy(xDataSet, 0, res,0,currentLength);
        return res;
    }

    synchronized public ValueContainer getAllValues(){
        return new ValueContainer(getValues(), getXValues());
    }

    public class ValueContainer {
        public float [][] yValues;
        public double [] xValues;

        public ValueContainer(float[][] y, double[] x){
            yValues = y;
            xValues = x;
        }
    }
}
