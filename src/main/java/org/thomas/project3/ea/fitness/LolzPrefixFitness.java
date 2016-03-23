package org.thomas.project3.ea.fitness;

import org.thomas.project3.ea.EA;
import org.thomas.project3.ea.gtype.AbstractGType;

import java.util.ArrayList;

public class LolzPrefixFitness extends AbstractFitness {

    /**
     * Calculate fitness score for generation
     * @param gen Generation to test
     */

    public static void test(EA gen) {
        // Get the population
        ArrayList<AbstractGType> children = gen.getChildren();

        // Loop the population
        for (int i = 0; i < children.size(); i++) {
            double fitness = calculateFitness(children.get(i));

            // Set the fitness
            children.get(i).setFitness(fitness);
        }
    }

    /**
     * Calculate fitness for one individual
     * @param individual One G-Type to calculate fitness for
     * @return The fitness score
     */

    public static double calculateFitness(AbstractGType individual) {
        // Get the S-Value
        int sValue = Integer.valueOf(individual.getSettings().getSetting("Z"));

        // Get the representation for this thing...
        double[] valueVector = individual.getAsArray();

        // Store what number to check
        int prefixType = (int) valueVector[0];
        int prefixLength = 1;

        // Loop the representation and check how many of them have the same value as the first one
        for (int j = 1; j < valueVector.length; j++) {
            if ((int) valueVector[j] == prefixType) {
                prefixLength++;
            }
            else {
                break;
            }
        }

        // The best prefix value is capped by S
        if (prefixType == 0 && prefixLength > sValue) {
            prefixLength = sValue;
        }

        // Return the final prefix length
        return prefixLength;
    }

    /**
     * Calculate the maximum fitness for the current problem
     * @return Maximum fitness
     */

    public static double maxFitness(AbstractGType individual) {
        // Return the minimum value of the two
        return individual.getSettings().getNumberOfValues();
    }

    /**
     * Checks if a solution for the current problem has been found
     * @param gen Generation to test
     * @return True if a solution is found
     */

    public static boolean solved(EA gen) {
        int max = (int) maxFitness(gen.getChildren().get(0));
        for (int i = 0; i < gen.getChildren().size(); i++) {
            if (((int) gen.getChildren().get(i).getFitness()) == max) {
                return true;
            }
        }

        return false;
    }
}
