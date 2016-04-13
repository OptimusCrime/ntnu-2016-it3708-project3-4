package org.annea.runner;

import org.annea.ann.Network;
import org.annea.tools.settings.AbstractSettings;

public abstract class AbstractProblemRunner {

    // Reference to the settings
    protected AbstractSettings settings;

    // Keep track of the current timestep
    protected int timestep;

    // Reference to the network
    protected Network ann;

    /**
     * TODO
     * @param s
     */

    public AbstractProblemRunner(AbstractSettings s) {
        // Set reference to settings
        settings = s;

        // Set the current timestep to 0
        timestep = 0;
    }


    /**
     * Run the maximum number of timesteps
     */

    public abstract void runAll();

    /**
     * Run one step
     */

    public abstract boolean runStep();

    /**
     * Setter for Network
     * @param n Instance of Network
     */

    public void setNetwork(Network n) {
        ann = n;
    }

    public int getTimestep() {
        return timestep;
    }
}
