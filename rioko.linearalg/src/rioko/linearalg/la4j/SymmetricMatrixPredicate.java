package rioko.linearalg.la4j;

import org.la4j.Matrix;
import org.la4j.matrix.functor.AdvancedMatrixPredicate;

public class SymmetricMatrixPredicate implements AdvancedMatrixPredicate {

	@Override
	public boolean test(Matrix matrix) {
		if(matrix.rows() == matrix.columns()) {
			for(int i = 1; i < matrix.rows(); i++) {
				for(int j = 0; j<i; j++) {
					if(matrix.get(i, j) != matrix.get(j, i)) {
						return false;
					}
				}
			}
			
			return true;
		}
		
		return false;
	}

}
