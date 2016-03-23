package org.thomas.annea.ea.gtype;

import org.thomas.annea.tools.RandomHelper;
import org.thomas.annea.tools.Settings;

import java.util.Random;

public class FloatGType extends AbstractGType {

    // The value
    protected double[] value;

    // The actual length of the geno vector
    private static int actualLength = 0;

    /**
     * Constructor for the G-Type
     * @param values number of values this G-Type should contain
     */

    public FloatGType(Settings s, int values) {
        super(s, values);

        // See if we need to calculate the length of the vector here
        if (actualLength == 0) {
            calculateVectorLength();
        }

        // Initialize the (empty) BitSet
        value = new double[actualLength];

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
            tempLength += networkDimensions[i - 1] * networkDimensions[i];
        }

        // Set the actual length
        actualLength = tempLength;
    }

    /**
     * Initialize the G-type by setting a random value to it
     */

    @Override
    public void initialize() {
        // Find the range for the randomizer
        int randomRange = 1;
        if (settings.getSetting("pheno_range") != null) {
            try {
                randomRange = Integer.parseInt(settings.getSetting("pheno_range"));
            }
            catch (Exception e) {

            }
        }

        Random r = new Random();
        for (int i = 0; i < value.length; i++) {
            value[i] = -randomRange + (r.nextDouble() * (2 * randomRange));
        }
    }

    /**
     * Handling of mutation
     */

    @Override
    public void mutate() {
        // Get the number of values
        int valueToChange = RandomHelper.randint(0, value.length);

        // Find the range for the randomizer
        int randomRange = 1;
        if (settings.getSetting("pheno_range") != null) {
            try {
                randomRange = Integer.parseInt(settings.getSetting("pheno_range"));
            }
            catch (Exception e) {

            }
        }

        // Get a new value
        Random r = new Random();
        double[] newValue = new double[]{-randomRange + (r.nextDouble() * (2 * randomRange))};

        // Apply the new value
        setValue(newValue, valueToChange);
    }

    /**
     * Apply the value with provided vector from the start position
     * @param vector Int array with binary values
     * @param start The index to start applying the vector on
     */

    @Override
    public void setValue(double[] vector, int start) {
        for (int i = 0; i < vector.length; i++) {
            value[start + i] = vector[i];
        }
    }

    /**
     * Apply crossover to this child
     */

    @Override
    public void applyCrossover(int[] crossovers, int idx, AbstractGType parent1, AbstractGType parent2) {
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
        System.out.println("This should never be called");
        return new double[]{0};
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
            builder.append(value[i]);

            if ((i + 1) != length) {
                builder.append(" ");
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
        return value[index];
    }

    /**
     * Return the entire internal value as a array of doubles
     * @return The representation as a array of doubles
     */

    @Override
    public double[] getAsArray() {
        return value;
    }
}
