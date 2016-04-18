package org.annea.ea.gtype;

import org.annea.ea.ptype.*;
import org.annea.tools.settings.AbstractSettings;

public abstract class AbstractGType implements Comparable {

    // The instance of the settings
    protected AbstractSettings settings;

    // The length of this instance
    protected int length;

    // The fitness value
    protected double fitness;

    // The P-type for this G-type
    protected AbstractPType ptype;

    // For debugging
    protected static int idNumber = 0;
    protected int id;

    /**
     * Constructor for the G-Type
     * @param values number of values this G-Type should contain
     */

    public AbstractGType(AbstractSettings s, int values) {
        // Store settings
        settings = s;

        // Store id
        id = idNumber;
        idNumber++;

        // Fitness is 0 initially
        fitness = 0;

        // Generate the correct P-Type
        if (settings.getPType().equals("ASCIIPType")) {
            ptype = new ASCIIPType(settings, this);
        }
        else if (settings.getPType().equals("BinaryPType")) {
            ptype = new BinaryPType(settings, this);
        }
        else if (settings.getPType().equals("Beer8BitPType")) {
            ptype = new Beer8BitPType(settings, this);
        }
        else {
            ptype = new FloatPType(settings, this);
        }

        // Set the length of this G-tye
        length = ptype.calculateBitSetLength(values);
    }

    /**
     * Initialize by fetching possible values from the P-Type and applying them to the G-Type
     */

    public abstract void initialize();

    /**
     * Apply crossover to this child
     */

    public abstract void applyCrossover(int[] crossovers, int idx, AbstractGType parent1, AbstractGType parent2);

    /**
     * Handling of mutation
     */

    public abstract void mutate();

    /**
     * Apply the value with provided vector from the start position
     * @param vector Int array with binary values
     * @param start The index to start applying the vector on
     */

    public abstract void setValue(double[] vector, int start);

    /**
     * Get value from the vector at a given index
     * @param index The index to fetch from
     * @return The value in the vecor
     */

    public abstract double getValue(int index);

    /**
     * Return the entire internal value as a array of doubles
     * @return The representation as a array of doubles
     */

    public abstract double[] getAsArray();

    /**
     * Return a random value from the possible values except the current value
     * @param currentValue The current value
     * @return The new value
     */

    public abstract double[] getRandomValue(double[] currentValue);

    /**
     * Various getters
     */

    public int getLength() {
        return length;
    }
    public double getFitness() {
        return fitness;
    }
    public AbstractPType getPType() {
        return ptype;
    }
    public AbstractSettings getSettings() {
        return settings;
    }

    /**
     * Various setters
     */

    public void setFitness(double value) {
        fitness = value;
    }

    /**
     * To String
     * @return The string
     */

    public String toString() {
        return "";
    }

    /**
     * For sorting
     */

    @Override
    public int compareTo(Object o) {
        AbstractGType compareObject = (AbstractGType) o;
        return Double.compare(compareObject.getFitness(), fitness);
    }
}
