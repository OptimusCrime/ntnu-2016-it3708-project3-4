package org.thomas.annea.runner;

import org.jblas.DoubleMatrix;
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

    private boolean removeObjectNextTick;

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

        // Indicates if a object should be removed next tick
        removeObjectNextTick = false;

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

        // Check if we need to get the next object
        if (removeObjectNextTick || currentObject == null) {
            // Next object should not be removed, reset flag
            removeObjectNextTick = false;

            // No current object, get the next one
            currentObject = objects.get(currentObjectIndex);

            // Increase the current object index
            currentObjectIndex++;
        }

        // Get the input from the agent
        DoubleMatrix inputSensors = tracker.getInput(currentObject);

        // Propagate the sensor values
        DoubleMatrix output = ann.propagate(inputSensors);

        // Move the tracker
        tracker.move(output);

        // Check if the current object is outside the world
        if (currentObject != null) {
            // We already have a current object, lower the object by one
            currentObject.fall();
        }

        // Check if we should check for capture/avoidance/fail
        if (currentObject.getRow() <= 1) {
            // Get the current object position
            int[] currentObjectGrid = new int[currentObject.getSize()];
            for (int i = 0; i < currentObjectGrid.length; i++) {
                currentObjectGrid[i] = currentObject.getLocation() + i;
            }

            // Get the current tracker position
            int[] trackerGrid = new int[5];
            for (int i = 0; i < trackerGrid.length; i++) {
                trackerGrid[i] = tracker.getLocation() + i;
            }

            // Handle different scenarios
            if (currentObject.getRow() == 1) {
                // Check if the objects are anywhere near each other
                if (hasCollision(currentObjectGrid, trackerGrid)) {
                    // Check if we have all the values present
                    if (fullCollision(currentObjectGrid, trackerGrid)) {
                        // Increase capture
                        capture++;

                        // Set object to be removed next tick
                        removeObjectNextTick = true;
                    }
                    else {
                        // This is then a fail
                        fail++;

                        // Set object to be removed next tick
                        removeObjectNextTick = true;
                    }
                }
            }
            else if (currentObject.getRow() == 0) {
                // At the bottom of the grid, here we check for avoidance or fail
                if (!hasCollision(currentObjectGrid, trackerGrid)) {
                    // No collision, this is a successful avoidance
                    // TODO check size etc
                    avoidance++;

                    // Set object to be removed next tick
                    removeObjectNextTick = true;
                }
                else {
                    // This is then a fail
                    fail++;

                    // Set object to be removed next tick
                    removeObjectNextTick = true;
                }
            }
        }

        // Tick was ran successfully
        return true;
    }

    private boolean fullCollision(int[] objectPos, int[] trackerPos) {
        for (int i = 0; i < objectPos.length; i++) {
            // Check if we found the current value in the tracker positions
            boolean found = false;
            for (int j = 0; j < trackerPos.length; j++) {
                if (objectPos[i] == trackerPos[j]) {
                    found = true;
                }
            }

            // Check if the value was not found
            if (!found) {
                return false;
            }
        }

        // If we got this far we have a direct hit
        return true;
    }

    private boolean hasCollision(int[] objectPos, int[] trackerPos) {
        for (int i = 0; i < objectPos.length; i++) {
            for (int j = 0; j < trackerPos.length; j++) {
                if (objectPos[i] == trackerPos[j]) {
                    return true;
                }
            }
        }

        return false;
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
