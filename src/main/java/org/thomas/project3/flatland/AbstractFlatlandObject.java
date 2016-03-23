package org.thomas.project3.flatland;

import org.thomas.project3.gui.AbstractGui;

public abstract class AbstractFlatlandObject {

    // Instance of the GUI object
    protected AbstractGui gui;

    /**
     * Constructor
     * @param g Instance of abstract GUI
     */

    public AbstractFlatlandObject(AbstractGui g) {
        gui = g;
    }

    /**
     * Getter for the GUI
     * @return The corresponding GUI for this object
     */

    public AbstractGui getGui() {
        return gui;
    }
}
