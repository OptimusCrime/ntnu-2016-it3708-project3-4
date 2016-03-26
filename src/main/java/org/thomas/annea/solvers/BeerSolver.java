package org.thomas.annea.solvers;

import org.thomas.annea.ann.Network;
import org.thomas.annea.beer.BeerWorld;
import org.thomas.annea.ea.EA;
import org.thomas.annea.ea.fitness.BeerFitness;
import org.thomas.annea.tools.settings.AbstractSettings;

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
        ann = new Network(settings);
        ann.create();

        // Set various things for the Flatland fitness function
        BeerFitness.setBeerWorld(beerWorld);
        BeerFitness.setNetwork(ann);
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
