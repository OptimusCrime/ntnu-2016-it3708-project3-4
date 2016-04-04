package org.thomas.annea.ann;


import org.thomas.annea.tools.settings.AbstractSettings;

public class CTRNetwork extends Network {

    /**
     * Constructor
     * @param s Instance of settings
     */

    public CTRNetwork(AbstractSettings s) {
        super(s);
    }

    /**
     * Create function. Creates the network based on the settings fetched from the config file
     */

    public void create() {
        int[] dimensions = settings.getNetworkDimensions();
        String[] functions = settings.getNetworkFunctions();

        // Create the layer array of correct length
        layers = new CTRLayer[dimensions.length];

        // Now set the correct sizes for each of the layer
        for (int i = 1; i < layers.length; i++) {
            layers[i - 1] = new CTRLayer(dimensions[i - 1], dimensions[i]);

            // Set the function
            if (functions.length >= i) {
                layers[i - 1].setFunction(functions[i - 1]);
            }
            else {
                layers[i - 1].setFunction("Sigmoid");
            }
        }

        // Create output layer, which is empty
        layers[layers.length - 1] = new CTRLayer(dimensions[dimensions.length - 1]);

        // Set the correct output function
        layers[layers.length - 1].setFunction(functions[functions.length - 1]);
    }
}
