package org.thomas.project3.ea.gtype;

import org.thomas.project3.tools.RandomHelper;
import org.thomas.project3.tools.Settings;

import java.util.BitSet;

public class BinaryGType extends AbstractGType {

    // The value
    protected BitSet value;

    /**
     * Constructor for the G-Type
     * @param values number of values this G-Type should contain
     */

    public BinaryGType(Settings s, int values) {
        super(s, values);

        // Initialize the (empty) BitSet
        value = new BitSet(length);

        // Initialize and set a random value
        initialize();
    }

    /**
     * Initialize the G-type by setting a random value to it
     */

    @Override
    public void initialize() {
        // Get all the possible values from the P-Type
        double[][] possibleValues = ptype.getPossibleValues();

        // Find out how many values we need to initialize
        for (int i = 0; i < ptype.calculateBitSetLength(settings.getNumberOfValues()); i++) {
            // Get a random possible value
            double[] randomValue = possibleValues[RandomHelper.randint(0, possibleValues.length)];

            // Calculate the offset
            int offset = ptype.calculateBitSetLength(settings.getNumberOfValues()) / settings.getNumberOfValues();
            setValue(randomValue, i * offset);
        }
    }

    /**
     * Handling of mutation
     */

    @Override
    public void mutate() {
        // Get the number of values
        int valueToChange = RandomHelper.randint(0, settings.getNumberOfValues());

        int bitlength = ptype.calculateBitSetLength(settings.getNumberOfValues()) / settings.getNumberOfValues();
        double[] currentValue = new double[bitlength];

        for (int i = 0; i < currentValue.length; i++) {
            currentValue[i] = getValue((valueToChange * currentValue.length) + i);
        }

        // Get a new value
        double[] newValue = getRandomValue(currentValue);

        // Apply the new value
        setValue(newValue, bitlength * valueToChange);
    }

    /**
     * Apply the value with provided vector from the start position
     * @param vector Int array with binary values
     * @param start The index to start applying the vector on
     */

    @Override
    public void setValue(double[] vector, int start) {
        // Loop the vector from start to finish
        for (int i = 0; i < vector.length; i++) {
            if ((int) vector[i] == 0) {
                value.clear(start + i);
            }
            else {
                value.set(start + i);
            }
        }
    }

    /**
     * Apply crossover to this child
     */

    @Override
    public void applyCrossover(int[] crossovers, int idx, AbstractGType parent1, AbstractGType parent2) {
        // First reset the current BitSet
        value.clear();

        // Find out which parent to start fetching from
        AbstractGType currentParent;
        if (idx == 0) {
            currentParent = parent1;
        }
        else {
            currentParent = parent2;
        }

        double[] crossoverResult = new double[ptype.calculateBitSetLength(settings.getNumberOfValues())];
        int singleValueLength = ptype.calculateBitSetLength(settings.getNumberOfValues() / settings.getNumberOfValues());

        for (int i = 1; i < crossovers.length; i++) {
            // Loop the P-Type length of the crossover
            for (int j = crossovers[i - 1]; j <= crossovers[i]; j++) {
                // Loop the actual bit length of each individual P-Type length
                for (int k = 0; k < singleValueLength; k++) {
                    crossoverResult[(j * singleValueLength) + k] = currentParent.getValue((j * singleValueLength) + k);
                }
            }

            if (currentParent == parent1) {
                currentParent = parent2;
            }
            else {
                currentParent = parent1;
            }
        }

        // Set the value for this G-Type
        setValue(crossoverResult, 0);
    }

    /**
     * Return a random value from the possible values except the current value
     * @param currentValue The current value
     * @return The new value
     */

    @Override
    public double[] getRandomValue(double[] currentValue) {
        int bitLength = ptype.calculateBitSetLength(settings.getNumberOfValues()) / settings.getNumberOfValues();
        int numberOfFoundValues = 0;
        double[][] possibleValues = ptype.getPossibleValues();
        double[][] filteredValues = new double[possibleValues.length - 1][bitLength];

        for (int i = 0; i < possibleValues.length; i++) {
            int duplicate = 0;
            for (int j = 0; j < bitLength; j++) {
                if ((int) possibleValues[i][j] == (int) currentValue[j]) {
                    duplicate++;
                }
            }

            if (duplicate != bitLength) {
                filteredValues[numberOfFoundValues] = possibleValues[i];
                numberOfFoundValues++;
            }
        }

        if (filteredValues.length == 1) {
            return filteredValues[0];
        }
        else {
            return filteredValues[RandomHelper.randint(0, filteredValues.length)];
        }
    }

    /**
     * Generic toString
     * @return The string
     */

    @Override
    public String toString() {
        // Build G-Type representation
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            if (value.get(i)) {
                builder.append("1");
            }
            else {
                builder.append("0");
            }
        }

        // Get the G-Type representation
        String GTypeString = "G = [" + builder.toString() + "], ";

        // Get the F-Type representation
        String PTypeString = "P = " + ptype.toString() + ", ";

        return "#" + id + " - " + GTypeString + PTypeString + "F = " + fitness;
    }

    /**
     * Get value from the vector at a given index
     * @param index The index to fetch from
     * @return The value in the vecor
     */

    @Override
    public double getValue(int index) {
        if (value.get(index)) {
            return 1;
        }
        return 0;
    }

    /**
     * Return the entire internal value as a array of doubles
     * @return The representation as a array of doubles
     */

    @Override
    public double[] getAsArray() {
        double[] binaryArray = new double[ptype.calculateBitSetLength(settings.getNumberOfValues())];
        for (int i = 0; i < binaryArray.length; i++) {
            if (value.get(i)) {
                binaryArray[i] = 1;
            }
        }
        return binaryArray;
    }
}
