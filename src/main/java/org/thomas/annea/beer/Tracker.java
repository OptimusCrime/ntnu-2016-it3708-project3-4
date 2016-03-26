package org.thomas.annea.beer;

import org.thomas.annea.gui.beer.TrackerGui;

public class Tracker extends AbstractBeerObject {

    private int location;

    public Tracker(int loc) {
        super(new TrackerGui());

        // Set the tracker location
        location = loc;

        // Set the gui source
        gui.setSource(this);
    }
}
