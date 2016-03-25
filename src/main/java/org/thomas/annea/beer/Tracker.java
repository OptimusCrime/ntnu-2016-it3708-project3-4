package org.thomas.annea.beer;

import org.thomas.annea.gui.beer.TrackerGui;

public class Tracker extends AbstractBeerObject {

    private int location;

    public Tracker() {
        super(new TrackerGui());

        // Set the gui source
        gui.setSource(this);
    }
}
