package org.thomas.project3.ann.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class Softmax implements ANNFunctionInterface {

    /**
     * Softmax function
     * @param matrix Matrix to apply function for
     * @param derived Indicates if the function is derived or not
     * @return The matrix after the function has been applied
     */

    public static DoubleMatrix apply(DoubleMatrix matrix, boolean derived) {
        // Return the applied matrix
        return MatrixFunctions.exp(matrix).div(MatrixFunctions.exp(matrix).sum());
    }
}