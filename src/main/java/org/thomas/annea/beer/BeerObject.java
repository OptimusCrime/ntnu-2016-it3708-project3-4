package org.thomas.annea.beer;

import org.thomas.annea.gui.beer.ObjectGui;

public class BeerObject extends AbstractBeerObject {

    private int size;
    private int location;
    private int row;

    public BeerObject(int s, int loc) {
        super(new ObjectGui());

        // Set the references
        size = s;
        location = loc;

        // Set row to initial value
        row = 14;

        // Set the gui source
        gui.setSource(this);
    }
}