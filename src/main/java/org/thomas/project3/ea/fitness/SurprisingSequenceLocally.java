package org.thomas.project3.ea.fitness;

import org.thomas.project3.ea.EA;
import org.thomas.project3.ea.gtype.AbstractGType;

import java.util.ArrayList;
import java.util.HashSet;

public class SurprisingSequenceLocally {
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
        // Get the representation for this thing...
        double[] valueVector = individual.getPType().getPRepresentation();

        // Find the longest sequence without duplicates in them
        int violations = 0;
        int values = valueVector.length;
        HashSet<String > occurences = new HashSet<>();
        for (int i = 0; i < values; i++) {
            if ((i + 1) < values) {
                String symbol = (int) valueVector[i] + "-" + 1 + "-" +  (int) valueVector[i + 1];

                if (!occurences.contains(symbol)) {
                    occurences.add(symbol);
                }
                else {
                    violations++;
                }
            }
        }

        // Return the final score
        return maxFitness(individual) - violations;
    }

    /**
     * Calculate the maximum fitness for the current problem
     * @return Maximum fitness
     */

    public static int maxFitness(AbstractGType individual) {
        return ((individual.getSettings().getNumberOfValues() * (individual.getSettings().getNumberOfValues() - 1)) / 2);
    }

    /**
     * Checks if a solution for the current problem has been found
     * @param gen Generation to test
     * @return True if a solution is found
     */

    public static boolean solved(EA gen) {
        double max = (int) maxFitness(gen.getChildren().get(0));
        for (int i = 0; i < gen.getChildren().size(); i++) {
            if (((int) gen.getChildren().get(i).getFitness()) == max) {
                return true;
            }
        }

        return false;
    }
}
