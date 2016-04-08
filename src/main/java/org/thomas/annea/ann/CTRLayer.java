package org.thomas.annea.ann;

import org.jblas.DoubleMatrix;

public class CTRLayer extends Layer {

    // The gains matrix
    protected DoubleMatrix gains;

    // The time constraints matrix
    protected DoubleMatrix timeConstraints;

    // Array of y values
    private double[] y;

    /**
     * Constructor for empty layer
     * @param output Ouput size
     */

    public CTRLayer(int output) {
        super(output);
        
        // Set y
        y = new double[output];
    }

    /**
     * Constructor
     * @param input Input size
     * @param output Output size
     */

    public CTRLayer(int input, int output) {
        super(input, output);

        // Set y
        y = new double[output];
    }

    public void setGains(DoubleMatrix matrix) {
        gains = matrix;
    }

    public void setTimeConstraints(DoubleMatrix matrix) {
        timeConstraints = matrix;
    }

    public double getTimeConstraint(int i) {
        return timeConstraints.get(0, i);
    }

    public double getGain(int i) {
        return gains.get(0, i);
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
