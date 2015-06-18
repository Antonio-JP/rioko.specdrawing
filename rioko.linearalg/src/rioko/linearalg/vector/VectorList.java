package rioko.linearalg.vector;

import java.util.ArrayList;

import rioko.linearalg.RNumber;
import rioko.linearalg.exceptions.BadClassArgumentException;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.utilities.Copiable;

public class VectorList<T extends RNumber> implements RVector<T> {

	private ArrayList<T> vector = new ArrayList<>();

	//Builders
	public VectorList(T[] vector) throws SizeArgumentException {
		if(vector.length == 0) {
			throw new SizeArgumentException("Empty vector not allowed");
		}
		for(T number : vector) {
			this.vector.add(number);
		}
	}
	
	public VectorList(ArrayList<T> vector) throws SizeArgumentException {
		if(vector.size() == 0) {
			throw new SizeArgumentException("Empty vector not allowed");
		}
		
		this.vector = vector;
	}
	
	@SuppressWarnings("unchecked")
	public VectorList(T type, int size) {
		for(int i = 0; i < size; i++) {
			this.vector.add((T) type.getZero());
		}
	}
	
	//RVector methods
	@Override
	public void setValue(T number, int pos) throws SizeArgumentException {
		if(pos < 0 || pos >= this.size()) {
			throw new SizeArgumentException("Bad position asked");
		} else {
			this.vector.remove(pos);
			this.vector.add(pos, number);
		}
	}
	@Override
	public T getValue(int pos) throws SizeArgumentException {
		if(pos < 0 || pos >= this.size()) {
			throw new SizeArgumentException("Bad position asked");
		} else {
			return this.vector.get(pos);
		}
	}
	
	@Override
	public int size() {
		return vector.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public RVector<T> add(RVector<T> toAdd) throws SizeArgumentException {
		ArrayList<T> res = new ArrayList<>();
		
		for(int pos = 0; pos < this.size(); pos++) {
			try {
				res.add((T) this.getValue(pos).add(toAdd.getValue(pos)));
			} catch (BadClassArgumentException e) {
				// Impossible Exception
				e.printStackTrace();
				return null;
			}
		}
		
		return new VectorList<T>(res);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RVector<T> minus(RVector<T> toRemove) throws SizeArgumentException {
		ArrayList<T> res = new ArrayList<>();
		
		for(int pos = 0; pos < this.size(); pos++) {
			try {
				res.add((T) this.getValue(pos).minus(toRemove.getValue(pos)));
			} catch (BadClassArgumentException e) {
				// Impossible Exception
				e.printStackTrace();
				return null;
			}
		}
		
		return new VectorList<T>(res);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RVector<T> scalar(T number) {
		ArrayList<T> res = new ArrayList<>();
		
		for(T el : this.vector) {
			try {
				res.add((T) el.prod(number));
			} catch (BadClassArgumentException e) {
				// Impossible Exception
				e.printStackTrace();
				return null;
			}
		}
		
		try {
			return new VectorList<T>(res);
		} catch (SizeArgumentException e) {
			// Impossible Exception
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public T dot(RVector<T> vec) throws SizeArgumentException {
		if(this.size() != vec.size()) {
			throw new SizeArgumentException("Bad size of the second vector");
		}
		
		@SuppressWarnings("unchecked")
		T res = (T) this.vector.get(0).getZero();
		
		for(int i = 0; i < this.size(); i++) {
			try {
				res.add(this.vector.get(i).prod(vec.getValue(i)));
			} catch (BadClassArgumentException e) {
				// Impossible Exception
				e.printStackTrace();
			}
		}
		
		return res;
	}

	@Override
	public double norm() {		
		Double res = 0.0;
		for(int i = 0; i < this.size(); i++) {
			double norm = this.vector.get(i).norm();
			res += norm*norm;
		}
		
		return Math.sqrt(res);
	}
	
	//Other methods
	@Override
	public String toString() {
		String res = "[";
		
		for(int i = 0; i < this.size() - 1; i++) {
			res += this.vector.get(i).toString() + ", ";
		}
		
		res += this.vector.get(this.size()-1).toString() + "]";
		
		return res;
	}

	//Copiable methods
	@SuppressWarnings("unchecked")
	@Override
	public Copiable copy() {
		ArrayList<T> copy = new ArrayList<>();
		
		for(T number : this.vector) {
			copy.add((T) number.copy());
		}
		
		try {
			return new VectorList<T>(copy);
		} catch (SizeArgumentException e) {
			// Impossible Exception
			e.printStackTrace();
			return null;
		}
	}

}
