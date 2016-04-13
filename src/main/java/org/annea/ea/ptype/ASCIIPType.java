package org.annea.ea.ptype;

import org.annea.ea.gtype.AbstractGType;
import org.annea.tools.settings.AbstractSettings;

public class ASCIIPType extends AbstractPType {

    // Override the internal length for this P-Type to 8 (binary ASCII values)
    static {
        internalLength = 8;
    }

    /**
     * Constructor
     * @param s Instance of settings
     * @param p Instance of the G-type
     */

    public ASCIIPType(AbstractSettings s, AbstractGType p) {
        super(s, p);
    }

    public int[] getRepresentation() {
        int numbers = (int) (gtype.getLength() / (float) 8);
        int[] representation = new int[numbers];

        for (int i = 0; i < numbers; i++) {
            StringBuilder asciiValue = new StringBuilder("00000000");
            for (int j = 0; j < 8; j++) {
                if ((int) gtype.getValue((i * 8) + j) == 1) {
                    asciiValue.setCharAt(j, '1');
                }
            }

            // Add to representation array
            representation[i] = Integer.parseInt(asciiValue.toString(), 2);
        }

        // Return the list of integers
        return representation;
    }

    /**
     * Translates the G-Type values to P-Type values
     * @return Representation as a vector of doubles
     */

    @Override
    public double[] getPRepresentation() {
        double[] binaryArray = gtype.getAsArray();
        double[] translatedValues = new double[binaryArray.length / internalLength];

        // Loop the n values we should translate to
        for (int i = 0; i < translatedValues.length; i++) {
            // Loop 8 and 8 bits
            StringBuilder asciiValue = new StringBuilder("00000000");
            for (int j = 0; j < 8; j++) {
                if (binaryArray[(i * 8) + j] == 1) {
                    asciiValue.setCharAt(j, '1');
                }
            }

            // Add to representation array
            translatedValues[i] = Integer.parseInt(asciiValue.toString(), 2);
        }

        // Return the list of integers
        return translatedValues;
    }

    /**
     * Defines the possible values we can have for the current P-type. For ASCII-type this is the decimal numbers
     * corresponding to the ASCII-values limited by the S-value.
     */

    @Override
    public void initializeValues() {
        int sValue = 2;
        if (settings.getSetting("S") != null) {
            // Get the s-value, which represents how many letters we can chose between
            sValue = Integer.valueOf(settings.getSetting("S"));
        }

        // Set the possible values for this P-Type
        possibleValues = new double[sValue][internalLength];

        for (int i = 0; i < sValue; i++) {
            // Get the correct ASCII value (skipping 91 - 96)
            int value = 65 + i;
            if (value > 90) {
                value += 6;
            }

            // Convert to binary string
            String binaryString = "0" + Integer.toBinaryString(value);

            // Turn into int array and apply to possible values
            for (int j = 0; j < binaryString.length(); j++) {
                int currentIntValue = 0;
                if (binaryString.charAt(j) == '1') {
                    currentIntValue = 1;
                }

                possibleValues[i][j] = currentIntValue;
            }
        }
    }

    /**
     * To String
     * @return Stringification of the instance
     */

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        int[] representation = getRepresentation();

        for (int i = 0; i < representation.length; i++) {
            out.append((char) representation[i]);
        }

        double[] valueVector = getPRepresentation();

        out.append(", L = ");
        for (int i = 0; i < valueVector.length; i++) {
            out.append((int) valueVector[i]);
            if ((i + 1) != valueVector.length) {
                out.append(", ");
            }
        }

        return out.toString();
    }
}
