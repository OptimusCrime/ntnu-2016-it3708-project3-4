package org.thomas.annea.solvers;

import org.thomas.annea.ann.Network;
import org.thomas.annea.ea.EA;
import org.thomas.annea.ea.fitness.FlatlandFitness;
import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.flatland.Flatland;

import org.thomas.annea.tools.settings.AbstractSettings;

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

    public FlatlandSolver(AbstractSettings s) {
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
