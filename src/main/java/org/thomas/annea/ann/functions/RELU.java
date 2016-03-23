package org.thomas.annea.ann.functions;

import org.jblas.DoubleMatrix;

public class RELU implements ANNFunctionInterface {

    /**
     * RELU function
     * @param matrix Matrix to apply function for
     * @param derived Indicates if the function is derived or not
     * @return The matrix after the function has been applied
     */

    public static DoubleMatrix apply(DoubleMatrix matrix, boolean derived) {
        // Duplicate the matrix
        DoubleMatrix newMatrix = matrix.dup();

        // Loop the entire matrix
        for (int y = 0; y < matrix.rows; y++) {
            for (int x = 0; x < matrix.columns; x++) {
                newMatrix.put(y, x, Math.max(0, matrix.get(y, x)));
            }
        }

        // Return the new matrix
        return newMatrix;
    }
}
