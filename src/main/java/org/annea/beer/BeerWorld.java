package org.annea.beer;

import org.annea.tools.RandomHelper;
import org.annea.tools.settings.AbstractSettings;

import java.util.ArrayList;
import java.util.List;

public class BeerWorld {

    // Games modes
    public final static int STANDARD = 0;
    public final static int PULL = 1;
    public final static int NOWRAP = 2;

    // Reference to the settings
    private AbstractSettings settings;

    /**
     * Constructor
     * @param s Instance of settings
     */

    public BeerWorld(AbstractSettings s) {
        settings = s;
    }

    public AbstractSettings getSettings() {
        return settings;
    }

    public List<BeerObject> getObjects() {
        // Create a list to hold the new objects
        List<BeerObject> objectList = new ArrayList<>();

        // Loop the objects
        for (int i = 0; i < 600; i++) {
            // Get the pair
            int objectSize = RandomHelper.randint(1, 7);

            // Get the random position of the object
            int objectLocation = RandomHelper.randint(0, 30 - objectSize);

            // Create a new object and add to list
            objectList.add(new BeerObject(this, objectSize, objectLocation));
        }

        // Return the list of objects
        return objectList;
    }

    public Tracker getTracker() {
        // Return the new tracker
        return new Tracker(this, RandomHelper.randint(0, 25));
    }
}
