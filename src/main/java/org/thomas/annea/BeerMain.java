package org.thomas.annea;

import org.thomas.annea.controller.BeerController;

public class BeerMain extends AbstractMain {

    /**
     * Main function
     * @param args System arguments
     */

    public static void main(String []args) {
        // Overload abstract main stuff
        configLoaderIdentifier = "beer";
        title = "Project 4";
        fxmlFile = "beer_layout.fxml";

        // Parse the config
        parseConfig(args);

        // Set the correct controller
        controller = new BeerController((config.getSettings()));

        // Start JavaFX magic here
        launch(args);
    }
}
