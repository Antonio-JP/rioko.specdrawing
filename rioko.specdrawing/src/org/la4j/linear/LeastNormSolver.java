/*
 * Copyright 2011-2013, by Vladimir Kostyukov and Contributors.
 *
 * This file is part of la4j project (http://la4j.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor(s): Maxim Samoylov
 *
 */

package org.la4j.linear;


import org.la4j.LinearAlgebra;
import org.la4j.Matrix;
import org.la4j.Vector;

/**
 * This class provides solution of "fat" linear system with least euclidean norm.
 * See details
 * <p>
 * <a href="http://see.stanford.edu/materials/lsoeldsee263/08-min-norm.pdf">here.</a>
 * </p>
 */

public class LeastNormSolver extends AbstractSolver implements LinearSystemSolver {

	private static final long serialVersionUID = 1L;

	protected LeastNormSolver(Matrix a) {
        super(a);
    }

    @Override
    public Vector solve(Vector b) {
        ensureRHSIsCorrect(b);

        Matrix temp = self().multiply(self().rotate());
        Matrix pseudoInverse = self().rotate().multiply(temp.withInverter(LinearAlgebra.InverterFactory.GAUSS_JORDAN).inverse());

        return pseudoInverse.multiply(b);
    }

    @Override
    public boolean applicableTo(Matrix matrix) {
        //TODO: we need to think about how to improve the speed here.
        //Note: Matrix.rank() works for O(N^3) which is quite slow.
        int r = matrix.rank();
        return (r == matrix.rows() || r == matrix.columns());
    }
}
