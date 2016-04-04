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
    public void draw(Canvas c) {
        // Cast the source to an instance of agent
        BeerObject beerObject = (BeerObject) source;

        // Get 2D context to draw on
        GraphicsContext gc = c.getGraphicsContext2D();

        // Get the various locations
        int x = beerObject.getLocation() * OBJECTSIZE;
        int y = (14 - beerObject.getRow()) * OBJECTSIZE;
        int height = OBJECTSIZE;
        int width = beerObject.getSize() * OBJECTSIZE;

        // Colorize
        gc.setFill(Color.BLUE);

        // Draw the rect
        gc.fillRect(x, y, width, height);
    }
}
