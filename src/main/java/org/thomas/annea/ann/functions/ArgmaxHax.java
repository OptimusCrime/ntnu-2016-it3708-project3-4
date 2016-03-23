package org.thomas.annea.ann.functions;

import org.jblas.DoubleMatrix;

public class ArgmaxHax implements ANNFunctionInterface {

    /**
     * ArgmaxHax function
     * @param matrix Matrix to apply function for
     * @param derived Indicates if the function is derived or not
     * @return The matrix after the function has been applied
     */

    public static DoubleMatrix apply(DoubleMatrix matrix, boolean derived) {
        // Get the correct argmax value
        double argmax = matrix.argmax();

        // Check if we need the hax
        if ((int) argmax == 0) {
            // Compare the first and second param
            if (Math.abs(matrix.get(0, 0) - matrix.get(0, 1)) < 1e-4) {
                // The first and second param are the same, override the value
                argmax = 1.0;
            }
        }

        // Return the "argmax" matrix
        return new DoubleMatrix(1, 1, argmax);
    }
}
