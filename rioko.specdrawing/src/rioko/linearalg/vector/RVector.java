package rioko.linearalg.vector;

import rioko.linearalg.RNumber;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.utilities.Copiable;

public interface RVector<T extends RNumber> extends Copiable {
	
	public void setValue(T number, int pos) throws SizeArgumentException;
	public T getValue(int pos) throws SizeArgumentException;
	
	public int size();
	public RVector<T> add(RVector<T> toAdd) throws SizeArgumentException;
	public RVector<T> minus(RVector<T> toRemove) throws SizeArgumentException;
	public RVector<T> scalar(T number);
	public T dot(RVector<T> vec) throws SizeArgumentException;
	public double norm();
}
