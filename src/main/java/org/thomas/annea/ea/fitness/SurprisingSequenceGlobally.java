package org.thomas.annea.ea.fitness;

import org.thomas.annea.ea.EA;
import org.thomas.annea.ea.gtype.AbstractGType;

import java.util.ArrayList;
import java.util.HashSet;

public class SurprisingSequenceGlobally extends AbstractFitness {

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
        for (int distance = 1; distance < values; distance++) {
            for (int i = 0; i < values; i++) {
                if ((i + distance) < values) {
                    String symbol = (int) valueVector[i] + "-" + distance + "-" +  (int) valueVector[i + distance];

                    if (!occurences.contains(symbol)) {
                        occurences.add(symbol);
                    }
                    else {
                        violations++;
                    }
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

    public static double maxFitness(AbstractGType individual) {
        return ((individual.getSettings().getNumberOfValues() * (individual.getSettings().getNumberOfValues() - 1)) / 2);
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