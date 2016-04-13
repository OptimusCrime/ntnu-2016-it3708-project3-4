package org.annea.ea.gtype;

import org.annea.tools.settings.AbstractSettings;

import java.util.BitSet;

public class Beer8BitGType extends BinaryGType {

    // The actual length of the geno vector
    public static int actualLength = 0;

    /**
     * Constructor for the G-Type
     * @param values number of values this G-Type should contain
     */

    public Beer8BitGType(AbstractSettings s, int values) {
        super(s, actualLength);

        // See if we need to calculate the length of the vector here
        if (actualLength == 0) {
            calculateVectorLength();
        }

        // Initialize the (empty) BitSet
        value = new BitSet(actualLength);

        // Initialize and set a random value
        initialize();
    }

    /**
     * As no actual length is provided, we must calculate the length of the value vector based on the size of the
     * neural network.
     */

    private void calculateVectorLength() {
        // Keep track of the length
        int tempLength = 0;

        // Get the dimensions
        int[] networkDimensions = settings.getNetworkDimensions();

        // Loop the network
        for (int i = 0; i < networkDimensions.length; i++) {
            // Get the
            int input = networkDimensions[i];

            // Get the output
            int output;
            if ((i + 1) == networkDimensions.length) {
                output = networkDimensions[i];
            }
            else {
                output = networkDimensions[i + 1];
            }


            for (int j = 0; j < output; j++) {
                // Add feed forward
                tempLength += input;

                // Add other weights
                tempLength += output;
            }

            // Add bias
            tempLength += output;

            // Add gains
            tempLength += output;

            // Add time
            tempLength += output;
        }

        // To bit
        tempLength *= 8;

        // Set the actual length
        actualLength = tempLength;

        // Set number of values to the settings
        settings.setNumberOfValues(actualLength);
    }

    /**
     *
     * @return
     */

    @Override
    public String toString() {
        // Build G-Type representation
        StringBuilder builder = new StringBuilder();

        // Get the F-Type representation
        String PTypeString = "P = " + ptype.toString() + ", ";

        return "#" + id + " - " + PTypeString;
    }

}
