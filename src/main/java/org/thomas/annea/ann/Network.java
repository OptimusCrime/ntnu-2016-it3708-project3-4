package org.thomas.annea.ann;

import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.tools.settings.AbstractSettings;

import org.jblas.DoubleMatrix;

import java.lang.reflect.Method;

public class Network {

    // Settings
    private AbstractSettings settings;

    // List of layers
    private Layer[] layers;

    /**
     * Constructor
     * @param s Instance of settings
     */

    public Network(AbstractSettings s) {
        settings = s;
    }

    /**
     * Create function. Creates the network based on the settings fetched from the config file
     */

    public void create() {
        int[] dimensions = settings.getNetworkDimensions();
        String[] functions = settings.getNetworkFunctions();

        // Create the layer array of correct length
        layers = new Layer[dimensions.length];

        // Now set the correct sizes for each of the layer
        for (int i = 1; i < layers.length; i++) {
            layers[i - 1] = new Layer(dimensions[i - 1], dimensions[i]);

            // Set the function
            if (functions.length >= i) {
                layers[i - 1].setFunction(functions[i - 1]);
            }
            else {
                layers[i - 1].setFunction("Sigmoid");
            }
        }

        // Create output layer, which is empty
        layers[layers.length - 1] = new Layer(dimensions[dimensions.length - 1]);

        // Set the correct output function
        layers[layers.length - 1].setFunction(functions[functions.length - 1]);
    }

    /**
     * Translates a vector to a matrix for the weights on the dimensions given by the layers
     * @param values The pheno type vector
     * @param rows Number of rows in the matrix
     * @param columns Number of columns in the matrix
     * @return The matrix for the weight
     */

    private static DoubleMatrix phenoToMatrix(double[] values, int rows, int columns) {
        // Create a new array for the weights
        double [][]weights = new double[rows][columns];

        // Keep track of the actual index
        int actualIndex = 0;

        // Loop the entire matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Apply the weight to the matrix
                weights[i][j] = values[actualIndex];

                // Incrase the index
                actualIndex++;
            }
        }

        // Return the new matrix
        return new DoubleMatrix(weights);
    }

    /**
     * Translates the G-type value to one or more weights for the network
     * @param individual Instance of G-Type
     */

    public void setWeights(AbstractGType individual) {
        // Get the value vector from the individual
        double[] valueVector = individual.getAsArray();

        // Loop all the layers, except the last
        for (int i = 0; i < layers.length - 1; i++) {
            // Set the translated weights for this layer
            layers[i].setWeights(phenoToMatrix(valueVector, layers[i].getRows(), layers[i].getColumns()));
        }
    }

    /**
     * Propagate the input through the network
     * @param input The input values (from the sensors)
     * @return The output matrix from the last layer in the network
     */

    public DoubleMatrix propagate(DoubleMatrix input) {
        // Create a array to populate with the intermediate layer values
        DoubleMatrix[] layerValues = new DoubleMatrix[layers.length + 1];

        // Set the input data to the input layer
        layerValues[0] = input;

        // Propagate the train method
        for (int i = 0; i < layers.length - 1; i++) {
            // Calculate the
            input = layers[i].calc(input);

            // Check if we should apply any function
            Method m = layers[i].getFunction();
            if (m != null) {
                // We have a method, try to call it
                try {
                    // Try to invoke the dynamic method and cast the result to a double matrix
                    input = (DoubleMatrix) m.invoke("apply", input, false);
                }
                catch (Exception e) {
                    System.out.println("Activation function not found");
                }
            }

            // Add the layer value
            layerValues[i + 1] = input;
        }

        // Get the output layer
        DoubleMatrix output = layerValues[layerValues.length - 2];

        // Check if we should apply any function
        Method m = layers[layers.length - 1].getFunction();
        if (m != null) {
            // We have a method, try to call it
            try {
                // Try to invoke the dynamic method and cast the result to a double matrix
                output = (DoubleMatrix) m.invoke("apply", output, false);
            }
            catch (Exception e) {
                System.out.println("Activation function not found");
            }
        }

        // Return the final output
        return output;
    }
}
