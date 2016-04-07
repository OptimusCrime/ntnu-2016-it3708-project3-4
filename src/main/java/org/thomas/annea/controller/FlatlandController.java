package org.thomas.annea.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.thomas.annea.ann.Network;
import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.flatland.Agent;
import org.thomas.annea.flatland.Cell;
import org.thomas.annea.flatland.Scenario;
import org.thomas.annea.gui.flatland.FlatlandDrawable;
import org.thomas.annea.gui.observers.GraphHelper;
import org.thomas.annea.runner.FlatlandProblemRunner;
import org.thomas.annea.solvers.AbstractSolver;
import org.thomas.annea.solvers.FlatlandSolver;
import org.thomas.annea.tools.settings.AbstractSettings;

import java.net.URL;
import java.util.ResourceBundle;

public class FlatlandController extends AbstractController implements Initializable, GraphHelper {

    // JavaFX stuff
    @FXML
    private Pane main;

    // Canvas and Plot
    @FXML
    private Canvas canvas;
    @FXML
    private LineChart graph;
    private GraphicsContext gc;

    // Images
    private Image[] flatlandImages;
    
    // Size of grid TODO: get from some kind of settings
    private double gridSize;
    private double grids = 10;

    //Tabs
    @FXML
    private TabPane tabPane;

    //@FXML private Group group;
    @FXML
    private ChoiceBox choiceBoxScenario;
    @FXML
    private Slider sliderRefreshRate;

    @FXML
    private Button buttonScenarioNew;
    @FXML
    private Button buttonPlayPause;
    @FXML
    private Button buttonGraph;

    @FXML
    private Label labelRefreshRate;
    @FXML
    private Label labelTimestep;
    @FXML
    private Label labelFood;
    @FXML
    private Label labelPoison;

    // Various dropdown stuff
    private int choiceBoxIndex;
    private ObservableList choiceBoxOptions;

    // Length of each tick
    private static int tickLength = 200;

    // Reference to the solver
    private AbstractSolver solver;

    // For the simulations
    private static FlatlandProblemRunner runner;

    // For the timeline
    private Timeline timeline;

    /**
     * Constructor
     *
     * @param s Instance of Settings
     */

    public FlatlandController(AbstractSettings s) {
        super(s);

        // Set reference afterwards (because hax)
        setReference(this);
    }

    /**
     * JavaFX black magic
     *
     * @param location  No idea
     * @param resources No idea
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Store grid size
        this.gridSize = canvas.getHeight() / this.grids;
        
        // Load images
        this.flatlandImages = new Image[6];
        this.flatlandImages[0] = new Image(getClass().getResourceAsStream("/img/agent2_forward.png"));
        this.flatlandImages[1] = new Image(getClass().getResourceAsStream("/img/agent2_backward.png"));
        this.flatlandImages[2] = new Image(getClass().getResourceAsStream("/img/agent2_left.png"));
        this.flatlandImages[3] = new Image(getClass().getResourceAsStream("/img/agent2_right.png"));
        this.flatlandImages[4] = new Image(getClass().getResourceAsStream("/img/poison.png"));
        this.flatlandImages[5] = new Image(getClass().getResourceAsStream("/img/food.png"));

        // Initialize Gui Elements
        gc = canvas.getGraphicsContext2D();

        // New solver
        solver = new FlatlandSolver(settings);

        // Solve the problem, self as observer
        solver.solve(this);

        // Start the interval
        startInterval();

        // Populate the gui with the various things needed
        populateGui();

        // Load the first scenario
        loadScenario(0);
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
     *
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

        // Cast all the things
        FlatlandSolver localSolver = (FlatlandSolver) solver;

        // Calculate the size for each GUI object
        this.gridSize = (int) ((750 - localSolver.getFlatland().getSize()) / localSolver.getFlatland().getSize());

        // Populate the dropdown
        choiceBoxOptions = FXCollections.observableArrayList();
        if (localSolver.getFlatland().getSettings().getSetting("scenario_mode").equals("static")) {
            // Static scenarios, just loop the list and add them all
            for (int i = 0; i < localSolver.getFlatland().getScenarios().size(); i++) {
                choiceBoxOptions.add("Scenario #" + (i + 1));
            }
        } else {
            // Dynamic scenarios, first remove all scenarios present
            localSolver.getFlatland().clearScenarios();

            for (int i = 0; i < localSolver.getFlatland().getScenariosToRun(); i++) {
                // Add a new scenario
                localSolver.getFlatland().newScenario();

                // Add to choice box
                choiceBoxOptions.add("Random scenario #" + (i + 1));
            }
        }

        // Graph Button
        buttonGraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tabPane.getSelectionModel().select(1);
                graph();
            }
        });

        // Set the items
        choiceBoxScenario.setItems(choiceBoxOptions);

        // Select the first item in the list
        choiceBoxScenario.getSelectionModel().selectFirst();

        // Choice box on change
        choiceBoxScenario.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newValue) {
                // Make sure the new value is not the current scenario
                if (choiceBoxIndex != newValue.intValue()) {
                    // Load the new scenario
                    loadScenario(newValue.intValue());
                }
            }
        });

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
        //group = new Group();

        // Add the group to the main pane
        //main.getChildren().add(group);

        // Request focus
        main.requestFocus();
    }

    /**
     * Load a scenario
     *
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
        FlatlandSolver localSolver = (FlatlandSolver) solver;
        Scenario scenario = localSolver.getFlatland().getScenario(index);

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
            } else {
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
        gc.setFill(Paint.valueOf("#FFFFFF"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw the grid lines
        drawGrid();

        // Get the grid
        Cell[][] grid = runner.getGrid();

        // Draw all empties and edibles
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                // Draw each object
                drawObject(grid[y][x], x, y);
            }
        }

        // Draw the agent
        Agent agent = runner.getAgent();
        drawObject((FlatlandDrawable) agent, agent.getX(), agent.getY());
    }

    private void drawObject(FlatlandDrawable object, int x, int y) {
        // Do not draw empty cells
        if (object.getImageIndex() >= 6) return;

        double upperLeftX = x * this.gridSize;
        double upperLeftY = y * this.gridSize;
        double width = gridSize;
        double height = gridSize;

        // Scale Agent image
        switch (object.getImageIndex()) {
            case 0:
            case 1:
                upperLeftX -= 5;
                width += 10;
                break;
            case 2:
            case 3:
                upperLeftY -= 5;
                height += 10;
                break;

        }

        // Draw the objects image
        gc.drawImage(this.flatlandImages[object.getImageIndex()], upperLeftX, upperLeftY, width, height);

    }

    private void drawGrid() {
        int width = (int) canvas.getWidth();
        int height = (int) canvas.getHeight();

        gc.setStroke(new Color(0.8784313725, 0.8784313725, 0.8784313725, 1));
        gc.setLineWidth(1.0);
        for (int x = 0; x <= width; x += this.gridSize) {
            gc.strokeLine(x, 0, x, height);
        }

        for (int y = 0; y <= height; y += this.gridSize) {
            gc.strokeLine(0, y, width, y);
        }

    }

    @FXML
    @SuppressWarnings("unchecked")
    private void buttonScenarioNewClicked(Event event) {
        FlatlandSolver localSolver = (FlatlandSolver) solver;

        // Create new scenario
        localSolver.getFlatland().newScenario();

        // Add to dropdown
        if (localSolver.getFlatland().getSettings().getSetting("scenario_mode").equals("static")) {
            choiceBoxOptions.add("New scenario #" + localSolver.getFlatland().getScenarios().size());
        } else {
            choiceBoxOptions.add("New random scenario #" + localSolver.getFlatland().getScenarios().size());
        }

        // Set the items
        choiceBoxScenario.setItems(choiceBoxOptions);

        // Update selected
        choiceBoxScenario.getSelectionModel().select(choiceBoxOptions.size() - 1);

        // Load the new scenario
        loadScenario(localSolver.getFlatland().getScenarios().size() - 1);
    }


    /**
     * Toggle running state
     *
     * @param event No idea
     */

    @FXML
    public void buttonPlayPauseClicked(Event event) {
        tabPane.getSelectionModel().select(0);
        // Can I has the reuse of code plx
        togglePausePlay();
    }

    /**
     * Used to toggle pause/play from the keyboard by using space or the P-button
     */

    public void togglePausePlay() {
        running = !running;

        // Update the button textst
        if (running) {
            buttonPlayPause.setText("Pause");
        } else {
            buttonPlayPause.setText("Play");
        }
    }


    /**
     * Observer for logging stats
     */

    @Override
    public void fireLog(int generation, double max, double avg) {
        bestFitnesses[generation] = max;
        avgFitnesses[generation] = avg;
    }

    // Data to be plotted on the graph
    private double[] bestFitnesses = new double[100];
    private double[] avgFitnesses = new double[100];

    /**
     * Used to update the graph based on the values from the observer
     */

    @FXML
    private void graph() {
        // Clear all previous data
        graph.getData().clear();

        // Draw graph
        GraphHelper.populateGraph(graph, new String[]{"Max", "Average"}, bestFitnesses, avgFitnesses);


    }
}


