package org.thomas.project3.solvers;

import org.thomas.project3.ann.Network;
import org.thomas.project3.ea.EA;
import org.thomas.project3.ea.fitness.FlatlandFitness;
import org.thomas.project3.ea.gtype.AbstractGType;
import org.thomas.project3.flatland.Agent;
import org.thomas.project3.flatland.Flatland;
import org.thomas.project3.tools.RandomHelper;

import javafx.scene.paint.Color;
import org.thomas.project3.tools.Settings;

public class FlatlandSolver extends AbstractSolver {

    // Various references
    private Flatland flatland;
    private Network ann;

    // Keep track of the best individual
    private AbstractGType bestIndividual;

    /**
     * Constructor
     * @param s Instance of settings
     */

    public FlatlandSolver(Settings s) {
        // Call the super constructor
        super(s);

        // Create a new flatland
        flatland = new Flatland(s);

        // Create the flatland world
        flatland.initialize();

        // Create a new neural network
        ann = new Network(settings);
        ann.create();
    }

    /**
     * Solve the actual problem
     */

    @Override
    public void solve() {
        // New instance of the EA class
        EA evoAlg = new EA(settings);

        // Set various things for the Flatland fitness function
        FlatlandFitness.setFlatland(flatland);
        FlatlandFitness.setNetwork(ann);

        // Initialize the EA child pool
        evoAlg.initialize(settings.getMaxChildren());

        for (int i = 0; i < settings.getMaxGenerations(); i++) {
            System.out.println("========= Generation #" + (i + 1) + " =========");

            // Calculate fitness
            FlatlandFitness.test(evoAlg);

            // Do adult selection
            evoAlg.adultSelection();

            // Do parent selection
            evoAlg.parentSelection();

            // Calculate fitness
            FlatlandFitness.test(evoAlg);

            // Stats n shit
            evoAlg.getStats();
        }

        // Store the best individual
        bestIndividual = evoAlg.getBest();
    }

    public Flatland getFlatland() {
        return flatland;
    }

    public Network getNetwork() {
        return ann;
    }

    public AbstractGType getBestIndividual() {
        return bestIndividual;
    }
}
