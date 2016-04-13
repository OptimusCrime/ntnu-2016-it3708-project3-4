package org.annea.ann;

import java.util.ArrayList;
import java.util.List;

public class CTRLayer extends Layer {

    private List<CTRNeuron> neurons;

    /**
     * Constructor for empty layer
     * @param output Ouput size
     */

    public CTRLayer(int output) {
        super(output);

        // Create the layer
        createLayer(output);
    }

    /**
     * Constructor
     * @param input Input size
     * @param output Output size
     */

    public CTRLayer(int input, int output) {
        super(input, output);

        // Create the layer
        createLayer(output);
    }

    public void reset() {
        createLayer(columns);
    }

    private void createLayer(int size) {
        neurons = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            neurons.add(new CTRNeuron());
        }
    }

    public List<CTRNeuron> getNeurons() {
        return neurons;
    }
}
