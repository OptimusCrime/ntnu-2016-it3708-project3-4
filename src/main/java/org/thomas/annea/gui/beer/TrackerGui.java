package org.thomas.annea.gui.beer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.thomas.annea.beer.BeerObject;
import org.thomas.annea.beer.Tracker;

public class TrackerGui extends AbstractBeerGui {

    /**
     * Constructor!
     */

    public TrackerGui() {
        super();
    }

    /**
     * Draw the agent
     * @return The Canvas on which the agent is drawn upon
     */

    @Override
    public void draw(Canvas c) {
        // Cast the source to an instance of tracker
        Tracker tracker = (Tracker) source;
        /*
        // Check if we need to do the initial drawing
        if (!drawn) {
            // Create the canvas content
            canvas.setHeight(OBJECTSIZE);
            canvas.setWidth(OBJECTSIZE * 5);

            // Fill the canvas with content
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.RED);
            gc.fillRect(0, 0, OBJECTSIZE * 5, OBJECTSIZE);

            // Set drawn to true, to avoid full redraw
            drawn = true;
        }

        // Move the object to the correct location
        canvas.setTranslateX(OBJECTSIZE * tracker.getLocation());
        canvas.setTranslateY(14 * OBJECTSIZE);*/
    }
}
