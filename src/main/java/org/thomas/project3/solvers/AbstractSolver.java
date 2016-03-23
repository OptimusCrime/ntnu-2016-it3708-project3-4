package org.thomas.project3.solvers;

import org.thomas.project3.tools.Settings;

public abstract class AbstractSolver {

    // Reference to the settings
    protected Settings settings;

    /**
     * Constructor
     * @param s Instance of Settings
     */

    public AbstractSolver(Settings s) {
        // Store reference to the settings
        settings = s;
    }

    /**
     * Method for solving the problem
     */

    public abstract void solve();
}
