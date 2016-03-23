package org.thomas.project3.ea.fitness;

import org.thomas.project3.ann.Network;
import org.thomas.project3.ea.EA;
import org.thomas.project3.ea.gtype.AbstractGType;
import org.thomas.project3.flatland.Flatland;
import org.thomas.project3.flatland.Scenario;
import org.thomas.project3.runner.FlatlandProblemRunner;

import java.util.ArrayList;

public class FlatlandFitness extends AbstractFitness {

    // Stuff used for the flatland problem
    private static Flatland flatland;
    private static Network ann;

    /**
     * Setters for the Flatland problem
     */

    public static void setFlatland(Flatland f) {
        flatland = f;
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

        // Keep track of all food and all poison stats
        int food = 0;
        int poison = 0;
        int totalFood = 0;

        // Loop all the scenarios
        for (int i = 0; i < flatland.getScenariosToRun(); i++) {
            Scenario scenario;

            // Check if we should run dynamic or static
            if (flatland.getSettings().getSetting("scenario_mode").equals("static")) {
                // Static, just fetch the scenario
                scenario = flatland.getScenario(i);
            }
            else {
                // Dynamic, create a new scenario
                flatland.newScenario();

                // Get the last scenario
                scenario = flatland.getScenario(flatland.getScenarios().size() - 1);
            }

            // New problem runner
            FlatlandProblemRunner runner = new FlatlandProblemRunner(flatland.getSettings());

            // Set various information
            runner.setNetwork(ann);
            runner.setGrid(scenario.getGridTwoDimensions());
            runner.setAgent(scenario.getAgent());

            // Run the entire scenario
            runner.runAll();

            // Accumulate the stats
            food += runner.getFood();
            poison += runner.getPoison();
            totalFood += runner.getTotalFood();
        }

        // Calculate the score
        double score = food / (double) totalFood;

        // If we found poison, cripple the fitness value
        if (poison > 0) {
            return score / (double) poison;
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
