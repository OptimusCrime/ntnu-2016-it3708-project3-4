package org.thomas.annea.beer;

public class BeerObject {

    private int size;
    private int location;
    private int row;

    public BeerObject(int s, int loc) {

        // Set the references
        size = s;
        location = loc;

        // Set row to initial value
        row = 14;
    }

    /**
     * Derp
     */

    public void fall() {
        row--;
    }

    public int getSize() {
        return size;
    }

    public int getLocation() {
        return location;
    }

    public int getRow() {
        return row;
    }
}
