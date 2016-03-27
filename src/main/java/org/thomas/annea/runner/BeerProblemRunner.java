package org.thomas.annea.runner;

import org.thomas.annea.beer.BeerObject;
import org.thomas.annea.beer.Tracker;
import org.thomas.annea.tools.settings.AbstractSettings;

import java.util.List;

public class BeerProblemRunner extends AbstractProblemRunner {

    // Local stuff
    private List<BeerObject> objects;
    private BeerObject currentObject;
    private int currentObjectIndex;
    private Tracker tracker;

    // Stats
    private int capture;
    private int avoidance;
    private int fail;

    /**
     * Constructor
     * @param s Instance of Settings
     */

    public BeerProblemRunner(AbstractSettings s) {
        super(s);

        // Set the current object to null and the index to 0
        currentObject = null;
        currentObjectIndex = 0;

        // Set stats to 0
        capture = 0;
        avoidance = 0;
        fail = 0;
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

        // Check if the current object is outside the world
        if (currentObject != null) {
            // We already have a current object, lower the object by one
            currentObject.fall();

            // Check if we have a object that is outside the world
            if (currentObject.getRow() < 0) {
                currentObject = null;
            }
        }

        // Check if we need to get the next object
        if (currentObject == null) {
            // No current object, get the next one
            currentObject = objects.get(currentObjectIndex);

            // Increase the current object index
            currentObjectIndex++;
        }

        return true;
    }

    /**
     *
     * @return
     */

    public BeerObject getCurrentObject() {
        return currentObject;
    }

    public Tracker getTracker() {
        return tracker;
    }

    /**
     *
     * @return
     */

    public int getCapture() {
        return capture;
    }

    public int getAvoidance() {
        return avoidance;
    }

    public int getFail() {
        return fail;
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
