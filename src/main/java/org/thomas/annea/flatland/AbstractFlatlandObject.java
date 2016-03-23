package org.thomas.annea.flatland;


import org.thomas.annea.gui.flatland.AbstractFlatlandGui;

public abstract class AbstractFlatlandObject {

    // Instance of the GUI object
    protected AbstractFlatlandGui gui;

    /**
     * Constructor
     * @param g Instance of abstract GUI
     */

    public AbstractFlatlandObject(AbstractFlatlandGui g) {
        gui = g;
    }

    /**
     * Getter for the GUI
     * @return The corresponding GUI for this object
     */

    public AbstractFlatlandGui getGui() {
        return gui;
    }
}
