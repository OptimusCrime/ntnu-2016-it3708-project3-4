package org.thomas.annea.gui.beer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.thomas.annea.beer.BeerObject;

public class BeerObjectGui extends AbstractBeerGui {

    /**
     * Constructor!
     */

    public BeerObjectGui() {
        super();
    }

    /**
     * Draw the agent
     * @return The Canvas on which the agent is drawn upon
     */

    @Override
    public Canvas draw() {
        // Cast the source to an instance of agent
        BeerObject beerObject = (BeerObject) source;

        // Check if we need to do the initial drawing
        if (!drawn) {
            // Create the canvas content
            canvas.setHeight(OBJECTSIZE);
            canvas.setWidth(OBJECTSIZE * beerObject.getSize());

            // Fill the canvas with content
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.BLUE);
            gc.fillRect(0, 0, OBJECTSIZE * beerObject.getSize(), OBJECTSIZE);

            // Set drawn to true, to avoid full redraw
            drawn = true;
        }

        // Move the object to the correct location
        canvas.setTranslateX(beerObject.getLocation() * OBJECTSIZE);
        canvas.setTranslateY((14 - beerObject.getRow()) * OBJECTSIZE);

        // Return the canvas
        return canvas;
    }
}
