package org.thomas.annea.runner;

import org.jblas.DoubleMatrix;
import org.thomas.annea.ann.functions.Argmax;
import org.thomas.annea.beer.BeerObject;
import org.thomas.annea.beer.BeerWorld;
import org.thomas.annea.beer.Tracker;
import org.thomas.annea.tools.settings.AbstractSettings;
import org.thomas.annea.tools.settings.BeerSettings;

import java.util.List;

public class BeerProblemRunner extends AbstractProblemRunner {

    // Local stuff
    private List<BeerObject> objects;
    private BeerObject currentObject;
    private int currentObjectIndex;
    private Tracker tracker;

    private boolean removeObjectNextTick;

    // Stats
    private boolean calculatedOptimal;

    private int optimalCorrect;
    private int correct;
    private int wrong;

    private int optimalCapture;
    private int optimalAvoidance;
    private int capture;
    private int avoidance;

    private int pulls;
    private int correctPulls;

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

        // We need to calculate optimal stuff
        calculatedOptimal = false;

        // Set stats to 0
        optimalCorrect = 0;
        correct = 0;
        wrong = 0;

        optimalCapture = 0;
        optimalAvoidance = 0;
        capture = 0;
        avoidance = 0;

        pulls = 0;
        correctPulls = 0;
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
        // Get settings
        BeerSettings beerSettings = (BeerSettings) settings;

        // Check if can run the step at all
        if (timestep == settings.getMaxTimesteps()) {
            // We are done running
            return false;
        }

        // Reset tracker color
        tracker.resetColor();

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

        boolean pullDown = false;
        if (beerSettings.getMode() == BeerWorld.PULL && shouldPull(output)) {
            // Pull down
            pullDown = true;
        }
        else {
            // Move the tracker
            tracker.move(output);
        }


        // Check if the current object is outside the world
        if (currentObject != null) {
            // We already have a current object, check if we should make it fall or pull all
            if (pullDown) {
                // Pull down
                currentObject.pull();

                // Increase pull down by one
                pulls++;
            }
            else {
                // Just fall one
                currentObject.fall();
            }
        }

        // Check if we should check for capture/avoidance/fail
        if (currentObject != null && currentObject.getRow() == 0) {
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


            // At the bottom of the grid, here we check for avoidance or fail
            if (hasCollision(currentObjectGrid, trackerGrid)) {
                if (fullCollision(currentObjectGrid, trackerGrid)) {
                    // Check if this object should have been captured or not
                    if (currentObject.getSize() <= 4) {
                        // Increase correct
                        correct++;

                        // Increase capture
                        capture++;

                        // Set the color
                        tracker.setColor("#3c763d");

                        // If we did a pull down, increase the number of correct pulldowns
                        if (pullDown) {
                            correctPulls++;
                        }
                    }
                    else {
                        // This is wrong
                        wrong++;

                        // Set the color
                        tracker.setColor("#a94442");
                    }
                }
                else {
                    // God damnit, neither
                    wrong++;

                    // Set the color
                    tracker.setColor("#a94442");
                }
            }
            else {
                // Check if this object should have been captured or not
                if (currentObject.getSize() <= 4) {
                    // We should have captured this object
                    wrong++;

                    // Set the color
                    tracker.setColor("#a94442");
                }
                else {
                    // Increase correct too
                    correct++;

                    // We correctly avoided the object
                    avoidance++;

                    // Set the color
                    tracker.setColor("#3c763d");
                }
            }

            // Set object to be removed next tick
            removeObjectNextTick = true;
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

    private boolean shouldPull(DoubleMatrix matrix) {
        double max = matrix.get(0, 0);
        int index = 0;

        for (int i = 1; i < 3; i++) {
            if (matrix.get(0, i) > max) {
                max = matrix.get(0, i);
                index = i;
            }
        }

        return index == 2;
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

    public int getCorrect() {
        return correct;
    }

    public int getWrong() {
        return wrong;
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

    private void calculateOptimal() {
        int currentTick = 0;
        int currentBeerObject = 0;

        // Loop until we are done
        while (currentTick <= 600) {
            // Get the current object
            BeerObject optimalCurrentObject = objects.get(currentBeerObject);

            // Check the size!
            if (optimalCurrentObject.getSize() <= 4) {
                // Increase number of potential captures
                optimalCapture++;

                // Increase the tick with 14
                currentTick += 14;
            }
            else {
                // We should avoid this object
                optimalAvoidance++;

                // Increase the tick with 15
                currentTick += 15;
            }

            // Recent study has shown that is smart to increase this variable!
            currentBeerObject++;
        }

        // Add avoidance and capture to optimal correct
        optimalCorrect = optimalAvoidance + optimalCapture;

        // Set calculate to true
        calculatedOptimal = true;
    }

    public int getOptimalCapture() {
        if (!calculatedOptimal) {
            calculateOptimal();
        }

        return optimalCapture;
    }

    public int getOptimalAvoidance() {
        if (!calculatedOptimal) {
            calculateOptimal();
        }

        return optimalAvoidance;
    }

    public int getOptimalCorrect() {
        if (!calculatedOptimal) {
            calculateOptimal();
        }

        return optimalCorrect;
    }

    public int getPulls() {
        return pulls;
    }

    public int getCorrectPulls() {
        return correctPulls;
    }
}
