package org.thomas.annea.beer;

import org.thomas.annea.gui.beer.ObjectGui;

public class Object extends AbstractBeerObject {

    private int size;
    private int location;
    private int row;

    public Object() {
        super(new ObjectGui());

        // Set the gui source
        gui.setSource(this);
    }
}
