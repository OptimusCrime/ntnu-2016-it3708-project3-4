package org.thomas.annea.flatland;

import org.thomas.annea.tools.RandomHelper;
import org.thomas.annea.tools.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scenario {

    // Reference to the settings
    private Settings settings;

    // Various settings
    private double probabilityFood;
    private double probabilityPoison;

    // For the representation
    private String signature;
    private int[] agentLocation;
    private int agentHeading;

    /**
     * Constructor
     */

    public Scenario(Settings s) {
        // Set the settings
        settings = s;

        // Get the probabilities for food and poison
        probabilityFood = 1 / (double) 4;
        probabilityPoison = 1 / (double) 4;

        // Check if we can override the probabilities
        String []probabilities = new String[]{"probability_f", "probability_p"};
        for (int i = 0; i < 2; i++) {
            if (settings.getSetting(probabilities[i]) != null) {
                try {
                    // Try to parse the settings double value (hack to generate true double values)
                    Double probability = 1 / Double.parseDouble(settings.getSetting(probabilities[i]));

                    // Check what variable to store in
                    if (i == 0) {
                        probabilityFood = probability;
                    }
                    else {
                        probabilityPoison = probability;
                    }
                }
                catch (Exception e) {
                }
            }
        }
    }

    /**
     * Creater for the scenario
     * @param size Length of each scenario
     */

    public void createScenario(int size) {
        // Calculate the string length
        int stringLength = size * size;

        // Create the empty string first
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            builder.append(" ");
        }

        // List the types
        double[] probabilities = new double[]{probabilityFood, probabilityPoison};
        int[] states = new int[]{49, 50};

        // Loop the types
        for (int stateIndex = 0; stateIndex < 2; stateIndex++) {
            // Create array list to hold all the empty cells
            List<Integer> emptyCells = new ArrayList<>();

            // Loop the entire grid
            for (int i = 0; i < stringLength; i++) {
                if (builder.charAt(i) == ' ') {
                    emptyCells.add(i);
                }
            }

            // Populate the
            placeGridState(builder, emptyCells, states[stateIndex], probabilities[stateIndex]);
        }

        // Convert the string builder to the signature
        signature = builder.toString();

        // Place the agent
        placeAgent();
    }

    /**
     * Place food and poison on the grid
     */

    private void placeGridState(StringBuilder builder, List<Integer> emptyCells, int state, double probability) {
        Random r = new Random();

        // Loop all the empty cells
        for (int i = 0; i < emptyCells.size(); i++) {
            // Check if we should populate this cell
            if (r.nextDouble() <= probability) {
                // Set the correct state here
                builder.setCharAt(i, (char) state);
            }
        }
    }

    /**
     * Plage an agent on the grid in a cell that is empty
     */

    private void placeAgent() {
        // Find all empty cells
        List<Integer> emptyCells = new ArrayList<>();
        for (int i = 0; i < signature.length(); i++) {
            if (signature.charAt(i) == ' ') {
                emptyCells.add(i);
            }
        }

        // Chose random position from the list
        int randomPos = emptyCells.get(RandomHelper.randint(0, emptyCells.size()));

        // Get the grid size
        int gridSize = (int) Math.sqrt(signature.length());

        // Calculate the different positions
        int posY = (int) (Math.ceil((randomPos + 1) / (double) gridSize) - 1);
        int posX = randomPos - (posY * gridSize);

        // Store the actual position
        agentLocation = new int[]{
                posX, posY
        };

        // Store the agent heading
        agentHeading = RandomHelper.randint(0, 4);
    }

    /**
     * Return the grid as a vector
     * @return The vector
     */

    public Cell[] getGrid() {
        // Create empty array for the grid
        Cell[] grid = new Cell[signature.length()];

        // Get the grid size
        int gridSize = (int) Math.sqrt(signature.length());
        int actualIndex = 0;

        // Loop the entire grid
        for(int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                // Get the current state
                int state = Cell.EMPTY;
                if (signature.charAt(actualIndex) == 49) {
                    state = Cell.FOOD;
                }
                else if (signature.charAt(actualIndex) == 50) {
                    state = Cell.POISON;
                }

                // Create the new cell
                grid[actualIndex] = new Cell(x, y,  state);

                // Because simplicity
                actualIndex++;
            }
        }

        // Return the grid
        return grid;
    }

    /**
     * Get the grid as a matrix
     * @return The matrix
     */

    public Cell[][] getGridTwoDimensions() {
        // Get the grid size
        int gridSize = (int) Math.sqrt(signature.length());

        // Create empty array for the grid
        Cell[][] grid = new Cell[gridSize][gridSize];


        int actualIndex = 0;

        // Loop the entire grid
        for(int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                // Get the current state
                int state = Cell.EMPTY;
                if (signature.charAt(actualIndex) == 49) {
                    state = Cell.FOOD;
                }
                else if (signature.charAt(actualIndex) == 50) {
                    state = Cell.POISON;
                }

                // Create the new cell
                grid[y][x] = new Cell(x, y,  state);

                // Because simplicity
                actualIndex++;
            }
        }

        // Return the grid
        return grid;
    }

    /**
     * Get the agent
     * @return The agent
     */

    public Agent getAgent() {
        // Create the new agent
        Agent agent = new Agent(agentLocation[0], agentLocation[1], agentHeading);

        // Set the flatland size
        agent.setFlatlandSize((int) Math.sqrt(signature.length()));

        // Return the new agent
        return agent;
    }

    /**
     * Stringification of the instance of a scenario
     * @return The string
     */

    public String toString() {
        return signature + "| Agent (" + agentLocation[0] + ",  " + agentLocation[1] + ")";
    }
}
