package org.annea.controller;

import org.annea.tools.settings.AbstractSettings;

public class AbstractController {

    // Reference to the controller
    private AbstractController reference;

    // Reference to the settings
    protected AbstractSettings settings;

    // For the timeline
    protected boolean running;

    /**
     * Constructor
     * @param s Instance of Settings
     */

    public AbstractController(AbstractSettings s) {
        // Store references
        settings = s;

        // Set running to false
        running = false;
    }

    protected void setReference(AbstractController r) {
        reference = r;
    }

    /**
     * Subclasses 4tw
     */

    public void togglePausePlay() {
        reference.togglePausePlay();
    }
}
