package org.annea.ann.functions;

import org.jblas.DoubleMatrix;

public class Argmax implements ANNFunctionInterface {

    /**
     * Argmax function
     * @param matrix Matrix to apply function for
     * @param derived Indicates if the function is derived or not
     * @return The matrix after the function has been applied
     */

    public static DoubleMatrix apply(DoubleMatrix matrix, boolean derived) {
        return new DoubleMatrix(1, 1, matrix.argmax());
    }
}
