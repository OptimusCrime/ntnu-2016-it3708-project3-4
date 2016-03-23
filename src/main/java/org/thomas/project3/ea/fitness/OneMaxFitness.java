package org.thomas.project3.ea.fitness;

import org.thomas.project3.ea.EA;
import org.thomas.project3.ea.gtype.AbstractGType;
import org.thomas.project3.tools.RandomHelper;

import java.util.ArrayList;

public class OneMaxFitness extends AbstractFitness {

    /**
     * Calculate fitness score for generation
     * @param gen Generation to test
     */

    public static void test(EA gen) {
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

    static int[] binaryAnswerVector;
    public static double calculateFitness(AbstractGType individual) {
        // Get the representation for this thing...
        double[] valueVector = individual.getAsArray();

        // Check if we need to calculate the answer vector
        if (binaryAnswerVector == null) {
            // Create the answer vector
            binaryAnswerVector = new int[valueVector.length];

            // Check if we should randomize the bit,
            if (individual.getSettings().getSetting("randombit") != null && individual.getSettings().getSetting("randombit").equals("yes")) {
                for (int i = 0; i < valueVector.length; i++) {
                    if (RandomHelper.randomInPercentage(50)) {
                        binaryAnswerVector[i] = 1;
                    }
                    else {
                        binaryAnswerVector[i] = 0;
                    }
                }
            }
            else {
                // All 1's!
                for (int i = 0; i < valueVector.length; i++) {
                    binaryAnswerVector[i] = 1;
                }
            }
        }

        // Store number of ones
        int score = 0;

        // Loop the representation and check how many of the numbers are ones
        for (int i = 0; i < valueVector.length; i++) {
            if ((int) valueVector[i] == binaryAnswerVector[i]) {
                score++;
            }
        }

        return score;
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
