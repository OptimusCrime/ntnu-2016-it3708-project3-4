package org.thomas.annea.ea.gtype;

import org.thomas.annea.tools.settings.AbstractSettings;

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
        for (int i = 1; i < networkDimensions.length; i++) {
            // Add size of the weights
            tempLength += networkDimensions[i - 1] * networkDimensions[i];
        }

        // Add one for each dimension (bias)
        tempLength += networkDimensions.length;

        // To bit
        tempLength *= 8;

        // Multiply by three (gains and time constraints)
        tempLength *= 3;

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
