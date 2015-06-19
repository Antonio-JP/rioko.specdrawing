package rioko.linearalg.matrix;

import java.util.ArrayList;

import rioko.linearalg.RNumber;
import rioko.linearalg.exceptions.BadClassArgumentException;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.linearalg.vector.RVector;
import rioko.linearalg.vector.VectorList;
import rioko.utilities.Copiable;

public class VectorMatrix<T extends RNumber> implements RMatrix<T> {
	
	ArrayList<RVector<T>> matrix = new ArrayList<>();
	
	//Builders
	public VectorMatrix(T[][] matrix) throws SizeArgumentException {
		if(matrix.length == 0) {
			throw new SizeArgumentException("Empty matrix not allowed");
		}
		
		for(T[] row : matrix) {
			this.matrix.add(new VectorList<T>(row));
		}
	}
	
	public VectorMatrix(ArrayList<RVector<T>> matrix) throws SizeArgumentException {
		if(matrix.size() == 0) {
			throw new SizeArgumentException("Empty matrix not allowed");
		}
		
		int columns = -1;
		
		for(RVector<T> row : matrix) {
			if(columns == -1) {
				columns = row.size();
			}
			
			if(columns != row.size()) {
				throw new SizeArgumentException("Different size between rows of the matrix");
			}
			
			matrix.add(row);
		}
	}
	
	public VectorMatrix(T type, int rows, int columns) throws SizeArgumentException {
		if(rows == 0 || columns == 0) {
			throw new SizeArgumentException("Empty matrix not allowed");
		}
		for(int i = 0; i < rows; i ++) {
			this.matrix.add(new VectorList<T>(type,columns));
		}
	}

	@Override
	public void setValue(T number, int row, int col) throws SizeArgumentException {
		if(row < 0 || row >= this.rows()) {
			throw new SizeArgumentException("Bad row size");
		} else if(col < 0 || col >=this.columns()) {
			throw new SizeArgumentException("Bad column size");
		}
		
		this.matrix.get(row).setValue(number, col);
	}

	@Override
	public T getValue(int row, int col) throws SizeArgumentException {
		if(row < 0 || row >= this.rows()) {
			throw new SizeArgumentException("Bad row size");
		} else if(col < 0 || col >=this.columns()) {
			throw new SizeArgumentException("Bad column size");
		}
		
		return this.matrix.get(row).getValue(col);
	}

	@Override
	public void setRow(RVector<T> vector, int row) throws SizeArgumentException {
		if(vector.size() != this.columns()) {
			throw new SizeArgumentException("Bad size for the new vector");
		} else if(row < 0 || row >= this.rows()) {
			throw new SizeArgumentException("Bad row to change");
		}
		
		this.matrix.remove(row);
		this.matrix.add(row, vector);
	}

	@Override
	public RVector<T> getRow(int row) throws SizeArgumentException {
		if(row < 0 || row >= this.rows()) {
			throw new SizeArgumentException("Bad row size");
		} 
		
		return this.matrix.get(row);
	}

	@Override
	public void setColumn(RVector<T> vector, int col) throws SizeArgumentException {
		if(vector.size() != this.rows()) {
			throw new SizeArgumentException("Bad size for the new vector");
		} else if(col < 0 || col >= this.rows()) {
			throw new SizeArgumentException("Bad row to change");
		}
		
		for(int i = 0; i < this.rows(); i++) {
			this.setValue(vector.getValue(i), i, col);
		}
	}

	@Override
	public RVector<T> getColumn(int col) throws SizeArgumentException {
		if(col < 0 || col >=this.columns()) {
			throw new SizeArgumentException("Bad column size");
		}
		
		RVector<T> res = new VectorList<T>(this.getValue(0, 0), this.rows());
		for(int row = 0; row < this.rows(); row++) {
			res.setValue(this.getValue(row, col), row);
		}
		
		return res;
	}

	@Override
	public int rows() {
		return this.matrix.size();
	}

	@Override
	public int columns() {
		return this.matrix.get(0).size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public VectorMatrix<T> add(RMatrix<T> matrix) throws SizeArgumentException {
		VectorMatrix<T> res = new VectorMatrix<>(this.getValue(0, 0), this.rows(), this.columns());
		
		for(int row = 0; row < this.rows(); row++) {
			for(int col = 0; col < this.columns(); col++) {
				try {
					res.setValue((T)this.getValue(row, col).add(matrix.getValue(row, col)), row, col);
				} catch (BadClassArgumentException e) {
					// Impossible Exception
					e.printStackTrace();
					return null;
				}
			}
		}
		
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RMatrix<T> minus(RMatrix<T> matrix) throws SizeArgumentException {
		VectorMatrix<T> res = new VectorMatrix<>(this.getValue(0, 0), this.rows(), this.columns());
		
		for(int row = 0; row < this.rows(); row++) {
			for(int col = 0; col < this.columns(); col++) {
				try {
					res.setValue((T)this.getValue(row, col).minus(matrix.getValue(row, col)), row, col);
				} catch (BadClassArgumentException e) {
					// Impossible Exception
					e.printStackTrace();
					return null;
				}
			}
		}
		
		return res;
	}

	@Override
	public VectorMatrix<T> prod(RMatrix<T> matrix) throws SizeArgumentException {
		if(this.columns() != matrix.rows()) {
			throw new SizeArgumentException("Bad dimensions for matrix product");
		}
		
		VectorMatrix<T> res = new VectorMatrix<>(this.getValue(0, 0), this.rows(), this.columns());
		
		for(int row = 0; row < this.rows(); row++) {
			for(int col = 0; col < this.columns(); col++) {
				res.setValue(this.getRow(row).dot(matrix.getColumn(col)), row, col);
			}
		}
		
		return res;
	}

	@Override
	public VectorMatrix<T> getZero() {
		try {
			return new VectorMatrix<T>(this.getValue(0, 0), this.rows(), this.columns());
		} catch (SizeArgumentException e) {
			// Impossible Exception
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public VectorMatrix<T> getIdentity() {
		VectorMatrix<T> res = this.getZero();
		for(int i = 0; i < this.rows(); i++) {
			try {
				res.setValue((T)this.getValue(0, 0).getUnit(), i, i);
			} catch (SizeArgumentException e) {
				// Impossible Exception
				e.printStackTrace();
			}
		}
		
		return res;
	}

	@Override
	public RMatrix<T> traspose() {
		VectorMatrix<T> res;
		try {
			res = new VectorMatrix<T>(this.getValue(0, 0), this.columns(), this.rows());
		
			for(int row = 0; row < this.rows(); row++) {
				for(int col = 0; col < this.columns(); col++) {
					try {
						res.setValue(this.getValue(col, row), row, col);
					} catch (SizeArgumentException e) {
						// Impossible Exception
						e.printStackTrace();
						return null;
					}
				}
			}
			
			return res;
		} catch (SizeArgumentException e1) {
			// Impossible Exception
			e1.printStackTrace();
			return null;
		}
	}

	@Override
	public RVector<T> apply(RVector<T> vector) throws SizeArgumentException {
		if(this.columns() != vector.size()) {
			throw new SizeArgumentException("Bad size for vector to apply");
		}
		
		VectorList<T> res = new VectorList<T>(this.getValue(0, 0), this.rows());
		
		for(int row = 0; row < this.rows(); row++) {
			res.setValue(this.getRow(row).dot(vector), row);
		}
		
		return res;
	}

	//Copiable methods
	@SuppressWarnings("unchecked")
	@Override
	public Copiable copy() {
		ArrayList<RVector<T>> copy = new ArrayList<>();
		
		for(RVector<T> row : this.matrix) {
			copy.add((RVector<T>) row.copy());
		}
		
		try {
			return new VectorMatrix<T>(copy);
		} catch (SizeArgumentException e) {
			// Impossible Exception
			e.printStackTrace();
			return null;
		}
	}
}
