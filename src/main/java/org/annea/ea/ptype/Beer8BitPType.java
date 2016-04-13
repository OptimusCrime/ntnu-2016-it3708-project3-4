package org.annea.ea.ptype;

import org.annea.ea.gtype.AbstractGType;
import org.annea.tools.settings.AbstractSettings;

public class Beer8BitPType extends BinaryPType {

    /**
     * Constructor
     * @param s Instance of settings
     * @param p Instance of the G-type
     */

    public Beer8BitPType(AbstractSettings s, AbstractGType p) {
        super(s, p);
    }

    public String toString() {
        // Get the value vector from the individual
        double[] valueVector = gtype.getAsArray();

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

        StringBuilder b = new StringBuilder();
        b.append("(" + byteValues.length + ") ");
        for (int i = 0; i < byteValues.length; i++) {
            b.append(byteValues[i]);

            if (i != (byteValues.length - 1)) {
                b.append(", ");
            }
        }
        return b.toString();
    }
}
