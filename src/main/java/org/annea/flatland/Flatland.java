package org.annea.flatland;

import org.annea.tools.settings.AbstractSettings;

import java.util.ArrayList;
import java.util.List;

public class Flatland {

    // Reference to the settings instance
    private AbstractSettings settings;

    // Number of scenarios and their size
    private int size;

    // Return the number of scenarios we should run
    private int scenariosToRun;

    // The actual scenarios
    private List<Scenario> scenarios;

    /**
     * Constructor for the Flatland class
     * @param s Instance of Settings
     */

    public Flatland(AbstractSettings s) {
        // Reference to settings
        settings = s;

        // Create the scenario list
        scenarios = new ArrayList<>();
    }

    public AbstractSettings getSettings() {
        return settings;
    }

    /**
     * Create the flatland scenarios
     */

    public void initialize() {
        // Store scenario size (because we use it many places)
        size = 10;

        // Check if any settings overrides the default value
        if (settings.getSetting("grid") != null) {
            try {
                size = Integer.parseInt(settings.getSetting("grid"));
            }
            catch (Exception e) {
            }
        }

        // Store the number of grids we are creating (because)
        scenariosToRun = 1;

        // Again check if any settings overrides this
        if (settings.getSetting("scenarios") != null) {
            try {
                scenariosToRun = Integer.parseInt(settings.getSetting("scenarios"));
            }
            catch (Exception e) {
            }
        }

        // Loop and create each grid
        for (int i = 0; i < scenariosToRun; i++) {
            // Reuse of code for the win
            newScenario();
        }
    }

    /**
     * Remove all present scenarios
     */

    public void clearScenarios() {
        scenarios.clear();
    }

    /**
     * Create a new scenario
     */

    public void newScenario() {
        Scenario newScenario = new Scenario(settings);
        newScenario.createScenario(size);

        // Add to list
        scenarios.add(newScenario);
    }

    /**
     * Getters
     * @return Values
     */

    public int getScenariosToRun() {
        return scenariosToRun;
    }

    public Scenario getScenario(int i) {
        return scenarios.get(i);
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public int getSize() {
        return size;
    }
}
