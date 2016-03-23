package org.thomas.project3.gui;

import org.thomas.project3.flatland.Cell;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CellGui extends AbstractGui {

    /**
     * Constructor
     */

    public CellGui() {
        super();
    }

    /**
     * Draw the cell
     * @return The Canvas on which the cell is drawn upon
     */

    @Override
    public Canvas draw() {
        // Cast the source to an instance of Cell
        Cell cell = (Cell) source;

        // Check if we should change the size
        if ((int) canvas.getHeight() != (SIZE + 1)) {
            canvas.setHeight(SIZE + 1);
            canvas.setWidth(SIZE + 1);
        }

        // Clear the canvas
        canvas.getGraphicsContext2D().clearRect(0, 0, SIZE + 1, SIZE + 1);

        // Translate to correct position on the stage
        canvas.setTranslateX((cell.getX() * SIZE) + cell.getX());
        canvas.setTranslateY((cell.getY() * SIZE) + cell.getY());

        // Get 2D context to draw on
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the actual square
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, SIZE, SIZE);

        // Draw lines
        gc.setStroke(new Color(0.8784313725, 0.8784313725, 0.8784313725, 1));
        gc.setLineWidth(1.0);
        gc.moveTo(0, SIZE + 1);
        gc.lineTo(SIZE + 1, SIZE + 1);
        gc.stroke();
        gc.moveTo(SIZE + 1, 0);
        gc.lineTo(SIZE + 1, SIZE + 1);
        gc.stroke();

        // Check if we need to draw anything else
        if (cell.getState() != Cell.EMPTY) {
            // Check what color we should switch to
            if (cell.getState() == Cell.POISON) {
                // Set poison color
                gc.setFill(Color.RED);
            }
            else {
                // Set food color
                gc.setFill(Color.GREEN);

            }

            // Draw the object
            gc.fillOval(10, 10, SIZE - 20, SIZE - 20);
        }

        // Return the entire canvas
        return canvas;
    }
}
