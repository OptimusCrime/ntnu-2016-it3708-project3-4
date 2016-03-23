package org.thomas.annea.gui;

import org.thomas.annea.flatland.AbstractFlatlandObject;

import javafx.scene.canvas.Canvas;

public class AbstractGui {

    // The size of each GUI object
    public static int SIZE = 75;

    // Instance of the source object, which is a flatland object
    protected AbstractFlatlandObject source;

    // The canvas
    protected Canvas canvas;

    public AbstractGui() {
        // Create the actual canvas
        canvas = new Canvas(SIZE + 1, SIZE + 1);
    }

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
     * @return The canvas on which the drawing is done on
     */

    public Canvas draw() {
        return canvas;
    }
}
