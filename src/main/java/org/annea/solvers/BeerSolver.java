package org.annea.solvers;

import org.annea.beer.BeerWorld;
import org.annea.ann.CTRNetwork;
import org.annea.ea.EA;
import org.annea.ea.fitness.BeerFitness;
import org.annea.gui.observers.GraphHelper;
import org.annea.tools.settings.AbstractSettings;

public class BeerSolver extends AbstractSolver {

    // Various references
    private BeerWorld beerWorld;

    /**
     * Constructor
     * @param s Instance of settings
     */

    public BeerSolver(AbstractSettings s) {
        // Call the super constructor
        super(s);

        // Create a new flatland
        beerWorld = new BeerWorld(s);

        // Create the flatland world
        beerWorld.initialize();

        // Create a new neural network
        ann = new CTRNetwork(settings);
        ann.create();

        // Set various things for the Flatland fitness function
        BeerFitness.setBeerWorld(beerWorld);
        BeerFitness.setNetwork(ann);
    }

    /**
     * Solve the actual problem
     */

    @Override
    public void solve(GraphHelper observer) {
        // Initialize the EA child pool
        evoAlg.initialize(settings.getMaxChildren());

        for (int i = 0; i < settings.getMaxGenerations(); i++) {
            System.out.println("========= Generation #" + (i + 1) + " =========");

            // Generate a new beerworld
            beerWorld.generate();

            // Calculate fitness
            calculateFitness(evoAlg);

            // Do adult selection
            evoAlg.adultSelection();

            // Do parent selection
            evoAlg.parentSelection();

            // Calculate fitness
            calculateFitness(evoAlg);

            // Print stats
            evoAlg.getStats();

            if(observer != null) {
                // Get the 0: max and 1:avg from evoAlg
                double[] stats = evoAlg.getStatsValues();

                // Broadcast the stats
                observer.fireLog(i, stats[0], stats[1]);
            }
        }

        // Store the best individual
        bestIndividual = evoAlg.getBest();
    }

    /**
     * Derp
     * @param evoAlg
     */

    @Override
    public void calculateFitness(EA evoAlg) {
        BeerFitness.test(evoAlg);
    }

    public BeerWorld getBeerWorld() {
        return beerWorld;
    }


}
