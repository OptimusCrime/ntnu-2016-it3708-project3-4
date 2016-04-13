package org.annea;

import org.annea.controller.FlatlandController;

public class FlatlandMain extends AbstractMain {

    /**
     * Main function
     * @param args System arguments
     */

    public static void main(String []args) {
        // Parse the config
        parseConfig(args);

        // Set the correct controller
        controller = new FlatlandController((config.getSettings()));

        // Start JavaFX magic here
        launch(args);
    }
}
