package org.annea.ann;


import org.annea.ea.gtype.AbstractGType;
import org.jblas.DoubleMatrix;
import org.annea.tools.settings.AbstractSettings;

import java.util.Arrays;

public class CTRNetwork extends Network {

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

        // Create the layer array of correct length
        layers = new CTRLayer[dimensions.length];

        // Now set the correct sizes for each of the layer
        for (int i = 1; i < layers.length; i++) {
            layers[i - 1] = new CTRLayer(dimensions[i - 1], dimensions[i]);
        }

        // Create output layer, which is empty
        layers[layers.length - 1] = new CTRLayer(dimensions[dimensions.length - 1], dimensions[dimensions.length - 1]);
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
        double[] weightsArray = new double[numberOfBytes];

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
            weightsArray[i] = byteValue;
        }

        // Loop all the layers, except the last
        int offset = 0;
        for (int i = 0; i < layers.length; i++) {
            // Get the current layer
            CTRLayer thisLayer = (CTRLayer) layers[i];

            // Reset the layer
            thisLayer.reset();

            // Get the layer size
            int input = layers[i].getRows();
            int output = layers[i].getColumns();
            int layerSize = input + output + 3;

            // Loop the number of outputs in this layer
            for (int j = 0; j < output; j++) {
                // Get all the weights for this neuron
                double[] neuronWeights = Arrays.copyOfRange(weightsArray, offset, offset + layerSize);

                // Set the normal weights for this neuron
                thisLayer.getNeurons().get(j).setWeights(Arrays.copyOfRange(neuronWeights, 0, input));

                // Set bias for this neuron
                thisLayer.getNeurons().get(j).setBias(neuronWeights[input]);

                // Set layer weights
                thisLayer.getNeurons().get(j).setOtherWeights(Arrays.copyOfRange(neuronWeights, input + 1, (output + input + 1)));

                // Set gains
                thisLayer.getNeurons().get(j).setGain(neuronWeights[neuronWeights.length - 2]);

                // Set time constant
                thisLayer.getNeurons().get(j).setTimeConstant(neuronWeights[neuronWeights.length - 1]);

                // Increase the offset with the current layer size
                offset += layerSize;
            }
        }
    }

    /**
     * Propagate the input through the network
     * @param input The input values (from the sensors)
     * @return The output matrix from the last layer in the network
     */

    public DoubleMatrix propagate(DoubleMatrix input) {
        // Store the current value
        DoubleMatrix currentValue = input;

        // Propagate the train method
        for (int i = 0; i < layers.length; i++) {
            // Cast the current layer
            CTRLayer thisLayer = (CTRLayer) layers[i];

            // Store the s value her
            double s = 0;

            // Keep track of each of the output values
            DoubleMatrix outputValues = new DoubleMatrix(1, thisLayer.getNeurons().size());

            // Get each neuron
            for (int j = 0; j < thisLayer.getNeurons().size(); j++) {
                // Shortcut
                CTRNeuron currentNeuron = thisLayer.getNeurons().get(j);

                // Formula 1
                for (int k = 0; k < thisLayer.getRows(); k++) {
                    s += currentNeuron.getWeight(k) * currentValue.get(0, k);
                }

                // Get interconnected values
                for (int k = 0; k < thisLayer.getNeurons().size(); k++) {
                    s += thisLayer.getNeurons().get(k).getLastOutput() * currentNeuron.getOtherWeight(k);
                }

                // Formula 2
                double timeDerivative = (1 / currentNeuron.getTimeConstant()) * (-1 * currentNeuron.getY() + s + 1 * currentNeuron.getBias());

                // Increase the Y value
                currentNeuron.increaseY(timeDerivative);

                // Formula 3
                double output = 1 / (1 + Math.exp(-1 * currentNeuron.getGain() * currentNeuron.getY()));



                // Save output to matrix
                outputValues.put(0, j, output);
            }

            // Update last output
            for (int j = 0; j < thisLayer.getNeurons().size(); j++) {
                thisLayer.getNeurons().get(j).setLastOutput(outputValues.get(0, j));
            }

            // Update the current matrix
            currentValue = outputValues;
        }

        // Return the final output
        return currentValue;
    }

    public Layer[] getLayers() {
        return layers;
    }
}
