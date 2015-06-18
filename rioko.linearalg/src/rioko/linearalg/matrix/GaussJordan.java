package rioko.linearalg.matrix;

import rioko.linearalg.RNumber;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.linearalg.exceptions.ZeroNumberException;
import rioko.linearalg.vector.RVector;

@Deprecated
public class GaussJordan <T extends RNumber> {
	private RMatrix<T> matrix;
	
	private boolean isRunned = false;
	
	@SuppressWarnings("unchecked")
	public GaussJordan(RMatrix<T> matrix) {
		this.matrix = (RMatrix<T>) matrix.copy();
	}
	
	public void run() {
		if(!isRunned) {
			this.Gauss();
			this.Jordan();
		}
	}

	//Private calculus methods
	private void Jordan() {
		//TODO Implement this method
	}

	@SuppressWarnings("unchecked")
	private void Gauss() {
		try{
			for(int i = 0, row = 0; i < Integer.min(this.matrix.rows(), this.matrix.columns()); i++) {
				//Nos aseguramos que $(i,i) != 0$
				boolean found = false;
				if(this.matrix.getValue(row, i).isZero()) {
					for(int j=row+1; j<this.matrix.rows(); j++) {
						if(!this.matrix.getValue(j, i).isZero()) {
							this.swapRows(i, j);
							found = true;
							break;
						}
					}
				} else {
					found = true;
				}
				
				//Si hay un número en la columna i, entonces hacemos más
				if(found) {
					//Escalamos la fila para tener un 1 en la posición deseada
					this.scalarRow((T) this.matrix.getValue(i, i).inverse(), i);
					
					//Restamos en el resto para obtener ceros
					for(int j = i+1; j< this.matrix.rows(); j++) {
						this.matrix.setRow(this.matrix.getRow(j).minus(this.matrix.getRow(i).scalar(this.matrix.getValue(j, i))), j);
					}
					
					row++;
				}
			}
		} catch(SizeArgumentException | ZeroNumberException e) {
			//Impossible Exception
			e.printStackTrace();
		}
	}
	
	//Private basic operations
	private void scalarRow(T number, int row) throws SizeArgumentException {
		this.matrix.setRow(this.matrix.getRow(row).scalar(number), row);
	}
	
	private void swapRows(int row1, int row2) throws SizeArgumentException {
		if(row1 == row2) {
			return;
		} else {
			RVector<T> aux = this.matrix.getRow(row1);
			this.matrix.setRow(this.matrix.getRow(row2), row1);
			this.matrix.setRow(aux, row2);
			
			return;
		}
	}
}
