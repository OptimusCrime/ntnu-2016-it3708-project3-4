package org.thomas.annea.solvers;

import org.thomas.annea.ann.Network;
import org.thomas.annea.ea.fitness.FlatlandFitness;
import org.thomas.annea.flatland.Flatland;

import org.thomas.annea.tools.settings.AbstractSettings;

public class BeerSolver extends AbstractSolver {

    // Various references
    private Flatland flatland;

    /**
     * Constructor
     * @param s Instance of settings
     */

    public BeerSolver(AbstractSettings s) {
        // Call the super constructor
        super(s);

        // Create a new flatland
        flatland = new Flatland(s);

        // Create the flatland world
        flatland.initialize();

        // Create a new neural network
        ann = new Network(settings);
        ann.create();

        // Set various things for the Flatland fitness function
        FlatlandFitness.setFlatland(flatland);
        FlatlandFitness.setNetwork(ann);
    }

    public Flatland getFlatland() {
        return flatland;
    }
}
