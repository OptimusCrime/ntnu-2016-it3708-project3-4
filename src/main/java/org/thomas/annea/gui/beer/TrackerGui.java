package org.thomas.annea.gui.beer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jblas.DoubleMatrix;
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

        // Get the tracker location
        int[] trackerLocation = tracker.getTrackerLocation();

        // Tracker stuff
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(Color.GRAY);

        // Draw the tracker
        for (int i = 0; i < trackerLocation.length; i++) {
            int x = trackerLocation[i] * OBJECTSIZE;
            int y = 600 - OBJECTSIZE;
            gc.fillRect(x, y, OBJECTSIZE, OBJECTSIZE);
        }

        // Draw the lines
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);
        for (int i = 0; i < trackerLocation.length; i++) {
            gc.moveTo(trackerLocation[i] * OBJECTSIZE, 600 - OBJECTSIZE);
            gc.lineTo(trackerLocation[i] * OBJECTSIZE, 600);
            gc.stroke();
        }

        // Get beer object (if any)
        DoubleMatrix input = null;
        if (tracker.getBeerObjectReference() != null) {
            // Set the input correctly
            input = tracker.getInput(tracker.getBeerObjectReference());
        }

        // Draw the buttons
        for (int i = 0; i < trackerLocation.length; i++) {
            // Get some initial locations
            int x = trackerLocation[i] * OBJECTSIZE;
            int y = 600 - OBJECTSIZE;

            if (input != null && (int) input.get(0, i) == 1) {
                gc.setFill(Color.GREEN);
            }
            else {
                gc.setFill(Color.RED);
            }

            // Draw the oval
            gc.fillOval(x + 10, y + 10, OBJECTSIZE - 20, OBJECTSIZE - 20);
        }
    }
}
