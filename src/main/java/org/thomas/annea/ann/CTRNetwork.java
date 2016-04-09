package org.thomas.annea.ann;


import org.jblas.DoubleMatrix;
import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.tools.settings.AbstractSettings;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CTRNetwork extends Network {

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

    /**
     * Constructor
     * @param s Instance of settings
     */

    public CTRNetwork(AbstractSettings s) {
        super(s);
    }

    /**
     * Create function. Creates the network based on the settings fetched from the config file
     */

    public void create() {
        int[] dimensions = settings.getNetworkDimensions();
        String[] functions = settings.getNetworkFunctions();

        // Create the layer array of correct length
        layers = new CTRLayer[dimensions.length];

        // Now set the correct sizes for each of the layer
        for (int i = 1; i < layers.length; i++) {
            layers[i - 1] = new CTRLayer(dimensions[i - 1], dimensions[i]);

            // Set the function
            if (functions.length >= i) {
                layers[i - 1].setFunction(functions[i - 1]);
            }
            else {
                layers[i - 1].setFunction("Sigmoid");
            }
        }

        // Create output layer, which is empty
        layers[layers.length - 1] = new CTRLayer(dimensions[dimensions.length - 1]);

        // Set the correct output function
        layers[layers.length - 1].setFunction(functions[functions.length - 1]);
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

    /**
     * Translates the G-type value to one or more weights for the network
     * @param individual Instance of G-Type
     */

    public void setWeights(AbstractGType individual) {
        // Get the value vector from the individual
        double[] valueVector = individual.getAsArray();

        // Get number of bytes
        int numberOfBytes = valueVector.length / 8;

        // Create empty array of doubles
        double[] byteValues = new double[numberOfBytes];

        // Populate the byte array
        for (int i = 0; i < numberOfBytes; i++) {
            // Create integer value array
            double byteValue = 0;
            for (int j = 0; j < 8; j++) {
                if ((int) valueVector[(i * 8) + j] == 1) {
                    byteValue += Math.pow(2, 7 - j);
                }
            }

            // Add byte value to array
            byteValues[i] = byteValue;
        }

        // Create various arrays for the weights, gains and the time contraints
        int lengthArray = numberOfBytes / 3;
        double[] weightsArray = new double[lengthArray];
        double[] gainArray = new double[lengthArray];
        double[] timeArray = new double[lengthArray];

        // Populate each of them
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < lengthArray; j++) {
                if (i == 0) {
                    double scaled = scale(byteValues[(i * lengthArray) + j], 0, 265, weightsMin, weightsMax);
                    weightsArray[j] = scaled;
                }
                else if (i == 1) {
                    double scaled = scale(byteValues[(i * lengthArray) + j], 0, 265, gainsMin, gainsMax);
                    gainArray[j] = scaled;
                }
                else {
                    double scaled = scale(byteValues[(i * lengthArray) + j], 0, 265, timeMin, timeMax);
                    timeArray[j] = scaled;
                }
            }
        }

        // Loop all the layers, except the last
        int offset = 0;
        for (int i = 0; i < layers.length - 1; i++) {
            // Get the current layer
            CTRLayer thisLayer = (CTRLayer) layers[i];

            // Reset shit
            thisLayer.resetY();

            // Get the layer size
            int layerSize = layers[i].getRows() * layers[i].getColumns();

            // Set the translated weights for this layer
            thisLayer.setWeights(phenoToMatrix(Arrays.copyOfRange(weightsArray, offset, offset + layerSize), layers[i].getRows(), layers[i].getColumns()));
            thisLayer.setGains(phenoToMatrix(Arrays.copyOfRange(weightsArray, offset, offset + layerSize), layers[i].getRows(), layers[i].getColumns()));
            thisLayer.setTimeConstraints(phenoToMatrix(Arrays.copyOfRange(weightsArray, offset, offset + layerSize), layers[i].getRows(), layers[i].getColumns()));

            // Increase the offset with the current layer size
            offset += layerSize;
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
            // Cast the current layer
            CTRLayer thisLayer = (CTRLayer) layers[i];

            // Store the s value her
            double s = 0;

            // Keep track of each of the output values
            DoubleMatrix outputValues = new DoubleMatrix(1, thisLayer.getColumns());

            // Get the rows
            for (int j = 0; j < thisLayer.getColumns(); j++) {

                // Formula 1
                for (int k = 0; k < thisLayer.getRows() - 1; k++) {
                    s += thisLayer.getWeight(k, j) * layerValues[i].get(0, k);
                }

                // Get interconnected values
                for (int k = 0; k < thisLayer.getColumns() - 1; k++) {
                    s += thisLayer.getLastOutput(k) * thisLayer.getOtherLayerWeight(k);
                }

                // Formula 2
                double timeDerivative = (1 / thisLayer.getTimeConstraint(j)) * ((-1 * thisLayer.getY(j)) + s);

                // Increase the Y value
                thisLayer.increaseY(j, timeDerivative);

                // Formula 3
                double output = 1 / (1 + Math.exp(-1 * thisLayer.getGain(j) * thisLayer.getY(j)));

                // Save output to matrix
                outputValues.put(0, j, output);
            }

            // Update last output
            for (int j = 0; j < thisLayer.getColumns(); j++) {
                thisLayer.setLastOutputs(j, outputValues.get(0, j));
            }

            // Add the layer value
            layerValues[i + 1] = outputValues;
        }

        // Return the final output
        return layerValues[layerValues.length - 2];
    }
}
