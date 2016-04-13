package org.annea.ann.functions;

import org.jblas.DoubleMatrix;

interface ANNFunctionInterface {

    /**
     * Various ANN functions
     * @param matrix Matrix to apply function for
     * @param derived Indicates if the function is derived or not
     * @return The matrix after the function has been applied
     */

    static DoubleMatrix apply(DoubleMatrix matrix, boolean derived) {
        return matrix;
    }
}
