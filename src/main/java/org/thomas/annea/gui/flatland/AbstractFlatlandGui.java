package org.thomas.annea.gui.flatland;

import org.thomas.annea.flatland.AbstractFlatlandObject;

import javafx.scene.canvas.Canvas;

public abstract class AbstractFlatlandGui {

    // The size of each GUI object
    public static int SIZE = 75;

    // Instance of the source object, which is a flatland object
    protected AbstractFlatlandObject source;

    /**
     * Setter for the source of the GUI, the object is draws
     * @param s Instance of the object
     */

    public void setSource(AbstractFlatlandObject s) {
        source = s;
    }

    /**
     * Getter for the source
     * @return Reference to the source
     */

    public AbstractFlatlandObject getSource() {
        return source;
    }

    /**
     * Method for drawing
     * @param c The canvas to draw on
     * @return The canvas on which the drawing is done on
     */

    public abstract void draw(Canvas c);
}
