package org.thomas.annea.runner;

import org.thomas.annea.beer.BeerObject;
import org.thomas.annea.beer.Tracker;
import org.thomas.annea.tools.settings.AbstractSettings;

import java.util.List;

public class BeerProblemRunner extends AbstractProblemRunner {

    // Local stuff
    private List<BeerObject> objects;
    private Tracker tracker;

    /**
     * Constructor
     * @param s Instance of Settings
     */

    public BeerProblemRunner(AbstractSettings s) {
        super(s);

        // TODO
    }

    /**
     * Run the maximum number of timesteps
     */

    @Override
    public void runAll() {
        // Loop the total number of timesteps and call runStep each time
        for (int k = 0; k < settings.getMaxTimesteps(); k++) {
            // Rune one step
            runStep();
        }
    }

    /**
     * Run one step
     */

    @Override
    public boolean runStep() {
        // Check if can run the step at all
        if (timestep == settings.getMaxTimesteps()) {
            // We are done running
            return false;
        }

        // We are not done running, increase the current timestep
        timestep++;

        // TODO
        return true;
    }

    /**
     * Derp
     * @param objectList
     */

    public void setObjects(List<BeerObject> objectList) {
        objects = objectList;
    }

    /**
     * Derp
     * @param t
     */

    public void setTracker(Tracker t) {
        tracker = t;
    }
}
