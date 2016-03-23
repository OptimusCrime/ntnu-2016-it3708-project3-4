package org.thomas.annea.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import org.thomas.annea.ann.Network;
import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.flatland.Cell;
import org.thomas.annea.flatland.Scenario;
import org.thomas.annea.runner.FlatlandProblemRunner;
import org.thomas.annea.solvers.FlatlandSolver;
import org.thomas.annea.tools.settings.AbstractSettings;

import java.net.URL;
import java.util.ResourceBundle;

public class BeerController extends AbstractController implements Initializable {

    // JavaFX stuff
    @FXML private Pane main;
    @FXML private Group group;
    @FXML private ChoiceBox choiceBoxScenario;
    @FXML private Slider sliderRefreshRate;

    @FXML private Button buttonScenarioNew;
    @FXML private Button buttonPlayPause;

    @FXML private Label labelRefreshRate;
    @FXML private Label labelTimestep;
    @FXML private Label labelFood;
    @FXML private Label labelPoison;

    // Various dropdown stuff
    private int choiceBoxIndex;
    private ObservableList choiceBoxOptions;

    // Length of each tick
    private static int tickLength = 200;

    // Reference to the solver
    private FlatlandSolver solver;

    // For the simulations
    private static FlatlandProblemRunner runner;

    // For the timeline
    private Timeline timeline;

    /**
     * Constructor
     * @param s Instance of Settings
     */

    public BeerController(AbstractSettings s) {
        super(s);

        // Set reference afterwards (because hax)
        setReference(this);
    }

    /**
     * JavaFX black magic
     * @param location No idea
     * @param resources No idea
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateGui();

        /*
        // New solver
        solver = new FlatlandSolver(settings);

        // Solve the problem
        solver.solve();

        // Start the interval
        startInterval();

        // Populate the gui with the various things needed
        populateGui();

        // Load the first scenario
        loadScenario(0);*/
    }

    /**
     * Start the interval that updates the view every nth ms
     */

    private void startInterval() {
        // Create a new timeline
        timeline = new Timeline();

        // Create the initial keyframe
        timeline.getKeyFrames().setAll(newKeyFrame());

        // Set count
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Play the timeline
        timeline.play();
    }

    /**
     * Dynamic construct keyframes to allow for adjusting keyframe update rete
     * @return The new keyframe with the new duration
     */

    private KeyFrame newKeyFrame() {
        // Create a new keyframe
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(tickLength),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        tick();
                    }
                }
        );

        // Return the new keyframe
        return keyFrame;
    }

    /**
     * Visualize the problem here
     */

    private void populateGui() {
        // Set various slider things
        sliderRefreshRate.setMin(100);
        sliderRefreshRate.setMax(1000);
        sliderRefreshRate.setValue(tickLength);

        // Set slider label
        labelRefreshRate.setText(tickLength + "ms");

        // Slider on change
        sliderRefreshRate.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
                // Store the new tick length
                tickLength = newVal.intValue();

                // Get a new keyframe
                KeyFrame keyFrame = newKeyFrame();

                // Pause the timeline
                timeline.stop();

                // Set the new keyframe
                timeline.getKeyFrames().setAll(keyFrame);

                // Start the timeline again
                timeline.play();

                // Update the label
                labelRefreshRate.setText(tickLength + "ms");
            }
        });

        // For grouping everything together
        group = new Group();

        // Add the group to the main pane
        main.getChildren().add(group);

        // Request focus
        main.requestFocus();
    }

    /**
     * Load a scenario
     * @param index Scenario index to load
     */

    private void loadScenario(int index) {
        // Store the index of the scenario we have present
        choiceBoxIndex = index;

        // Make sure to pause the player
        running = false;

        // Set the button text and disabled state
        buttonPlayPause.setDisable(false);
        buttonPlayPause.setText("Play");

        // Reset the timestep
        labelTimestep.setText("1");

        // Get the scenario
        Scenario scenario = solver.getFlatland().getScenario(index);

        // Set up the network
        Network ann = solver.getNetwork();

        // Get the best runner from the EA
        AbstractGType bestIndividual = solver.getBestIndividual();

        // Apply the weights from the best individual to the ANN
        ann.setWeights(bestIndividual);

        runner = new FlatlandProblemRunner(settings);
        runner.setNetwork(solver.getNetwork());
        runner.setGrid(scenario.getGridTwoDimensions());
        runner.setAgent(scenario.getAgent());

        // Update eaten stats
        labelFood.setText("0 / " + runner.getTotalFood());
        labelPoison.setText("0 / " + runner.getTotalPoison());

        // Draw the initial frame
        draw();
    }

    /**
     * Simulate a single tick
     */

    public void tick() {
        // Make sure we are running
        if (running) {
            // Run a step
            boolean state = runner.runStep();

            // Check the state
            if (!state) {
                // We are done running, stop the simulation
                running = false;

                // Set button things
                buttonPlayPause.setDisable(true);
                buttonPlayPause.setText("Finished");
            }
            else {
                // Update the current tick
                labelTimestep.setText(Integer.toString(runner.getTimestep()));

                // Update eaten stats
                labelFood.setText(runner.getFood() + " / " + runner.getTotalFood());
                labelPoison.setText(runner.getPoison() + " / " + runner.getTotalPoison());

                // Draw the tick
                draw();
            }
        }
    }

    /**
     * Draw the graphics
     */

    private void draw() {
        // Clear all the children
        group.getChildren().clear();

        // Get the grid
        Cell[][] grid = runner.getGrid();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                group.getChildren().add(grid[y][x].getGui().draw());
            }
        }

        // Add the agent
        group.getChildren().add(runner.getAgent().getGui().draw());
    }

    /**
     * Toggle running state
     * @param event No idea
     */

    @FXML
    public void buttonPlayPauseClicked(Event event) {
        // Can I has the reuse of code plx
        togglePausePlay();
    }

    /**
     * Used to toggle pause/play from the keyboard by using space or the P-button
     */

    public void togglePausePlay() {
        running = !running;

        // Update the button text
        if (running) {
            buttonPlayPause.setText("Pause");
        }
        else {
            buttonPlayPause.setText("Play");
        }
    }
}
