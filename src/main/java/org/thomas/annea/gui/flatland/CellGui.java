package org.thomas.annea.gui.flatland;

import org.thomas.annea.flatland.Cell;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CellGui extends AbstractFlatlandGui {

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
    public void draw(Canvas c) {
        // Cast the source to an instance of Cell
        Cell cell = (Cell) source;

        int x = cell.getX() * SIZE;
        int y = cell.getY() * SIZE;

        // Get 2D context to draw on
        GraphicsContext gc = c.getGraphicsContext2D();

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
            gc.fillOval(x + 10, y + 10, SIZE - 20, SIZE - 20);
        }
    }
}
