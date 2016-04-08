package org.thomas.annea.ann;

import org.jblas.DoubleMatrix;

public class CTRLayer extends Layer {

    // The gains matrix
    protected DoubleMatrix gains;

    // The time constraints matrix
    protected DoubleMatrix timeConstraints;

    // Store the last output
    private double[] lastOutput;

    // Array of y values
    private double[] y;

    /**
     * Constructor for empty layer
     * @param output Ouput size
     */

    public CTRLayer(int output) {
        super(output);

        // Set last output to null
        lastOutput = null;

        // Set y
        y = new double[output];

        // Store the last values
        lastOutput = new double[output];
    }

    /**
     * Constructor
     * @param input Input size
     * @param output Output size
     */

    public CTRLayer(int input, int output) {
        super(input, output);

        // Set y
        y = new double[input];

        // Store the last values
        lastOutput = new double[input];
    }

    public void setGains(DoubleMatrix matrix) {
        gains = matrix;
    }

    public void setTimeConstraints(DoubleMatrix matrix) {
        timeConstraints = matrix;
    }

    public double getLastOutput(int row) {
        return lastOutput[row];
    }

    public void setLastOutput(int row, double value) {
        lastOutput[row] = value;
    }

    public double getOtherLayerWeight(int row, int column) {
        double value = 0;
        for (int i = 0; i < weights.getColumns(); i++) {
            if (i != column) {
                value += weights.get(row, i);
            }
        }

        // Return the average
        return value / (weights.getColumns() - 1);
    }

    public double getTimeConstraint() {
        return timeConstraints.get(0, 0);
    }

    public double getGain() {
        return gains.get(0, 0);
    }

    public double getBias() {
        return gains.get(0, 0);
    }

    public double getY(int i) {
        return y[i];
    }

    public void resetY() {
        for (int i = 0; i < y.length; i++) {
            y[i] = 0;
        }
    }

    public void increaseY(int i, double value) {
        y[i] += value;
    }
}
