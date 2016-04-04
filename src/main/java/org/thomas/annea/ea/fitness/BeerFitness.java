package org.thomas.annea.ea.fitness;

import org.thomas.annea.ann.Network;
import org.thomas.annea.beer.BeerWorld;
import org.thomas.annea.ea.EA;
import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.runner.BeerProblemRunner;

import java.util.ArrayList;

public class BeerFitness extends AbstractFitness {

    // Stuff used for the flatland problem
    private static BeerWorld beerWorld;
    private static Network ann;

    /**
     * Setters for the Flatland problem
     */

    public static void setBeerWorld(BeerWorld bw) {
        beerWorld = bw;
    }
    public static void setNetwork(Network a) {
        ann = a;
    }

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

    public static double calculateFitness(AbstractGType individual) {
        // Set the weights for this individual
        ann.setWeights(individual);

        // New problem runner
        BeerProblemRunner runner = new BeerProblemRunner(beerWorld.getSettings());

        // Set various information
        runner.setNetwork(ann);
        runner.setObjects(beerWorld.getObjects());
        runner.setTracker(beerWorld.getTracker());

        // Run the entire scenario
        runner.runAll();

        // Calculate the score
        int fail = runner.getFail();
        double score = 0;
        if (runner.getOptimalCapture() > 0) {
            score += runner.getCapture() / (double) runner.getOptimalCapture();
        }
        if (runner.getOptimalAvoidance() > 0) {
            //score += runner.getAvoidance() / (double) runner.getOptimalAvoidance();
        }

        // If we found poison, cripple the fitness value
        if (fail > 0) {
            return score / (double) (fail + 1);
        }

        // Return the final fitness score
        return score;
    }

    /**
     * Calculate the maximum fitness for the current problem
     * @return Maximum fitness
     */

    public static double maxFitness(AbstractGType individual) {
        return 0;
    }

    /**
     * Checks if a solution for the current problem has been found
     * @param gen Generation to test
     * @return True if a solution is found
     */

    public static boolean solved(EA gen) {
        return false;
    }
}
