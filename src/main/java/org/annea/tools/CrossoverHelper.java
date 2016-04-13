package org.annea.tools;

import org.annea.tools.settings.AbstractSettings;

import java.util.*;

public class CrossoverHelper {

    /**
     * Calculate crossover, taking into account the internal representation of the bit vector en sure valid options only
     * @param settings Instance of settings
     * @return Array of the crossover points
     */

    public static int[] getCrossover(AbstractSettings settings) {
        // Find out how many crossovers we should do
        int numberOfCrossovers = 1;
        if (settings.getNumberOfValues() > 3 && RandomHelper.randomInPercentage(settings.getProbabilityDoubleCrossover())) {
            numberOfCrossovers = 2;
        }

        // Array of possible crossovers
        int []crossovers = new int[numberOfCrossovers + 2];
        crossovers[0] = 0;
        crossovers[numberOfCrossovers + 2 - 1] = settings.getNumberOfValues() - 1;

        // Generate possible crossovers
        for (int i = 1; i < numberOfCrossovers + 1; i++) {
            // Loop until we find a unique value here
            while (true) {
                // Get the current crossover value
                int crossoverValue = RandomHelper.randint(1, settings.getNumberOfValues());

                // Make sure that the value is not present
                boolean found = false;
                for (int j = 0; j < crossovers.length; j++) {
                    if (crossovers[j] == crossoverValue) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // Set this value
                    crossovers[i] = crossoverValue;
                    break;
                }
            }
        }

        // Sort the array
        if (crossovers.length > 2) {
            Arrays.sort(crossovers);
        }

        // Return the list of crossovers
        return crossovers;
    }
}
