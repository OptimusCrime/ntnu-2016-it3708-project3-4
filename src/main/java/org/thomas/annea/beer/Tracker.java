package org.thomas.annea.beer;

import org.jblas.DoubleMatrix;
import org.thomas.annea.tools.settings.BeerSettings;

public class Tracker {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    // Reference to beerworld
    private BeerWorld beerWorld;

    // Keep track of the location
    private int location;

    // Reference for the beer object
    private BeerObject beerObject;

    /**
     * @param bw Instance of beerWorld
     * @param loc
     */

    public Tracker(BeerWorld bw, int loc) {
        // Reference to beerWorld
        beerWorld = bw;

        // Set the tracker location
        location = loc;
    }

    /**
     * Derp
     * @return
     */

    public int[] getTrackerLocation () {
        // Get the current tracker position
        int[] grid = new int[5];
        int overflow = 0;
        for (int i = 0; i < grid.length; i++) {
            if ((location + i) > 29) {
                grid[i] = overflow;
                overflow++;
            }
            else {
                grid[i] = location + i;
            }
        }

        return grid;
    }


    /**
     *
     * @param object
     * @return
     */

    public DoubleMatrix getInput(BeerObject object) {
        // Get beer settings
        BeerSettings beerSettings = (BeerSettings) beerWorld.getSettings();

        int inputSize = 5;
        if (beerSettings.getMode() == BeerWorld.NOWRAP) {
            inputSize = 7;
        }

        // Create matrix for the input values where all values are 0
        DoubleMatrix inputValues = DoubleMatrix.zeros(1, inputSize);

        // Get the current object position
        int[] currentObjectGrid = new int[object.getSize()];
        for (int i = 0; i < currentObjectGrid.length; i++) {
            currentObjectGrid[i] = object.getLocation() + i;
        }

        // Get the current tracker location
        int[] trackerGrid = getTrackerLocation();

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
            if (found) {
                inputValues.put(0, i, 1.0);
            }
        }

        // Check if we should add blocked to input matrix
        if (beerSettings.getMode() == BeerWorld.NOWRAP) {
            if (location == 0) {
                inputValues.put(0, 5, 1);
                //double locationLeftDegree = (location + 1) / 15;
                //inputValues.put(0, 5, locationLeftDegree);
            }
            else {
                inputValues.put(0, 5, 1);
            }
        }

        // Check if we should add blocked to input matrix
        if (beerSettings.getMode() == BeerWorld.NOWRAP) {
            if (location == 25) {
                inputValues.put(0, 6, 1);
                //double locationLRightDegree = (location + 6 - 14) / 15;
                //inputValues.put(0, 5, locationLRightDegree);
            }
            else {
                //inputValues.put(0, 5, 1);
            }
        }

        // Return the input values
        return inputValues;
    }

    /**
     *
     * @param matrix
     */

    public void move(DoubleMatrix matrix) {
        // Find the direction to move in
        int direction;
        if (matrix.get(0, 0) <= 0.5) {
            direction = LEFT;
        }
        else {
            direction = RIGHT;
        }

        // Get beer settings
        BeerSettings beerSettings = (BeerSettings) beerWorld.getSettings();

        // Get number of steps
        int steps = (int) Math.ceil(matrix.get(0, 1) * 4);

        // Apply wrap and stuff
        if (direction == LEFT) {
            // Move left
            location -= steps;

            if (location < 0) {
                // Check if we should apply wrap or not
                if (beerSettings.getMode() == BeerWorld.NOWRAP) {
                    // Set location to just 0
                    location = 0;
                }
                else {
                    // Apply wrap
                    location += 29;
                }

            }
        }
        else {
            // Move right
            location += steps;

            // Check if we should apply wrap or not
            if (beerSettings.getMode() == BeerWorld.NOWRAP) {
                // Check if location violates nowrap
                if (location >= 25) {
                    location = 25;
                }
            }
            else {
                // Make it possible to wrap
                if (location > 29) {
                    location -= 29;
                }
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

    /**
     *
     * @param bo
     */

    public void setTrackerReference(BeerObject bo) {
        beerObject = bo;
    }

    public BeerObject getBeerObjectReference() {
        return beerObject;
    }
}
