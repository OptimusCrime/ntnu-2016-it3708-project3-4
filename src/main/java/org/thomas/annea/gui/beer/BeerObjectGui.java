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
        if (beerObject.getSize() <= 5) {
            gc.setFill(Color.BLUE);
        }
        else {
            gc.setFill(Color.RED);
        }

        // Draw the rect
        gc.fillRect(x, y, width, height);

        // Draw lines
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        for (int i = 1; i <= beerObject.getSize(); i++) {
            gc.moveTo(x + (i * OBJECTSIZE), y);
            gc.lineTo(x + (i * OBJECTSIZE), y + OBJECTSIZE);
            gc.stroke();
        }
    }
}
