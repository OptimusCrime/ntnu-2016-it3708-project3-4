package org.thomas.annea.gui.beer;

import javafx.scene.canvas.Canvas;
import org.thomas.annea.beer.AbstractBeerObject;

public class AbstractBeerGui {

    // Instance of the source object, which is a flatland object
    protected AbstractBeerObject source;

    // The canvas
    protected Canvas canvas;

    public AbstractBeerGui() {
        // TODO
    }

    /**
     * Setter for the source of the GUI, the object is draws
     * @param s Instance of the object
     */

    public void setSource(AbstractBeerObject s) {
        source = s;
    }

    /**
     * Getter for the source
     * @return Reference to the source
     */

    public AbstractBeerObject getSource() {
        return source;
    }
}
