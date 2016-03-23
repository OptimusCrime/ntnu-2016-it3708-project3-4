package org.thomas.annea.runner;

import org.jblas.DoubleMatrix;
import org.thomas.annea.ann.Network;
import org.thomas.annea.flatland.Agent;
import org.thomas.annea.flatland.Cell;
import org.thomas.annea.tools.Settings;

public class FlatlandProblemRunner {

    // Reference to the settings
    private Settings settings;

    // Various references to stuff we need to run the problem
    private Network ann;
    private Cell[][] grid;
    private Agent agent;

    // Keep track of the current timestep
    private int timestep;

    // Stats
    private int food;
    private int poison;
    private int totalFood;
    private int totalPoison;

    /**
     * Constructor
     * @param s Instance of Settings
     */

    public FlatlandProblemRunner(Settings s) {
        // Set reference to settings
        settings = s;

        // Store the current timestep
        timestep = 0;
        food = 0;
        poison = 0;
        totalFood = 0;
        totalPoison = 0;
    }

    /**
     * Run the maximum number of timesteps
     */

    public void runAll() {
        // Get the information
        getTotal();

        // Loop the total number of timesteps and call runStep each time
        for (int k = 0; k < settings.getMaxTimesteps(); k++) {
            // Rune one step
            runStep();
        }
    }

    /**
     * Run one step
     */

    public boolean runStep() {
        // Check if can run the step at all
        if (timestep == settings.getMaxTimesteps()) {
            // We are done running
            return false;
        }

        // We are not done running, increase the current timestep
        timestep++;

        // Get the input from the agent
        DoubleMatrix inputSensors = agent.getInput(grid);

        // Propagate the sensor values
        DoubleMatrix output = ann.propagate(inputSensors);

        // Direction
        int direction = (int) output.get(0, 0);

        // Get the stats from this round
        int[] ate = agent.move(direction, grid);

        // Update food and poison
        food += ate[0];
        poison += ate[1];

        // Return true to indicate that the timestep was ran
        return true;
    }

    /**
     * Get the stats for the grid, which is number of total food and total poisons there are
     */

    public void getTotal() {
        // Avoid running this multiple times
        if (totalFood == 0 && totalPoison == 0) {
            // Loop the total variables
            int size = grid.length;
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (grid[y][x].getState() == Cell.FOOD) {
                        totalFood++;
                    }
                    else if (grid[y][x].getState() == Cell.POISON) {
                        totalPoison++;
                    }
                }
            }
        }
    }

    /**
     * Setter for Network
     * @param n Instance of Network
     */

    public void setNetwork(Network n) {
        ann = n;
    }

    /**
     * Setter for Grid
     * @param g Instance of Grid
     */

    public void setGrid(Cell[][] g) {
        // Reference the grid
        grid = g;

        // Get the totals
        getTotal();
    }

    /**
     * Setter for Agent
     * @param a Instance of Agent
     */

    public void setAgent(Agent a) {
        agent = a;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Agent getAgent() {
        return agent;
    }

    public int getFood() {
        return food;
    }

    public int getPoison() {
        return poison;
    }

    public int getTotalFood() {
        return totalFood;
    }

    public int getTotalPoison() {
        return totalPoison;
    }

    public int getTimestep() {
        return timestep;
    }

}
