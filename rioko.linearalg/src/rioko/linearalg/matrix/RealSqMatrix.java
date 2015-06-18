package rioko.linearalg.matrix;

import java.util.ArrayList;
import java.util.HashMap;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;

import rioko.linearalg.RDouble;
import rioko.linearalg.exceptions.SizeArgumentException;
import rioko.linearalg.exceptions.ZeroNumberException;
import rioko.linearalg.la4j.SymmetricMatrixPredicate;
import rioko.linearalg.vector.RVector;
import rioko.linearalg.vector.VectorList;

public class RealSqMatrix extends VectorMatrix<RDouble> {

	private HashMap<RDouble, Integer> eigenvalues = new HashMap<>();
	private HashMap<RDouble, ArrayList<RVector<RDouble>>> eigenvectors = new HashMap<>();
	
	boolean done = false;
	
	private Matrix stubMatrix;
	
	//Builders
	public RealSqMatrix(ArrayList<RVector<RDouble>> matrix) throws SizeArgumentException {
		super(matrix);
		
		if(this.rows() != this.columns()) {
			throw new SizeArgumentException("No square matrix created");
		}
	}

	public RealSqMatrix(int size) throws SizeArgumentException {
		super(RDouble.zero(), size, size);
	}
	
	//OVerriden methods
	@Override
	public void setValue(RDouble number, int row, int col) throws SizeArgumentException {
		super.setValue(number, row, col);
		
		this.done = false;
	}
	
	@Override
	public void setRow(RVector<RDouble> vector, int row) throws SizeArgumentException {
		super.setRow(vector, row);
		
		this.done = false;
	}
	
	@Override
	public void setColumn(RVector<RDouble> vector, int col) throws SizeArgumentException {
		super.setColumn(vector, col);
		
		this.done = false;
	}

	//Other methods
	public HashMap<RDouble, Integer> getEigenvalues() {
		return this.eigenvalues;
	}
	
	public HashMap<RDouble, ArrayList<RVector<RDouble>>> getEigenvectors() {
		return this.eigenvectors;
	}

	public void run() {
		if(!done) {
			//Creamos la matrix auxiliar
			this.stubMatrix = new Basic2DMatrix(this.rows(), this.columns());
			for(int row = 0; row < this.rows(); row++) {
				for(int col = 0; col < this.columns(); col++) {
					try {
						this.stubMatrix.set(row, col, this.getValue(row, col).getValue());
					} catch (SizeArgumentException e) {
						// Impossible Exception
						e.printStackTrace();
					}
				}
			}
			
			if(!this.stubMatrix.is(new SymmetricMatrixPredicate())) {
				throw new IllegalArgumentException("No symmetric matrix");
			}
			
			EigenDecompositor decompositor = new EigenDecompositor(this.stubMatrix);
			Matrix[] decomposition = decompositor.decompose();
			
			Matrix values = decomposition[1];
			Matrix vectors = decomposition[0];
			
			for(int i = 0; i<this.rows(); i++) {
				int prev = 0;
				RDouble value = new RDouble(values.get(i, i));
				
				if(this.eigenvalues.containsKey(value)) {
					prev = this.eigenvalues.get(value);
				} else {
					this.eigenvectors.put(value, new ArrayList<RVector<RDouble>>());
				}
				
				this.eigenvalues.put(value, prev+1);
	
				try {
					RVector<RDouble> vector = this.parseVector(vectors.getColumn(i));
					RDouble norm = new RDouble(vector.norm());
					
					//The eigenvectors are normalized
					this.eigenvectors.get(value).add(vector.scalar((RDouble) norm.inverse()));
				} catch (SizeArgumentException | ZeroNumberException e) {
					// Impossible Exception
					e.printStackTrace();
				}
			}
			
			this.done = true;
		}
	}
	
	private RVector<RDouble> parseVector(Vector vector) throws SizeArgumentException {
		ArrayList<RDouble> vec = new ArrayList<>();
		
		for(int i = 0; i < vector.length(); i++) {
			vec.add(new RDouble(vector.get(i)));
		}
		
		return new VectorList<RDouble>(vec);
	}
}
