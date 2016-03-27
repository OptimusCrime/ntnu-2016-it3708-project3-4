package org.thomas.annea.beer;

import org.jblas.DoubleMatrix;
import org.thomas.annea.gui.beer.TrackerGui;

public class Tracker extends AbstractBeerObject {

    public static final int NONE = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    // Keep track of the location
    private int location;

    /**
     *
     * @param loc
     */

    public Tracker(int loc) {
        super(new TrackerGui());

        // Set the tracker location
        location = loc;

        // Set the gui source
        gui.setSource(this);
    }

    /**
     *
     * @param object
     * @return
     */

    public DoubleMatrix getInput(BeerObject object) {
        // Create matrix for the input values where all values are 0
        DoubleMatrix inputValues = DoubleMatrix.zeros(1, 5);

        // Get the current object position
        int[] currentObjectGrid = new int[object.getSize()];
        for (int i = 0; i < currentObjectGrid.length; i++) {
            currentObjectGrid[i] = object.getLocation() + i;
        }

        // Get the current tracker position
        int[] trackerGrid = new int[5];
        for (int i = 0; i < trackerGrid.length; i++) {
            trackerGrid[i] = location + i;
        }

        // Loop and check which tracker sensors detect anything
        for (int i = 0; i < trackerGrid.length; i++) {
            // Check if we found the current value in the tracker positions
            boolean found = false;
            for (int j = 0; j < currentObjectGrid.length; j++) {
                if (trackerGrid[i] == currentObjectGrid[j]) {
                    found = true;
                }
            }

            // Check if the value was not found
            if (!found) {
                inputValues.put(0, i, 1.0);
            }
        }

        // Return the input values
        return inputValues;
    }

    public void move(DoubleMatrix matrix) {
        // Find the direction to move in
        int direction = NONE;
        double treashold = 0.1;

        // Make sure to get over the treashold
        if (matrix.get(0, 0) < treashold &&  matrix.get(0, 1) < treashold) {
            return;
        }

        if (matrix.get(0, 0) > matrix.get(0, 1)) {
            direction = LEFT;
        }
        else {
            direction = RIGHT;
        }

        // TODO manitude
        // TODO wrap (?)

        // Check what direction to move in
        if (direction == LEFT) {
            // Move left
            location--;

            if (location < 0) {
                location = 0;
            }
        }
        else if (direction == RIGHT) {
            // Move right
            location++;

            if (location > 25) {
                location = 24;
            }
        }
    }

    /**
     * Derp
     * @return
     */

    public int getLocation() {
        return location;
    }
}
