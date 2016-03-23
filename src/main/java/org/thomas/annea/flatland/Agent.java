package org.thomas.annea.flatland;

import org.jblas.DoubleMatrix;
import org.thomas.annea.gui.AgentGui;

public class Agent extends AbstractFlatlandObject {

    // Various headings
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    // Various agent variables
    private int x;
    private int y;
    private int heading;

    // Holds the size of the flatland world
    private int flatlandSize;

    /**
     * Constructor
     * @param xCoor The initial x coordinate
     * @param yCoor The initial y coordinate
     * @param h The initial heading
     */

    public Agent(int xCoor, int yCoor, int h) {
        super(new AgentGui());

        // Set the gui source
        gui.setSource(this);

        x = xCoor;
        y = yCoor;
        heading = h;
    }

    /**
     * Method for storing the size of the flatland (to shorten the code)
     * @param size Size of the flatland
     */

    public void setFlatlandSize(int size) {
        flatlandSize = size;
    }

    /**
     * Method for reading the input sensors from the agent
     * @param grid The grid to read from
     * @return The matrix of read values
     */

    public DoubleMatrix getInput(Cell[][] grid) {
        int[][] cellsToRead = new int[3][2];

        // Get the headings
        if (heading == UP) {
            cellsToRead[0][0] = x - 1;
            cellsToRead[0][1] = y;

            cellsToRead[1][0] = x;
            cellsToRead[1][1] = y - 1;

            cellsToRead[2][0] = x + 1;
            cellsToRead[2][1] = y;
        }
        else if (heading == DOWN) {
            cellsToRead[0][0] = x + 1;
            cellsToRead[0][1] = y;

            cellsToRead[1][0] = x;
            cellsToRead[1][1] = y + 1;

            cellsToRead[2][0] = x - 1;
            cellsToRead[2][1] = y;
        }
        else if (heading == LEFT) {
            cellsToRead[0][0] = x;
            cellsToRead[0][1] = y + 1;

            cellsToRead[1][0] = x - 1;
            cellsToRead[1][1] = y;

            cellsToRead[2][0] = x;
            cellsToRead[2][1] = y - 1;
        }
        else {
            cellsToRead[0][0] = x;
            cellsToRead[0][1] = y - 1;

            cellsToRead[1][0] = x + 1;
            cellsToRead[1][1] = y;

            cellsToRead[2][0] = x;
            cellsToRead[2][1] = y + 1;
        }

        // Make sure non of the headings are illegal
        for (int i = 0; i < 3; i++) {
            if (cellsToRead[i][0] < 0) {
                cellsToRead[i][0] = flatlandSize - 1;
            }
            else if (cellsToRead[i][0] == flatlandSize) {
                cellsToRead[i][0] = 0;
            }

            if (cellsToRead[i][1] < 0) {
                cellsToRead[i][1] = flatlandSize - 1;
            }
            else if (cellsToRead[i][1] == flatlandSize) {
                cellsToRead[i][1] = 0;
            }
        }

        // Create matrix for the input values where all values are 0
        DoubleMatrix inputValues = DoubleMatrix.zeros(1, 6);

        // Create array of food types
        int[] foodTypes = new int[]{Cell.FOOD, Cell.POISON};

        // Keep track of the actual input index
        int inputIndex = 0;

        // Loop the food types
        for (int k = 0; k < 2; k++) {
            // Loop the three cells to read
            for (int i = 0; i < 3; i++) {
                int[] cellToRead = cellsToRead[i];

                // Fetch the correct cell
                Cell currentCell = grid[cellToRead[1]][cellToRead[0]];

                // Check if th current cell contains our current type
                if (currentCell.getState() == foodTypes[k]) {
                    inputValues.put(0, inputIndex, 1);
                }

                // Update the input index regardless
                inputIndex++;
            }
        }

        return inputValues;
    }

    /**
     * Method for rotating the agent
     * @param direction Direction to rotate in
     */

    public void rotate(int direction) {
        // Check if we should rotate clockwise of counter-clockwise
        if (direction == 2) {
            // Clockwise
            if (heading == UP) {
                heading = RIGHT;
            }
            else if (heading == RIGHT) {
                heading = DOWN;
            }
            else if (heading == DOWN) {
                heading = LEFT;
            }
            else {
                heading = UP;
            }
        }
        else {
            // Counter clockwise
            if (heading == UP) {
                heading = LEFT;
            }
            else if (heading == LEFT) {
                heading = DOWN;
            }
            else if (heading == DOWN) {
                heading = RIGHT;
            }
            else {
                heading = UP;
            }
        }
    }

    /**
     * Method for moving the agent. Will only move straight ahead, so the agent has to be rotated first to change
     * course.
     */

    public void move() {
        // Do the actual move
        if (heading == UP) {
            y--;
        }
        else if (heading == DOWN) {
            y++;
        }
        else if (heading == LEFT) {
            x--;
        }
        else {
            x++;
        }

        // Make sure to wrap
        if (y < 0) {
            y = flatlandSize - 1;
        }
        if (y == flatlandSize) {
            y = 0;
        }
        if (x < 0) {
            x = flatlandSize - 1;
        }
        if (x == flatlandSize) {
            x = 0;
        }
    }

    /**
     * Method for applying the movement of the current agent on a grid. Will first rotate the agent (if needed), then
     * do the actual move. Last if will record anything that was eaten.
     * @param direction Direction to move in
     * @param grid Grid to move on
     * @return Array of food and poison that was eaten
     */

    public int[] move(int direction, Cell[][] grid) {
        int[] ate = new int[]{0, 0};

        if (direction != 1) {
            rotate(direction);
        }

        // Do the actual move
        move();

        // Check what we are currently on
        Cell currentGrid = grid[y][x];

        // Check if the current cell has any content
        if (currentGrid.getState() != Cell.EMPTY) {
            // Store what was eaten
            if (currentGrid.getState() == Cell.FOOD) {
                ate[0] = 1;
            }
            else {
                ate[1] = 1;
            }

            // Set the grid state to empty
            currentGrid.setState(Cell.EMPTY);
        }

        // Return the overview of what was eaten
        return ate;
    }

    /**
     * Various getters
     * @return Value(s)
     */

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeading() {
        return heading;
    }
}
