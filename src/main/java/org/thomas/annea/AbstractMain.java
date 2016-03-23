package org.thomas.annea;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.thomas.annea.controller.AbstractController;
import org.thomas.annea.tools.ConfigLoader;

import java.io.IOException;

public abstract class AbstractMain extends Application {

    // For the config loader
    protected static ConfigLoader config;

    // Reference to the controller
    protected static AbstractController controller;

    // For the config loader
    protected static String configLoaderIdentifier = "flatland";

    // For the GUI title
    protected static String title = "Project 3";

    // For the gui fxml
    protected static String fxmlFile = "flatland_layout.fxml";

    /**
     * Fetches the config and applies the settings to the controller
     * @param args Main method arguments
     */

    protected static void parseConfig(String []args) {
        // Parse the settings
        if (args.length > 0) {
            config = new ConfigLoader(args[0], configLoaderIdentifier);
        }
        else {
            config = new ConfigLoader(configLoaderIdentifier);
        }
    }

    /**
     * JavaFX runner method
     * @param primaryStage Instance of the stage on which to draw on
     * @throws Exception If something dies
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set title and resize options
        primaryStage.setTitle("Thomas Gautvedt :: IT3708 :: " + title);
        primaryStage.setResizable(false);

        // Try to fetch the layout
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/" + fxmlFile));

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
