package org.thomas.annea.gui.beer;

import javafx.scene.canvas.Canvas;
import org.thomas.annea.beer.AbstractBeerObject;

public class AbstractBeerGui {
    // Size
    public final int OBJECTSIZE = 40;

    // Instance of the source object, which is a flatland object
    protected AbstractBeerObject source;

    // The canvas
    protected Canvas canvas;

    // Keep track of the initial drawing
    protected boolean drawn;

    /**
     * Derp
     */

    public AbstractBeerGui() {
        // Create the canvas
        canvas = new Canvas();

        // Set drawn to false
        drawn = false;
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

    /**
     * Method for drawing
     * @return The canvas on which the drawing is done on
     */

    public Canvas draw() {
        return canvas;
    }
}
