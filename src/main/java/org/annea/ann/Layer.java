package org.annea.ann;

import org.jblas.DoubleMatrix;

import java.lang.reflect.Method;

public class Layer {

    // Number of rows in the layer
    protected int rows;

    // NUmber of columns in the layer
    protected int columns;

    // The weight matrix
    protected DoubleMatrix weights;

    // Activation function goes here
    protected String function;

    /**
     * Constructor for empty layer
     * @param output Ouput size
     */

    public Layer(int output) {
        // This is an empty layer
        columns = output;
    }

    /**
     * Constructor
     * @param input Input size
     * @param output Output size
     */

    public Layer(int input, int output) {
        // Set the various sizes here
        rows = input;
        columns = output;

        //weights = DoubleMatrix.rand(input, output);
        weights = new DoubleMatrix();
    }

    /**
     * Set the weights for this layer
     * @param v The weight matrix
     */

    public void setWeights(DoubleMatrix v) {
        weights = v;
    }

    /**
     * Calculate the weights for this layer
     * @param input The input matrix
     * @return The output matrix
     */

    public DoubleMatrix calc(DoubleMatrix input) {
        return input.mmul(weights);
    }

    /**
     * Get the weights for this layer
     * @return The weight matrix
     */

    public DoubleMatrix getWeights() {
        return weights;
    }

    public double getWeight(int i, int j) {
        return weights.get(i, j);
    }

    /**
     * Getters for the rows and columns
     * @return Row or column value
     */

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    /**
     * Returns the activation method for this layer
     * @return The method
     */

    @SuppressWarnings("unchecked")
    public Method getFunction() {
        try {
            String functionClass = "Sigmoid";
            if (function != null) {
                functionClass = function;
            }

            // Try to fetch the class
            Class c = Class.forName("org.annea.ann.functions." + functionClass);

            // Check if the class has the correct declared method
            return c.getDeclaredMethod("apply", DoubleMatrix.class, boolean.class);
        }
        catch (Exception e) {
            // Something went to hell
            return null;
        }
    }

    /**
     * Setter for function
     * @param f Sets the function (the string representation for it)
     */

    public void setFunction (String f) {
        function = f;
    }

    /**
     * To String method
     * @return The stringification of the instance
     */

    public String toString() {
        return weights.toString();
    }
}
