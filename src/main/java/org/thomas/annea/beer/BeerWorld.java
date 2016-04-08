package org.thomas.annea.beer;

import org.thomas.annea.tools.RandomHelper;
import org.thomas.annea.tools.settings.AbstractSettings;

import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BeerWorld {

    // Games modes
    public final static int STANDARD = 0;
    public final static int PULL = 1;
    public final static int NOWRAP = 2;

    // Reference to the settings
    private AbstractSettings settings;

    // List of all the objects
    private List<Pair<Integer, Integer>> objects;

    // The location of the tracker
    private int trackerLocation;

    /**
     * Constructor
     * @param s Instance of settings
     */

    public BeerWorld(AbstractSettings s) {
        settings = s;
    }

    /**
     * Create the various locations of the objects and their random sizes
     */

    public void initialize() {
        // Create the list that holds all the objects
        objects = new ArrayList<>();

        // Create each object
        for (int i = 0; i < 700; i++) {
            // Get the random size of the object
            int objectSize = RandomHelper.randint(1, 7);

            // Get the random position of the object
            int objectLocation = RandomHelper.randint(0, 30 - objectSize);

            // Add the new pair
            objects.add(new Pair<>(objectSize, objectLocation));
        }

        // Get the random position of the tracker
        trackerLocation = RandomHelper.randint(0, 25);
    }

    public AbstractSettings getSettings() {
        return settings;
    }

    public List<BeerObject> getObjects() {
        // Create a list to hold the new objects
        List<BeerObject> objectList = new ArrayList<>();

        // Loop the objects
        for (int i = 0; i < objects.size(); i++) {
            // Get the pair
            Pair<Integer, Integer> pair = objects.get(i);

            // Get the values from the pair
            int size = pair.fst;
            int location = pair.snd;

            // Create a new object and add to list
            objectList.add(new BeerObject(this, size, location));
        }

        // Return the list of objects
        return objectList;
    }

    public Tracker getTracker() {
        // Return the new tracker
        return new Tracker(this, trackerLocation);
    }
}
