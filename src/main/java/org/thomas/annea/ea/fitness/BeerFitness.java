package org.thomas.annea.ea.fitness;

import org.thomas.annea.ann.Network;
import org.thomas.annea.beer.BeerWorld;
import org.thomas.annea.ea.EA;
import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.runner.BeerProblemRunner;
import org.thomas.annea.tools.settings.BeerSettings;

import java.util.ArrayList;
import java.util.List;

public class BeerFitness extends AbstractFitness {

    // Stuff used for the flatland problem
    private static BeerWorld beerWorld;
    private static Network ann;
    private static int maxCorrectPulls = 0;
    private static List<Integer> numberOfPulls = new ArrayList<>();

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
        // Reset the correct pull
        maxCorrectPulls = 0;

        ArrayList<AbstractGType> children = gen.getChildren();

        // Check if we should calculate max captures
        BeerSettings beerSettings = (BeerSettings) beerWorld.getSettings();
        if (beerSettings.getMode() == BeerWorld.PULL) {
            // Reset the list of pull downs
            numberOfPulls.clear();

            // Pull special case stuff
            for (int i = 0; i < children.size(); i++) {
                double fitness = calculateFitness(children.get(i));

                // Set the fitness
                children.get(i).setActualFitness(fitness);
            }

            for (int i = 0; i < children.size(); i++) {
                double newFitness = children.get(i).getActualFitness() * (numberOfPulls.get(i) / (double) maxCorrectPulls);
                children.get(i).setFitness(newFitness);
            }
        }
        else {
            // Just do what we usually does
            for (int i = 0; i < children.size(); i++) {
                double fitness = calculateFitness(children.get(i));

                // Set the fitness
                children.get(i).setFitness(fitness);
            }
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
            double capture = runner.getCapture() * 0.5;
            double avoid = runner.getAvoidance() * 0.5;

            // Add number of pull downs
            numberOfPulls.add(runner.getCapture());

            if (runner.getCapture() > maxCorrectPulls) {
                maxCorrectPulls = runner.getCapture();
            }

            return capture + avoid;
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
