package org.annea.ea.ptype;

import org.annea.ea.gtype.AbstractGType;
import org.annea.tools.settings.AbstractSettings;

public abstract class AbstractPType {

    // Defines the bit length of one value for type P-type
    public static int internalLength = 1;

    // Defines the possible values for this P-type
    protected static double[][] possibleValues;

    // The settings
    protected AbstractSettings settings;

    // Reference to the corresponding G-type
    protected AbstractGType gtype;

    /**
     * Constructor
     * @param s Instance of settings
     * @param p Instance of the G-type
     */

    public AbstractPType(AbstractSettings s, AbstractGType p) {
        settings = s;

        // Check if we have the list of possible values
        if (possibleValues == null) {
            initializeValues();
        }

        // Store reference to the P-Type
        gtype = p;
    }

    /**
     * Translates the G-Type values to P-Type values
     * @return Representation as a vector of doubles
     */

    public double[] getPRepresentation() {
        return gtype.getAsArray();
    }


    /**
     * Returns the P-Type value
     * @return
     */

    public int[] getValue() {
        int[] value = new int[gtype.getLength()];
        for (int i = 0; i < value.length; i++) {
            if ((int) gtype.getValue(i) == 1) {
                value[i] = 1;
            }
        }
        return value;
    }

    /**
     * This method defines the possible values we can have for the current P-type
     */

    public void initializeValues() {
        possibleValues = new double[][]{
                {0}, {1}};
    }

    /**
     * Getters
     */

    public AbstractGType getGType() {
        return gtype;
    }
    public double[][] getPossibleValues() {
        return possibleValues;
    }

    /**
     * To String
     * @return Stringification of the instance
     */

    public String toString() {
        return "";
    }

    /**
     * Calculates the bit set length
     * @param values
     * @return
     */

    public int calculateBitSetLength(int values) {
        return values * internalLength;
    }
}
