package org.thomas.annea.gui;

import org.thomas.annea.flatland.Agent;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AgentGui extends AbstractGui {

    /**
     * Constructor!
     */

    public AgentGui() {
        super();
    }

    /**
     * Draw the agent
     * @return The Canvas on which the agent is drawn upon
     */

    @Override
    public Canvas draw() {
        // Cast the source to an instance of agent
        Agent agent = (Agent) source;

        // Check if we should change the size
        if ((int) canvas.getHeight() != (SIZE + 1)) {
            canvas.setHeight(SIZE + 1);
            canvas.setWidth(SIZE + 1);
        }

        // Clear the canvas
        canvas.getGraphicsContext2D().clearRect(0, 0, SIZE + 1, SIZE + 1);

        // Translate to correct position on the stage
        canvas.setTranslateX((agent.getX() * SIZE) + agent.getX());
        canvas.setTranslateY((agent.getY() * SIZE) + agent.getY());

        // Get 2D context to draw on
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the agent
        gc.setFill(Color.BLUE);
        gc.fillOval(10, 10, SIZE - 20, SIZE - 20);

        // Draw the line
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);

        // Calculate the heading indicator
        int center = (SIZE / 2) - 2;
        int x = 0;
        int y = 0;

        if (agent.getHeading() == Agent.UP) {
            x = center;
            y = 0;
        }
        else if (agent.getHeading() == Agent.DOWN) {
            x = center;
            y = SIZE;
        }
        else if (agent.getHeading() == Agent.RIGHT) {
            x = SIZE;
            y = center;
        }
        else {
            x = 0;
            y = center;
        }

        // Draw the heading indicator
        gc.strokeLine(center, center, x, y);

        // Return the entire canvas
        return canvas;
    }
}
