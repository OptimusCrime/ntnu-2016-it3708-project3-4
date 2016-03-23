package org.thomas.project3.ann.functions;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public class Sigmoid implements ANNFunctionInterface {

    /**
     * Sigmoid function
     * @param matrix Matrix to apply function for
     * @param derived Indicates if the function is derived or not
     * @return The matrix after the function has been applied
     */

    public static DoubleMatrix apply(DoubleMatrix matrix, boolean derived) {
        // Methods are different for derived matrices
        if (derived) {
            return matrix.mul(matrix.sub(1).neg());
        }

        // Return the applied matrix
        return MatrixFunctions.exp(matrix.neg()).add(1).rdiv(1);
    }
}
