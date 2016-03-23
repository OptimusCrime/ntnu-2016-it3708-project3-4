package org.thomas.annea;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.thomas.annea.controller.FlatlandController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.thomas.annea.tools.ConfigLoader;

import java.io.IOException;

public class Main extends Application {

    // Reference to the controller
    private static FlatlandController controller;

    /**
     * Main function
     * @param args System arguments
     */

    public static void main(String []args) {
        // Parse the config
        parseConfig(args);

        // Start JavaFX magic here
        launch(args);
    }

    /**
     * Fetches the config and applies the settings to the controller
     * @param args Main method arguments
     */

    public static void parseConfig(String []args) {
        // Parse the settings
        ConfigLoader config;
        if (args.length > 0) {
            config = new ConfigLoader(args[0]);
        }
        else {
            config = new ConfigLoader();
        }

        // Create the controller and supply the settings
        controller = new FlatlandController(config.getSettings());
    }

    /**
     * JavaFX runner method
     * @param primaryStage Instance of the stage on which to draw on
     * @throws Exception If something dies
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set title and resize options
        primaryStage.setTitle("Thomas Gautvedt :: IT3708 :: Project 3");
        primaryStage.setResizable(false);

        // Try to fetch the layout
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/flatland_layout.fxml"));

            // Set the controller
            loader.setController(controller);

            // Load the parent panel
            Parent pane = (Parent) loader.load();

            // Create new scene and set that as the stage
            Scene myScene = new Scene(pane);

            // Add key listener on scene
            myScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent e) {
                    if (e.getCode().equals(KeyCode.SPACE) ||e.getCode().equals(KeyCode.P)) {
                        controller.togglePausePlay();
                    }
                }
            });

            primaryStage.setScene(myScene);
            primaryStage.show();
        } catch (IOException e) {
            // Something died
            e.printStackTrace();
        }
    }
}
