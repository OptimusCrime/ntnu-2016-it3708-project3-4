package org.annea.controller;

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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.annea.ann.*;
import org.annea.ea.fitness.BeerFitness;
import org.annea.runner.BeerProblemRunner;
import org.annea.tools.settings.BeerSettings;
import org.jblas.DoubleMatrix;
import org.annea.beer.BeerObject;
import org.annea.beer.BeerWorld;
import org.annea.beer.Tracker;
import org.annea.ea.gtype.AbstractGType;
import org.annea.solvers.AbstractSolver;
import org.annea.solvers.BeerSolver;
import org.annea.tools.settings.AbstractSettings;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BeerController extends AbstractController implements Initializable {

    // JavaFX stuff
    @FXML
    private Pane main;

    @FXML
    private Slider sliderRefreshRate;

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    @FXML
    private Button buttonPlayPause;

    @FXML
    private Label labelRefreshRate;
    @FXML
    private Label labelTimestep;
    @FXML
    private Label labelCapture;
    @FXML
    private Label labelAvoidance;
    @FXML
    private Label labelCorrectFail;

    // Size TODO: get from config
    public final int OBJECTSIZE = 40;

    // Various dropdown stuff
    private int choiceBoxIndex;
    private ObservableList choiceBoxOptions;

    // Length of each tick
    private static int tickLength = 30;

    // Reference to the solver
    private AbstractSolver solver;

    // For the simulations
    private static BeerProblemRunner runner;

    // For the timeline
    private Timeline timeline;

    // For replay
    private boolean finished;

    /**
     * Constructor
     *
     * @param s Instance of Settings
     */

    public BeerController(AbstractSettings s) {
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
        // Grab graphics context
        gc = canvas.getGraphicsContext2D();

        populateGui();

        // New solver
        solver = new BeerSolver(settings);

        // Solve the problem
        solver.solve(null);

        // Print stats
        //printStats(solver);

        // Propagate input
        //propagateInput(solver);

        // Start the interval
        startInterval();

        // Populate the gui with the various things needed
        populateGui();

        // Load the first scenario
        loadWorld();
    }

    private void printStats(AbstractSolver solver) {
        CTRNetwork network = (CTRNetwork) solver.getNetwork();
        Layer[] layers = network.getLayers();

        for (int i = 0; i < layers.length; i++) {
            CTRLayer thisLayer = (CTRLayer) layers[i];
            System.out.println("Layer #" + i);
            System.out.println(thisLayer.getRows() + ", " + thisLayer.getColumns());
            System.out.println("");

            List<CTRNeuron> neurons = thisLayer.getNeurons();
            for (int j = 0; j < neurons.size(); j++) {
                System.out.println("Neuron " + j);
                System.out.println("Weights: ");
                double[] weights = neurons.get(j).getWeights();
                for (int k = 0; k < weights.length; k++) {
                    System.out.println(weights[k]);
                }

                double bias = neurons.get(j).getBias();
                System.out.println("Bias: " + bias);

                System.out.println("Other weights: ");
                double[] otherWeights = neurons.get(j).getOtherWeights();
                for (int k = 0; k < otherWeights.length; k++) {
                    System.out.println(otherWeights[k]);
                }

                double gain = neurons.get(j).getGain();
                System.out.println("Gain: " + gain);

                double timeConstant = neurons.get(j).getTimeConstant();
                System.out.println("Time constant: " + timeConstant);
            }
        }
    }

    private void propagateInput(AbstractSolver solver) {
        CTRNetwork network = (CTRNetwork) solver.getNetwork();
        Layer[] layers = network.getLayers();
        CTRLayer layerOne = (CTRLayer) layers[0];
        CTRLayer layerTwo = (CTRLayer) layers[1];
        CTRLayer layerThree = (CTRLayer) layers[2];

        // Layer one
        List<CTRNeuron> layerOneNeuron = layerOne.getNeurons();
        layerOneNeuron.get(0).setWeights(new double[]{-3.863, -0.686, 1.549, 2.059, -3.980});
        layerOneNeuron.get(0).setOtherWeights(new double[]{2.451, -3.196, -3.353});
        layerOneNeuron.get(0).setBias(-7.176);
        layerOneNeuron.get(0).setGain(2.475);
        layerOneNeuron.get(0).setTimeConstant(1.435);

        layerOneNeuron.get(1).setWeights(new double[]{-4.137, 2.451, 3.980, -0.608, -2.843});
        layerOneNeuron.get(1).setOtherWeights(new double[]{2.059, 1.549, 4.333});
        layerOneNeuron.get(1).setBias(-0.902);
        layerOneNeuron.get(1).setGain(4.765);
        layerOneNeuron.get(1).setTimeConstant(1.349);

        layerOneNeuron.get(2).setWeights(new double[]{2.647, 1.745, -3.941, -1.157, -0.804});
        layerOneNeuron.get(2).setOtherWeights(new double[]{-2.686, 1.353, 0.882});
        layerOneNeuron.get(2).setBias(-6.745);
        layerOneNeuron.get(2).setGain(4.859);
        layerOneNeuron.get(2).setTimeConstant(1.071);

        // Layer two
        List<CTRNeuron> layerTwoNeuron = layerTwo.getNeurons();
        layerTwoNeuron.get(0).setWeights(new double[]{0.647, 2.373, 1.902});
        layerTwoNeuron.get(0).setOtherWeights(new double[]{3.157, 4.569});
        layerTwoNeuron.get(0).setBias(-9.608);
        layerTwoNeuron.get(0).setGain(1.376);
        layerTwoNeuron.get(0).setTimeConstant(1.682);

        layerTwoNeuron.get(1).setWeights(new double[]{1.314, 3.235, -3.274});
        layerTwoNeuron.get(1).setOtherWeights(new double[]{-4.098, -2.0196});
        layerTwoNeuron.get(1).setBias(-0.275);
        layerTwoNeuron.get(1).setGain(2.961);
        layerTwoNeuron.get(1).setTimeConstant(1.137);

        // Layer three
        List<CTRNeuron> layerThreeNeuron = layerThree.getNeurons();
        layerThreeNeuron.get(0).setWeights(new double[]{1.0, 3.980});
        layerThreeNeuron.get(0).setOtherWeights(new double[]{-3.980, -0.529});
        layerThreeNeuron.get(0).setBias(-0.275);
        layerThreeNeuron.get(0).setGain(1.235);
        layerThreeNeuron.get(0).setTimeConstant(1.788);

        layerThreeNeuron.get(1).setWeights(new double[]{-4.333, -1.941});
        layerThreeNeuron.get(1).setOtherWeights(new double[]{-1.0, 4.843});
        layerThreeNeuron.get(1).setBias(-0.275);
        layerThreeNeuron.get(1).setGain(-8.706);
        layerThreeNeuron.get(1).setTimeConstant(1.137);

        DoubleMatrix output1 = network.propagate(new DoubleMatrix(new double[][]{{1, 0, 0, 0, 0}}));
        DoubleMatrix output2 = network.propagate(new DoubleMatrix(new double[][]{{1, 0, 0, 0, 0}}));
        DoubleMatrix output3 = network.propagate(new DoubleMatrix(new double[][]{{1, 0, 0, 0, 0}}));
        DoubleMatrix output4 = network.propagate(new DoubleMatrix(new double[][]{{1, 0, 0, 0, 0}}));
        DoubleMatrix output5 = network.propagate(new DoubleMatrix(new double[][]{{1, 0, 0, 0, 0}}));

        System.out.println(output1);
        System.out.println(output2);
        System.out.println(output3);
        System.out.println(output4);
        System.out.println(output5);
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
        // Set various slider things
        sliderRefreshRate.setMin(10);
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

        // Request focus
        main.requestFocus();
    }

    /**
     * Loads the world
     */

    private void loadWorld() {
        // Make sure to pause the player
        running = false;

        // Set the button text and disabled state
        buttonPlayPause.setDisable(false);
        buttonPlayPause.setText("Play");

        // Reset the timestep
        labelTimestep.setText("1");

        // Get the beer world
        BeerSolver localSolver = (BeerSolver) solver;
        BeerWorld beerWorld = localSolver.getBeerWorld();

        // Generate a new world
        beerWorld.generate();

        // Set up the network
        Network ann = solver.getNetwork();

        // Get the best runner from the EA
        AbstractGType bestIndividual = solver.getBestIndividual();

        BeerFitness.calculateFitness(bestIndividual);
        System.out.println("Running Visualisation with fitness: " + bestIndividual.getFitness());

        // Apply the weights from the best individual to the ANN
        ann.setWeights(bestIndividual);

        // Create a new instance of the runner
        runner = new BeerProblemRunner(settings);
        runner.setNetwork(solver.getNetwork());
        runner.setObjects(beerWorld.getObjects());
        runner.setTracker(beerWorld.getTracker());

        // Update eaten stats
        labelCapture.setText("0");
        labelAvoidance.setText("0");
        labelCorrectFail.setText("0 / 0");

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
                finished = true;

                // Set button to replay
                buttonPlayPause.setText("Restart");
            }
            else {
                // Update the current tick
                labelTimestep.setText(Integer.toString(runner.getTimestep()));

                // Update stats
                BeerSettings beerSettings = (BeerSettings) settings;
                if (beerSettings.getMode() == BeerWorld.PULL) {
                    labelCapture.setText(Integer.toString(runner.getCapture()));
                    labelAvoidance.setText(Integer.toString(runner.getAvoidance()));
                }
                else {
                    labelCapture.setText(Integer.toString(runner.getCapture()) + " / " + Integer.toString(runner.getOptimalCapture()));
                    labelAvoidance.setText(Integer.toString(runner.getAvoidance()) + " / " + Integer.toString(runner.getOptimalAvoidance()));
                }
                labelCorrectFail.setText(Integer.toString(runner.getCorrect()) + " / " + Integer.toString(runner.getWrong()));

                // Draw the tick
                draw();
            }
        }
    }

    /**
     * Draw the graphics
     */

    private void draw() {
        // Clear the previous
        gc.setFill(Paint.valueOf("#FFFFFF"));
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw grid
        drawGrid();

        // Draw the current object
        BeerObject beerObject = runner.getCurrentObject();
        if (beerObject != null) {
            drawObject(beerObject);

            // Set reference to the tracker
            runner.getTracker().setTrackerReference(beerObject);
        } else {
            // Empty the reference to the object for the tracker
            runner.getTracker().setTrackerReference(null);
        }

        // Draw the tracker
        drawTracker(runner.getTracker());
    }

    private void drawObject(BeerObject object) {
        // Get the various locations
        int x = object.getLocation() * OBJECTSIZE;
        int y = (14 - object.getRow()) * OBJECTSIZE;
        int height = OBJECTSIZE;
        int width = object.getSize() * OBJECTSIZE;

        // Colorize
        if (object.getSize() <= 4) {
            gc.setFill(Color.BLUE);
        }
        else {
            gc.setFill(Color.RED);
        }

        // Draw the rect
        gc.fillRoundRect(x, y, width, height, 15, 15);
    }

    private void drawTracker(Tracker tracker) {
        // Draw the tracker body
        gc.setFill(Paint.valueOf(tracker.getColor()));
        int x = tracker.getLocation() * OBJECTSIZE;
        int y = OBJECTSIZE * 14;
        int height = OBJECTSIZE;
        int width = OBJECTSIZE * 5;
        gc.fillRoundRect(x, y, width, height, 15, 15);

        if (tracker.getLocation() > 25) {
            int trackerWrapWidth = (tracker.getLocation() - 25) * OBJECTSIZE;
            gc.fillRoundRect(0, y, trackerWrapWidth, height, 15, 15);
        }


        // Get sensor readings
        DoubleMatrix input = null;
        if (tracker.getBeerObjectReference() != null) {
            // Set the input correctly
            input = tracker.getInput(tracker.getBeerObjectReference());
        }

        int[] trackerLocation = tracker.getTrackerLocation();

        // Draw the sensors if activated
        for (int i = 0; i < trackerLocation.length; i++) {
            // Get some initial locations
            int sensorX = trackerLocation[i] * OBJECTSIZE;
            int sensorY = 14 * OBJECTSIZE;

            if (input != null && (int) input.get(0, i) == 1) {
                gc.setFill(Paint.valueOf("#dbdb4c"));
                // Draw the oval
                gc.fillOval(sensorX + 15, sensorY + 15, 10, 10);
            }
        }
    }

    private void drawGrid() {
        double widthRatio = canvas.getWidth() / 30;
        double heightRatio = canvas.getHeight() / 15;

        gc.setStroke(Paint.valueOf("#b2d9d9"));
        gc.setLineWidth(0.5);
        for (double x = 0; x <= canvas.getWidth(); x += widthRatio) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }

        for (double y = 0; y <= canvas.getHeight(); y += heightRatio) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }
    }

    /**
     * Toggle running state
     *
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

        // Create new random if restart
        if (finished) {
            loadWorld();
            finished = false;
        }

        running = !running;

        // Update the button text
        if (running) {
            buttonPlayPause.setText("Pause");
        } else {
            buttonPlayPause.setText("Play");
        }
    }
}
