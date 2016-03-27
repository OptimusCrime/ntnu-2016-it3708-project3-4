package org.thomas.annea.beer;

import org.thomas.annea.gui.beer.BeerObjectGui;

public class BeerObject extends AbstractBeerObject {

    private int size;
    private int location;
    private int row;

    public BeerObject(int s, int loc) {
        super(new BeerObjectGui());

        // Set the references
        size = s;
        location = loc;

        // Set row to initial value
        row = 14;

        // Set the gui source
        gui.setSource(this);
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
