package org.thomas.annea.gui.beer;

import javafx.scene.canvas.Canvas;
import org.thomas.annea.beer.AbstractBeerObject;

public abstract class AbstractBeerGui {
    // Size
    public final int OBJECTSIZE = 40;

    // Instance of the source object, which is a flatland object
    protected AbstractBeerObject source;

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
     * @param c The canvas to draw on
     * @return The canvas on which the drawing is done on
     */

    public abstract void draw(Canvas c);
}
