package org.thomas.annea.ann;

import org.jblas.DoubleMatrix;

public class CTRLayer extends Layer {

    private DoubleMatrix y;

    /**
     * Constructor for empty layer
     * @param output Ouput size
     */

    public CTRLayer(int output) {
        super(output);
    }

    /**
     * Constructor
     * @param input Input size
     * @param output Output size
     */

    public CTRLayer(int input, int output) {
        super(input, output);
    }

    public double getY() {

    }
}
