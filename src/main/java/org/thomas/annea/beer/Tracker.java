package org.thomas.annea.beer;

import org.jblas.DoubleMatrix;

public class Tracker {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    // Keep track of the location
    private int location;

    // Reference for the beer object
    private BeerObject beerObject;

    /**
     *
     * @param loc
     */

    public Tracker(int loc) {
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
        // Create matrix for the input values where all values are 0
        DoubleMatrix inputValues = DoubleMatrix.zeros(1, 5);

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

        int steps =  (int) Math.ceil(matrix.get(0, 1) * 4);

        // Check what direction to move in
        if (direction == LEFT) {
            // Move left
            location -= steps;

            if (location < 0) {
                location += 29;
            }
        }
        else {
            // Move right
            location += steps;

            if (location > 29) {
                location -= 29;
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
