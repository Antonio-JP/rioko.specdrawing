package rioko.linearalg.matrix;

import rioko.linearalg.RNumber;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.linearalg.vector.RVector;
import rioko.utilities.Copiable;

public interface RMatrix<T extends RNumber> extends Copiable {
	public void setValue(T number, int row, int col) throws SizeArgumentException;
	public T getValue(int row, int col) throws SizeArgumentException;
	public void setRow(RVector<T> vector, int row) throws SizeArgumentException;
	public RVector<T> getRow(int row) throws SizeArgumentException;
	public void setColumn(RVector<T> vector, int col) throws SizeArgumentException;
	public RVector<T> getColumn(int col) throws SizeArgumentException;
	
	public int rows();
	public int columns();
	public RMatrix<T> add(RMatrix<T> matrix) throws SizeArgumentException;
	public RMatrix<T> minus(RMatrix<T> matrix) throws SizeArgumentException;
	public RMatrix<T> prod(RMatrix<T> matrix) throws SizeArgumentException;
	
	public RMatrix<T> getZero();
	public RMatrix<T> getIdentity();
	public RMatrix<T> traspose();
	
	public RVector<T> apply(RVector<T> vector) throws SizeArgumentException;
}
