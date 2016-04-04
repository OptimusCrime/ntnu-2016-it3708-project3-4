package org.thomas.annea.gui.flatland;

import org.thomas.annea.flatland.Agent;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AgentGui extends AbstractFlatlandGui {

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
    public void draw(Canvas c) {
        // Cast the source to an instance of agent
        Agent agent = (Agent) source;

        int x = agent.getX() * SIZE;
        int y = agent.getY() * SIZE;

        // Get 2D context to draw on
        GraphicsContext gc = c.getGraphicsContext2D();

        // Draw the agent
        gc.setFill(Color.BLUE);
        gc.fillOval(x + 10, y + 10, SIZE - 20, SIZE - 20);

        // Draw the line
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);

        // Calculate the heading indicator
        int headingX;
        int headingY;
        if (agent.getHeading() == Agent.UP) {
            headingX = x + (SIZE / 2) - 2;
            headingY = y + (SIZE / 2) - 40;
        }
        else if (agent.getHeading() == Agent.DOWN) {
            headingX = x + (SIZE / 2) - 2;
            headingY = y + (SIZE / 2) + 40;
        }
        else if (agent.getHeading() == Agent.RIGHT) {
            headingX = x + (SIZE / 2) + 40;
            headingY = y + (SIZE / 2) - 2;
        }
        else {
            headingX = x + (SIZE / 2) - 40;
            headingY = y + (SIZE / 2) - 2;
        }

        // Draw the heading indicator
        gc.strokeLine(x + (SIZE / 2) - 2, y + (SIZE / 2) - 2, headingX, headingY);
    }
}
