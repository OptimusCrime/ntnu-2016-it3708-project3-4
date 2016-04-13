package org.annea.solvers;

import org.annea.ann.Network;
import org.annea.ea.EA;
import org.annea.ea.fitness.FlatlandFitness;
import org.annea.flatland.Flatland;
import org.annea.tools.settings.AbstractSettings;

public class FlatlandSolver extends AbstractSolver {

    // Various references
    private Flatland flatland;

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

        // Set various things for the Flatland fitness function
        FlatlandFitness.setFlatland(flatland);
        FlatlandFitness.setNetwork(ann);
    }

    /**
     * Derp
     * @param evoAlg
     */

    @Override
    public void calculateFitness(EA evoAlg) {
        FlatlandFitness.test(evoAlg);
    }


    public Flatland getFlatland() {
        return flatland;
    }
}
