package org.thomas.annea.beer;

import org.thomas.annea.gui.beer.AbstractBeerGui;

public abstract class AbstractBeerObject {

    // Instance of the GUI object
    protected AbstractBeerGui gui;

    /**
     * Constructor
     * @param g Instance of abstract GUI
     */

    public AbstractBeerObject(AbstractBeerGui g) {
        gui = g;
    }

    /**
     * Getter for the GUI
     * @return The corresponding GUI for this object
     */

    public AbstractBeerGui getGui() {
        return gui;
    }
}