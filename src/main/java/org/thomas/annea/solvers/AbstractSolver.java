package org.thomas.annea.solvers;

import org.thomas.annea.ann.Network;
import org.thomas.annea.ea.EA;
import org.thomas.annea.ea.fitness.FlatlandFitness;
import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.tools.settings.AbstractSettings;

public abstract class AbstractSolver {

    // Reference to the settings
    protected AbstractSettings settings;

    // The network
    protected Network ann;

    // Keep track of the best individual
    protected AbstractGType bestIndividual;

    // EA
    protected EA evoAlg;

    /**
     * Constructor
     * @param s Instance of Settings
     */

    public AbstractSolver(AbstractSettings s) {
        // Store reference to the settings
        settings = s;

        // New instance of the EA class
        evoAlg = new EA(settings);
    }

    /**
     * Solve the actual problem
     */

    public void solve() {
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

    public Network getNetwork() {
        return ann;
    }

    public AbstractGType getBestIndividual() {
        return bestIndividual;
    }
}
