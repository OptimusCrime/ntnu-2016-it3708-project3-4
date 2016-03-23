package org.thomas.annea.solvers;

import org.thomas.annea.tools.settings.AbstractSettings;

public abstract class AbstractSolver {

    // Reference to the settings
    protected AbstractSettings settings;

    /**
     * Constructor
     * @param s Instance of Settings
     */

    public AbstractSolver(AbstractSettings s) {
        // Store reference to the settings
        settings = s;
    }

    /**
     * Method for solving the problem
     */

    public abstract void solve();
}
