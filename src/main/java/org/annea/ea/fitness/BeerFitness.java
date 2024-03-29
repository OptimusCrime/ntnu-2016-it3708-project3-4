package org.annea.ea.fitness;

import org.annea.ann.Network;
import org.annea.beer.BeerWorld;
import org.annea.ea.EA;
import org.annea.ea.gtype.AbstractGType;
import org.annea.runner.BeerProblemRunner;
import org.annea.tools.settings.BeerSettings;

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

        // Get BeerSettings
        BeerSettings beerSetting = (BeerSettings) beerWorld.getSettings();

        // Check what fitness to apply
        if (beerSetting.getMode() == BeerWorld.STANDARD) {
            // Standard fitness
            double capture = (runner.getCapture() / (double) runner.getOptimalCapture()) * 0.5;
            double avoid = (runner.getAvoidance() / (double) runner.getOptimalAvoidance()) * 0.25;
            double correct = (runner.getCorrect() / (double) runner.getOptimalCorrect()) * 0.25;
            return capture + avoid + correct;
        }
        else if (beerSetting.getMode() == BeerWorld.NOWRAP) {
            // No-wrap fitness
            double capture = (runner.getCapture() / (double) runner.getOptimalCapture()) * 0.75;
            double avoid = (runner.getAvoidance() / (double) runner.getOptimalAvoidance()) * 0.25;
            return capture + avoid;
        }
        else {
            // Pull fitness
            double nominator = Math.max(Math.pow(runner.getCorrect(), 2) - Math.pow(runner.getWrongPulls(), 2.01), 0) * (runner.getCorrect() - runner.getWrong());
            return nominator / (Math.pow(runner.getCorrect(), 2) * runner.getCorrect());
        }
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
