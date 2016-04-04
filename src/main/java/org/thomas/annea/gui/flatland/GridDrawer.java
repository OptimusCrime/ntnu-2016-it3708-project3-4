package org.thomas.annea.gui.flatland;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridDrawer {

    public static void draw(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();

        gc.setStroke(new Color(0.8784313725, 0.8784313725, 0.8784313725, 1));
        gc.setLineWidth(1.0);

        for (int i = 0; i < 9; i++) {
            // Horizontal lines
            gc.moveTo(0, AbstractFlatlandGui.SIZE * (i + 1));
            gc.lineTo(750, AbstractFlatlandGui.SIZE * (i + 1));
            gc.stroke();

            // Vertical lines
            gc.moveTo(AbstractFlatlandGui.SIZE * (i + 1), 0);
            gc.lineTo(AbstractFlatlandGui.SIZE * (i + 1), 750);
            gc.stroke();
        }
    }
}
