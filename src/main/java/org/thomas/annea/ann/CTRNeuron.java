package org.thomas.annea.ann;

import java.util.List;

public class CTRNeuron {

    // Weights
    public final static double weightsMin = -5;
    public final static double weightsMax = 5;

    // Biases
    public final static double biasMin = -10;
    public final static double biasMax = 0;

    // Gains
    public final static double gainsMin = 1;
    public final static double gainsMax = 5;

    // Time constants
    public final static double timeMin = 1;
    public final static double timeMax = 2;

    private double[] weights;
    private double[] otherWeights;
    private double bias;
    private double gain;
    private double timeConstant;
    private double y;
    private double lastOutput;

    public CTRNeuron() {
        // Set y to null
        y = 0;

        // Set last output to null
        lastOutput = 0;
    }

    public void setWeights(double[] w) {
        weights = new double[w.length];

        for (int i = 0; i < w.length; i++) {
            weights[i] = scale(w[i], 0, 255, weightsMin, weightsMax);
        }

    }

    public void setBias(double b) {
        bias = scale(b, 0, 255, biasMin, biasMax);
    }

    public void setOtherWeights(double[] w) {
        otherWeights = new double[w.length];

        for (int i = 0; i < w.length; i++) {
            otherWeights[i] = scale(w[i], 0, 255, weightsMin, weightsMax);
        }
    }

    public void setGain(double g) {
        gain = scale(g, 0, 255, gainsMin, gainsMax);

    }

    public void setTimeConstant(double tc) {
        timeConstant = scale(tc, 0, 255, timeMin, timeMax);
    }

    public void increaseY(double value) {
        y += value;
    }

    public void setLastOutput(double value) {
        lastOutput = value;
    }

    /**
     * Scale function
     * @param valueIn Actual value
     * @param baseMin
     * @param baseMax
     * @param limitMin
     * @param limitMax
     * @return
     */

    public static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin, final double limitMax) {
        return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
    }

    public double getWeight(int index) {
        return weights[index];
    }

    public double getBias() {
        return bias;
    }

    public double getOtherWeight(int index) {
        return otherWeights[index];
    }

    public double getGain() {
        return gain;
    }

    public double getTimeConstant() {
        return timeConstant;
    }

    public double getY() {
        return y;
    }

    public double getLastOutput() {
        return lastOutput;
    }
}
